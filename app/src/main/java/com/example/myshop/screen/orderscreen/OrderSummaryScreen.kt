package com.example.myshop.screen.orderscreen

import CartViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myshop.components.MyShopTopAppBar
import com.example.myshop.navigation.MyShopScreens
import com.example.myshop.screen.cartscreen.CartItemRow
import com.example.myshop.screen.cartscreen.PriceAndOrderButton

@Composable
fun OrderSummaryScreen(navController: NavController, viewModel: CartViewModel = viewModel()) {
    val cartItems by viewModel.cartItems.collectAsState()
    val directItem by viewModel.directItem.observeAsState(emptyList())
    val totalAmount by viewModel.totalAmount.collectAsState()
    val scrollState = rememberScrollState()
    Scaffold(topBar = {
        MyShopTopAppBar(title = "Order Summary", isMainscreen = false) {
            navController.popBackStack()
        }
    }, bottomBar = {
        PriceAndOrderButton(
            viewModel = viewModel,
            totalAmount = if (directItem?.isEmpty()!!) totalAmount.toInt() else (directItem!![0].price * directItem!![0].quantity).toInt(),
            navController = navController
        ) {
            navController.navigate(MyShopScreens.PaymentScreen.name)
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
            AddressSummaryPaymentBar(sColor = Color.Blue.copy(0.6f), sTextColor = Color.White)
            if (directItem?.isEmpty() == true) {
                cartItems.forEach { cartItem ->
                    CartItemRow(cartItem = cartItem, viewModel = viewModel, navController)
                }
            } else {
                directItem?.forEach { item ->
                    CartItemRow(
                        cartItem = item,
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}
