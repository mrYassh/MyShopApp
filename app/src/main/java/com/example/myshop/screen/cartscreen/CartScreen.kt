package com.example.myshop.screen.cartscreen

import CartViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myshop.components.MyShopProductByIdCard
import com.example.myshop.components.MyShopTopAppBar
import com.example.myshop.data.CartItem
import com.example.myshop.navigation.MyShopScreens

@Composable
fun CartScreen(
    navController: NavController,
    viewModel: CartViewModel = viewModel(
    ),
) {

    val cartItems by viewModel.cartItems.collectAsState()
    val totalAmount by viewModel.totalAmount.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            MyShopTopAppBar(
                title = "Cart Summary",
                isMainscreen = false,
                icon1 = Icons.Default.Search,
                onIcon1 = { navController.navigate(MyShopScreens.SearchScreen.name) }) {
                navController.popBackStack()
            }
        },
        bottomBar = {
            PriceAndOrderButton(
                viewModel = viewModel,
                totalAmount = totalAmount.toInt(),
                navController = navController
            ) {
                if (!cartItems.isEmpty()) {
                    viewModel.removeAllItemsFromCart()
                    navController.navigate(MyShopScreens.AddressScreen.name)
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = it.calculateTopPadding(),
                    start = it.calculateStartPadding(layoutDirection = LayoutDirection.Ltr),
                    end = it.calculateEndPadding(LayoutDirection.Ltr),
                    bottom = 85.dp
                )

                .verticalScroll(scrollState), verticalArrangement = Arrangement.Top
        ) {
            if (cartItems.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Cart is Empty")
                }
            } else {
                cartItems.forEach { cartItem ->
                    CartItemRow(cartItem = cartItem, viewModel = viewModel, navController)
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    cartItem: CartItem,

    viewModel: CartViewModel,
    navController: NavController,
) {
    val directItem by viewModel.directItem.observeAsState(emptyList())
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally


    ) {
        MyShopProductByIdCard(cartItem.productId.toInt(), navController = navController)

        if (directItem?.isEmpty() == true) {
            Row {
                Button(
                    onClick = { viewModel.decreaseQuantity(cartItem.productId) },
                    shape = CircleShape
                ) {
                    Text(text = "-")
                }
                Text(text = "${cartItem.quantity}", modifier = Modifier.padding(8.dp))

                Button(
                    onClick = { viewModel.increaseQuantity(cartItem.productId) },
                    shape = CircleShape
                ) {
                    Text(text = "+")
                }
                Spacer(modifier = Modifier.width(15.dp))
                Button(onClick = { viewModel.removeItemFromCart(cartItem.productId) }) {
                    Text(text = "Remove")
                }
            }
        } else {
            Row {
                Button(
                    onClick = { viewModel.decreaseDirectItemQuantity(cartItem.productId) },
                    shape = CircleShape
                ) {
                    Text(text = "-")
                }
                Text(text = "${cartItem.quantity}", modifier = Modifier.padding(8.dp))

                Button(
                    onClick = { viewModel.increaseDirectItemQuantity(cartItem.productId) },
                    shape = CircleShape
                ) {
                    Text(text = "+")
                }
                Spacer(modifier = Modifier.width(15.dp))
            }
        }
    }
    Divider(modifier = Modifier.padding(top = 5.dp))
}


@Composable
fun PriceAndOrderButton(
    text: String = "Place Order",
    viewModel: CartViewModel,
    totalAmount: Int = viewModel.totalAmount.value.toInt(),
    navController: NavController,
    onClick: () -> Unit = {},
) {
    val directItem = viewModel.directItem.value;
    Surface(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Row(

            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceAround
        ) {
            if (directItem != null) {
                Text(
                    text = "$ ${if (directItem.isEmpty()) totalAmount else (directItem[0].price * directItem[0].quantity).toInt()}",
                    fontSize = 20.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }
            Button(
                onClick = {
                    onClick()
                },
                modifier = Modifier.padding(vertical = 5.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow)
            ) {
                Text(
                    text = text,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }
    }
}