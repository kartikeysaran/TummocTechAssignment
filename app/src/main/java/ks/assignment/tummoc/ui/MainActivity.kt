package ks.assignment.tummoc.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.runBlocking
import ks.assignment.tummoc.db.AppDatabase
import ks.assignment.tummoc.repository.AppRepository
import ks.assignment.tummoc.screen.CartScreen
import ks.assignment.tummoc.screen.Dashboard
import ks.assignment.tummoc.screen.FavouriteScreen
import ks.assignment.tummoc.viewmodels.MainViewModel
import ks.assignment.tummoc.viewmodels.viewmodel_factory.MainViewModelFactory


class MainActivity : ComponentActivity() {

    private val appDatabase: AppDatabase by lazy {
        AppDatabase.getInstance(applicationContext)
    }

    private val appRepository: AppRepository by lazy {
        AppRepository(applicationContext, appDatabase)
    }

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory(appRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.categoriesWithItems.observe(this, { categoriesWithItems ->
            setContent {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "dashboardRoute") {
                    composable("dashboardRoute") {
                        Dashboard(
                            categoriesWithItems,
                            navController,
                            onFavoriteClick = { itemId ->
                                mainViewModel.toggleFavorite(itemId)
                                mainViewModel.updateCount()
                            }, onAddToCartClick = { itemId ->
                                mainViewModel.addToCart(itemId)
                                mainViewModel.updateCount()
                            }, onRemoveFromCartClick = { itemId ->

                            },
                            mainViewModel
                        )
                    }

                    composable("cartScreenRoute") {
                        CartScreen(
                            mainViewModel,
                            onBackClick = {
                                navController.popBackStack(
                                    "dashboardRoute",
                                    inclusive = false
                                )
                            },
                            onCheckoutClick = {}, onRemoveItemClick = { itemId ->
                                mainViewModel.removeFromCart(itemId)
                            }, onAddItemClick = { itemId ->
                                mainViewModel.addToCart(itemId)
                            })
                    }

                    composable("favoriteScreenRoute") {
                        FavouriteScreen(
                            mainViewModel = mainViewModel,
                            onAddToCartClick = { itemId ->
                                mainViewModel.addToCart(itemId = itemId)
                            },
                            onFavoriteClick = { itemId ->
                                mainViewModel.toggleFavorite(itemId = itemId)
                            },
                            onBackClick = {
                                navController.popBackStack(
                                    "dashboardRoute",
                                    inclusive = false
                                )
                                mainViewModel.fetchCategoriesWithItems()
                                mainViewModel.updateCount()
                            })
                    }
                }
            }
        })
    }
}
