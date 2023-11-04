package ks.assignment.tummoc.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ks.assignment.tummoc.models.Item
import ks.assignment.tummoc.viewmodels.MainViewModel

@Composable
fun CartScreen(
    mainViewModel: MainViewModel,
    onBackClick: () -> Unit,
    onCheckoutClick: () -> Unit,
    onAddItemClick: (itemId: Int) -> Unit,
    onRemoveItemClick: (itemId: Int) -> Unit
) {
    val cartItems = remember {
        mutableStateListOf<Item>()
    }

    val cartItemsState by mainViewModel.cartItems.observeAsState(emptyList())
    cartItems.clear()
    cartItems.addAll(cartItemsState)

//    val cartItems by mainViewModel.cartItems.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        mainViewModel.fetchCartItems()
    }
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = "Cart",
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
                itemsIndexed(cartItems) { index, cartItem ->
                    CartItemRow(
                        cartItem = cartItem,
                        onRemoveItemClick = { itemId, quantity, index ->
                            print("Quantity : ${quantity}")
                            if(quantity == 1) {
                                cartItems.removeAt(index)
                            }
                            onRemoveItemClick(itemId)
                        },
                        onAddItemClick = { itemId, quantity, index ->
                            onAddItemClick(itemId)
                        },
                        index = index
                    )
                }
            }

            // Card with Total Amount and Discount
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    var subTotal = cartItems.sumByDouble { it.price*it.quantity }
                    var discount = 0.0
                    var total = subTotal - discount
                    Text(text = "Sub Total: ₹${subTotal}")
                    Text(text = "Discount: ₹${discount}")
                    Text(text = "Total Amount: ₹${total}")
                }
            }

            // Checkout Button
            Button(
                onClick = { onCheckoutClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(text = "Checkout")
            }
        }
    }

}


@Composable
fun CartItemRow(
    cartItem: Item,
    index: Int,
    onAddItemClick: (itemId: Int, quantity: Int, index: Int) -> Unit,
    onRemoveItemClick: (itemId: Int, quantity: Int, index: Int) -> Unit
) {
    var quantity = remember {
        mutableStateOf(cartItem.quantity)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(120.dp),
        elevation = 4.dp

    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = cartItem.icon,
                contentDescription = "Item Image",
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
            )
            Column(Modifier.weight(1f)) {
                Text(
                    text = cartItem.name,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "₹${cartItem.price}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp)) // Set the corner radius as needed
                            .background(color = Color(0xffFC7100))
                    ) {
                        IconButton(
                            onClick = {
                                --quantity.value
                                onRemoveItemClick(cartItem.id, cartItem.quantity, index)
                            },
                            modifier = Modifier
                                .background(color = Color(0xffFC7100))
                                .clip(RoundedCornerShape(5.dp))
                                .width(30.dp)
                                .height(30.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Minus",
                                Modifier.padding(5.dp),
                                tint = Color.White
                            )
                        }
                    }
                    Text(
                        text = "${quantity.value}",
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp)) // Set the corner radius as needed
                            .background(color = Color(0xffFC7100))
                    ) {
                        IconButton(
                            onClick = {
                                ++quantity.value
                                onAddItemClick(cartItem.id, cartItem.quantity, index)
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
                val totalPrice = quantity.value * cartItem.price
                val formattedPrice = String.format("%.1f", totalPrice)
                Text(
                    text = "₹${formattedPrice}",
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                )
            }
        }
    }
}