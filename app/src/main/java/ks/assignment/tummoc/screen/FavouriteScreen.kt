package ks.assignment.tummoc.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ks.assignment.tummoc.models.Item
import ks.assignment.tummoc.viewmodels.MainViewModel

@Composable
fun FavouriteScreen(
    mainViewModel: MainViewModel, onBackClick: () -> Unit, onFavoriteClick: (itemId: Int) -> Unit,
    onAddToCartClick: (itemId: Int) -> Unit,
) {
    val favItems = remember {
        mutableStateListOf<Item>()
    }

    val favItemsState by mainViewModel.favItems.observeAsState(emptyList())
    favItems.clear()
    favItems.addAll(favItemsState)

    LaunchedEffect(Unit) {
        mainViewModel.fetchFavItems()
    }
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = "Favourite",
                    Modifier.padding(10.dp, 0.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            navigationIcon = {
                IconButton(onClick = { onBackClick() }) {
                    Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Back")
                }
            },
            backgroundColor = Color.Transparent,
            elevation = 0.dp
        )
    })
    { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            // List of Cart Items
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                itemsIndexed(favItems) { index, favItem ->
                    favItemRow(favItem = favItem, index = index,
                        onFavoriteClick = { itemId, index ->
                            // remove the item and update the LazyColumn
                            favItems.removeAt(index)
                            onFavoriteClick(itemId)
                        }, onAddToCartClick = onAddToCartClick
                    )
                }
            }
        }
    }
}

@Composable
fun favItemRow(
    favItem: Item,
    index: Int,
    onFavoriteClick: (itemId: Int, index: Int) -> Unit,
    onAddToCartClick: (itemId: Int) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(120.dp),
        elevation = 4.dp

    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = favItem.icon,
                contentDescription = "Item Image",
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
            )
            Column (Modifier.weight(1f)){
                Text(
                    text = favItem.name,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "₹${favItem.price}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = {
                    onFavoriteClick(favItem.id, index)
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Favorite,
                        contentDescription = "favourite",
                        tint = Color.Red
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp)) // Set the corner radius as needed
                        .background(color = Color(0xffFC7100))
                ) {
                    IconButton(
                        onClick = {
                            onAddToCartClick(favItem.id)
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