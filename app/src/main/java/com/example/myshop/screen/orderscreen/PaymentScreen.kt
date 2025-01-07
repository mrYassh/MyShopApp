package com.example.myshop.screen.orderscreen

import CartViewModel
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myshop.components.MyShopTopAppBar
import com.example.myshop.navigation.MyShopScreens
import com.example.myshop.screen.cartscreen.PriceAndOrderButton
import com.example.myshop.viewmodel.OrderViewModel

@Composable
fun PaymentScreen(
    navController: NavController,
    cartViewModel: CartViewModel = viewModel(),
    orderViewModel: OrderViewModel = viewModel(),
) {
    val context = LocalContext.current
    val directItem by cartViewModel.directItem.observeAsState()
    val cartItem by cartViewModel.cartItems.collectAsState()
    val totalAmount =
        if (directItem?.isEmpty() == true) cartViewModel.totalAmount.collectAsState().value else ((directItem?.get(
            0
        )?.price!!) * directItem?.get(0)?.quantity!!)
    Scaffold(topBar = {
        MyShopTopAppBar(title = "Payment", isMainscreen = false) {
            navController.popBackStack()
        }
    },
        bottomBar = {
            PriceAndOrderButton(
                viewModel = cartViewModel,
                navController = navController
            ) {
                orderViewModel.onOrderButtonClick(
                    (if (directItem!!.isEmpty()) cartItem else directItem)!!,
                    totalAmount
                )
                Toast.makeText(context, "Order is Successfully Placed", Toast.LENGTH_SHORT).show()
                cartViewModel.removeCart()
                cartViewModel.removeAllItemsFromCart()
                navController.navigate(MyShopScreens.HomeScreen.name)
            }
        }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AddressSummaryPaymentBar(pColor = Color.Blue.copy(0.6f), pTextColor = Color.White)
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        modifier = Modifier
                            .height(300.dp)
                            .width(300.dp)
                            .padding(10.dp), backgroundColor = Color.LightGray,
                        border = BorderStroke(2.dp, color = Color.Red)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Payment Functionality is Not Implemented",
                                modifier = Modifier.padding(20.dp),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

        }
    }
}


