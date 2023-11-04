package ks.assignment.tummoc.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import ks.assignment.tummoc.models.Category
import ks.assignment.tummoc.models.Item
import ks.assignment.tummoc.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dashboard(
    categories: List<Category>, navController: NavController,
    onFavoriteClick: (itemId: Int) -> Unit,
    onAddToCartClick: (itemId: Int) -> Unit,
    onRemoveFromCartClick: (itemId: Int) -> Unit,
    mainViewModel: MainViewModel
) {
    var isPopupVisible by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(-1) }
    val items by mainViewModel.categoryNames.observeAsState(emptyList())
    val favCount by mainViewModel.favCount.observeAsState(0)
    val cartCount by mainViewModel.cartCount.observeAsState(0)
    val listState = rememberLazyListState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isPopupVisible = !isPopupVisible
                    if (!isPopupVisible) {
                        selectedIndex = -1 // Reset selected index when closing the popup
                    }
                },
                content = {
                    Row(Modifier.padding(10.dp)) {
                        Icon(
                            imageVector = if (isPopupVisible) Icons.Default.Close else Icons.Default.DateRange,
                            contentDescription = "Toggle Popup",
                            tint = Color.White
                        )
                        if (!isPopupVisible) {
                            Spacer(modifier = Modifier.padding(start = 10.dp))
                            Text(
                                text = "Categories",
                                style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                },
                containerColor = Color.Black
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Store",
                        Modifier.padding(10.dp, 0.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = null,
                        modifier = Modifier.clickable { /* Handle menu icon click */ }
                    )
                },
                actions = {
                    BadgedBox(badge = {
                        Badge {
                            Text(text = "${favCount}")
                        }
                    }, Modifier.padding(10.dp, 0.dp)) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            modifier = Modifier
                                .clickable { navController.navigate("favoriteScreenRoute") }
                        )
                    }
                    BadgedBox(badge = {
                        Badge {
                            Text(text = "${cartCount}")
                        }
                    }, Modifier.padding(10.dp, 0.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier
                                .clickable { navController.navigate("cartScreenRoute") }
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xffFFE100),
                                Color(0xffFC7100)
                            )
                        )
                    )
                    .padding(8.dp)
                    .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
            )
        },

        ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            state = listState
        ) {
            items(categories) { category ->
                CategoryItem(
                    category = category,
                    onFavoriteClick = onFavoriteClick,
                    onAddToCartClick = onAddToCartClick,
                    onRemoveFromCartClick = onRemoveFromCartClick
                )
            }
        }

        if (isPopupVisible) {
            val popupOffset = with(LocalDensity.current) { (-80.dp).toPx() }
            Popup(
                onDismissRequest = {
                    isPopupVisible = false
                    selectedIndex = -1
                },
                properties = PopupProperties(focusable = true),
                alignment = Alignment.BottomCenter,
                offset = IntOffset(0, popupOffset.toInt()),
            ) {
                Column(
                    modifier = Modifier.background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(15.dp)),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items.forEachIndexed { index, item ->
                        Text(
                            text = item,
                            color = if (index == selectedIndex) MaterialTheme.colorScheme.primary else Color.Black,
                            style = TextStyle(fontSize = 18.sp),
                            modifier = Modifier
                                .clickable {
                                    selectedIndex = index
                                    //listState.scrollToItem(index)
                                    isPopupVisible = false
                                }
                                .padding(10.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewForMainItem() {
    val category = Category(
        55,
        "Food",
        listOf(
            Item(
                5501,
                "Potato Chips",
                "https://cdn-icons-png.flaticon.com/128/2553/2553691.png",
                40.0,
                isFavorite = false
            ),
            Item(
                5502,
                "Penne Pasta",
                "https://cdn-icons-png.flaticon.com/128/2553/2553691.png",
                110.40,
                isFavorite = true
            ),
            Item(
                5503,
                "Tomato Ketchup",
                "https://cdn-icons-png.flaticon.com/128/2553/2553691.png",
                80.0,
                isFavorite = false
            ),
            Item(
                5504,
                "Nutella Spread",
                "https://cdn-icons-png.flaticon.com/128/2553/2553691.png",
                120.0,
                isFavorite = true
            )
        )
    )
//    MaterialTheme {
//        CategoryItem(category)
//    }
}

@Composable
fun CategoryItem(
    category: Category,
    onFavoriteClick: (itemId: Int) -> Unit,
    onAddToCartClick: (itemId: Int) -> Unit,
    onRemoveFromCartClick: (itemId: Int) -> Unit,
) {
    var expanded = remember {
        mutableStateOf(true)
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(20.dp)
        ) {
            Text(
                text = category.name,
                style = MaterialTheme.typography.titleMedium,
            )
            Icon(
                imageVector = if(expanded.value) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier
                    .clickable { expanded.value = !expanded.value }
                    .padding(10.dp, 0.dp)
            )
        }
        greySpacer()
        if (expanded.value) {
            LazyRow {
                items(category.items) { item ->
                    subItem(
                        item = item,
                        onFavoriteClick = onFavoriteClick,
                        onAddToCartClick = onAddToCartClick,
                        onRemoveFromCartClick = onRemoveFromCartClick
                    )
                }
            }
        }
    }
}

@Composable
fun greySpacer() {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .background(color = Color.LightGray)
            .height(2.dp)
    )
}

@Composable
fun subItem(
    item: Item,
    onFavoriteClick: (itemId: Int) -> Unit,
    onAddToCartClick: (itemId: Int) -> Unit,
    onRemoveFromCartClick: (itemId: Int) -> Unit
) {
    var count = remember {
        mutableStateOf(0)
    }
    var isFavorite = remember {
        mutableStateOf(item.isFavorite)
    }
    Card(Modifier.padding(10.dp)) {
        Box(
            Modifier
                .width(150.dp)
                .height(180.dp)
        ) {
            AsyncImage(
                model = item.icon, contentDescription = "Item Image", modifier = Modifier
                    .width(120.dp)
                    .height(120.dp)
                    .align(Alignment.TopCenter)
                    .padding(top = 10.dp)
            )
            Icon(
                imageVector = if (isFavorite.value) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorite Icon",
                tint = Color.Red, // Tint color for the icon
                modifier = Modifier
                    .align(Alignment.TopEnd) // Align the icon to the top right corner
                    .padding(8.dp) // Add padding to the icon
                    .clickable {
                        isFavorite.value = !isFavorite.value
                        item.isFavorite = isFavorite.value;
                        onFavoriteClick(item.id)
                    }
            )
            Row(
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = "${item.name}",
                        style = TextStyle(fontWeight = FontWeight.Bold, color = Color.Black),
                        modifier = Modifier.padding(start = 10.dp, top = 2.dp)
                    )
                    Text(
                        text = "₹${item.price}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 10.dp, bottom = 5.dp, top = 5.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp)) // Set the corner radius as needed
                        .background(color = Color(0xffFC7100))
                        .align(Alignment.Bottom)
                ) {
                    IconButton(
                        onClick = {
                            count.value++;
                            onAddToCartClick(item.id)
                        },

                        modifier = Modifier
                            .background(color = Color(0xffFC7100))
                            .clip(RoundedCornerShape(5.dp))
                            .width(30.dp)
                            .height(30.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            Modifier.padding(5.dp),
                            tint = Color.White
                        )
                    }
                }

            }

        }
    }
}