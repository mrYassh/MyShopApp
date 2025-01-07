package com.example.myshop.screen.orderscreen


import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myshop.components.MyShopTopAppBar
import com.example.myshop.data.Address
import com.example.myshop.data.DataOrException
import com.example.myshop.data.Order
import com.example.myshop.data.OrderItem
import com.example.myshop.model.ProductX
import com.example.myshop.navigation.MyShopScreens
import com.example.myshop.viewmodel.OrderViewModel
import com.example.myshop.viewmodel.ProductViewModel
import java.util.Locale

@Composable
fun OrderScreen(navController: NavController) {
    val viewModel: OrderViewModel = viewModel()
    val orders by viewModel.orders.collectAsState()
    val addresses by viewModel.addresses.collectAsState()

    Scaffold(
        topBar = {
            MyShopTopAppBar(
                title = "My Orders",
                isMainscreen = false,
                icon1 = Icons.Default.Search,
                onIcon1 = { navController.navigate(MyShopScreens.SearchScreen.name) },
                icon2 = Icons.Default.ShoppingCart,
                onIcon2 = { navController.navigate(MyShopScreens.CartScreen.name) }) {
                navController.popBackStack()
            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            if (orders.isEmpty()) {
                Text(text = "No orders found.")
            } else {
                LazyColumn {
                    items(orders) { item ->
                        OrderItems(order = item, navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun OrderItems(order: Order, navController: NavController) {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(5.dp)) {
            Text(
                text = "Date & Time : ${
                    SimpleDateFormat(
                        "dd MMMM yyyy, HH:mm",
                        Locale.ENGLISH
                    ).format(order.timestamp)
                }"
            )
            Text(text = "Order Id : ${order.orderId} ")
            LazyRow {
                items(order.items) { item ->
                    MyShopProductOrdersByIdCard(
                        itemId = item.productId.toInt(),
                        quantity = item.quantity,
                        navController = navController
                    )
                }
            }
            Text(text = "Status : ${order.status}")
            Text(text = "Total Amount : ${order.totalAmount}")
        }
    }
    Divider()
}

@Composable
fun AddressSection(addresses: List<Address>, onAddressSelect: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = "Deliver to:", style = MaterialTheme.typography.h6)
        LazyColumn {
            items(addresses) { address ->
                AddressItem(address, address.selected, onSelect = {
                    onAddressSelect(address.id)
                })
            }
        }
    }
}

@Composable
fun AddressItem(address: Address, isSelected: Boolean, onSelect: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = address.name, style = MaterialTheme.typography.subtitle1)
            Text(text = address.street, style = MaterialTheme.typography.body1)
            Text(
                text = "${address.city}, ${address.state} - ${address.postalCode}",
                style = MaterialTheme.typography.body1
            )
            Text(text = address.phoneNumber, style = MaterialTheme.typography.body1)
            Button(
                onClick = onSelect,
                colors = ButtonDefaults.buttonColors(backgroundColor = if (isSelected) Green else Yellow)
            ) {
                Text(text = if (isSelected) "Selected" else "Select")
            }
        }
    }
}

@Composable
fun OrderSummary(order: Order) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = "Order Summary", style = MaterialTheme.typography.h6)
        order.items.forEach { item ->
            OrderItemRow(item)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Total: ₹${order.totalAmount}", style = MaterialTheme.typography.h6)
    }
}

@Composable
fun OrderItemRow(orderItem: OrderItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Qty: ${orderItem.quantity}", style = MaterialTheme.typography.body2)
            Text(text = "Price: ₹${orderItem.price}", style = MaterialTheme.typography.body2)
        }
        Text(
            text = "₹${orderItem.price * orderItem.quantity}",
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
fun MyShopProductOrdersByIdCard(
    itemId: Int,
    quantity: Int = 0,
    navController: NavController,
    viewModel: ProductViewModel = hiltViewModel(),
) {
    val productInfo = produceState<DataOrException<ProductX, Boolean, Exception>>(
        initialValue = DataOrException(loading = true)
    ) {
        value = viewModel.getProductById(itemId.toInt())

    }.value
    if (productInfo.loading == true) {
        Log.d("home", "HomeScreen: ${productInfo.loading.toString()}")
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

            }
        }
    } else if (productInfo.data != null) {
        val item = productInfo.data!!
        androidx.compose.material3.Card(
            modifier = Modifier
                .width(380.dp)
                .padding(10.dp)
                .clickable {
                    if (item != null) {
                        navController.navigate(MyShopScreens.ProductScreen.name + "/${item.id}")
                    }
                }
        ) {
            Row(
                modifier = Modifier.padding(5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                if (item != null) {
                    AsyncImage(
                        model = item.images[0],
                        contentDescription = "product",
                        modifier = Modifier
                            .width(150.dp),
                        contentScale = ContentScale.Fit
                    )
                }
                Column {
                    if (item!!.brand != null) {
                        Text(
                            text = item.brand,
                            fontSize = androidx.compose.material3.MaterialTheme.typography.titleMedium.fontSize,
                            fontWeight = androidx.compose.material3.MaterialTheme.typography.titleMedium.fontWeight
                        )
                    }
                    if (item.title != null) {
                        Text(
                            text = item.title,
                            modifier = Modifier.padding(vertical = 2.dp),
                            fontSize = androidx.compose.material3.MaterialTheme.typography.labelMedium.fontSize,
                            fontWeight = androidx.compose.material3.MaterialTheme.typography.labelMedium.fontWeight,
                            color = Color.Gray,
                            lineHeight = 15.sp
                        )
                    }

                    Text(text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.W500,
                                fontSize = androidx.compose.material3.MaterialTheme.typography.titleMedium.fontSize
                            )
                        ) { append("$${item.price}") }
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF964B00)
                            )
                        ) { append("  ${item.discountPercentage}%off") }
                    }

                    )
                    if (quantity != 0) {
                        Text(
                            text = "Quantity : $quantity",
                            modifier = Modifier.padding(top = 10.dp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}