package com.example.myshop.screen.homescreen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myshop.R
import com.example.myshop.components.MyShopProductCard
import com.example.myshop.components.MyShopTopAppBar
import com.example.myshop.data.DataOrException
import com.example.myshop.model.Product
import com.example.myshop.navigation.MyShopScreens
import com.example.myshop.viewmodel.LoginScreenViewModel
import com.example.myshop.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(navController: NavController, mainViewModel: MainViewModel = hiltViewModel()) {
    val productInfo = produceState<DataOrException<Product, Boolean, Exception>>(
        initialValue = DataOrException(loading = true)
    ) {
        value = mainViewModel.getProducts(query = "")

    }.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            scaffoldState = scaffoldState,
            drawerContent = {
                SideDrawer(navController = navController) {
                    navController.navigate(MyShopScreens.OrderScreen.name)
                }
            },
            topBar = {
                MyShopTopAppBar(
                    title = "MyShop App",
                    isMainscreen = true,
                    icon1 = Icons.Default.Search,
                    icon2 = Icons.Default.ShoppingCart,
                    onIcon1 = { navController.navigate(MyShopScreens.SearchScreen.name) },
                    onIcon2 = { navController.navigate(MyShopScreens.CartScreen.name) }) {
                    Log.d("menu", "HomeScreen: ")
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            },
        ) {
            if (productInfo.loading == true) {
                Log.d("home", "HomeScreen: ${productInfo.loading.toString()}")
                Surface(modifier = Modifier.fillMaxSize()) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else if (productInfo.data != null) {
                Column(
                    modifier = Modifier.padding(it),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(10.dp)
                    ) {
                        items(productInfo.data!!.products) { item ->
                            MyShopProductCard(item = item, navController)
                        }
                    }
                }
            } else if (productInfo.e != null) {
                Text(text = productInfo.e!!.message.toString())

            }
        }
    }
}

@Composable
fun SideDrawer(
    viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navController: NavController,
    onClick: () -> Unit = {},
) {
    val auth = FirebaseAuth.getInstance()
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Surface(
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth(), color = Color.Gray
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome To ",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp, bottom = 5.dp),
                    color = Color.LightGray,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Left,
                )
                Text(
                    text = "MyShop App",
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = Color.White
                )
            }
        }
        val user = auth.currentUser?.email?.split("@")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "profile",
                modifier = Modifier.size(40.dp)
            )
            if (!user.isNullOrEmpty()) {
                Text(
                    text = "${user!![0][0].uppercase()}${user[0].substring(1)}",
                    modifier = Modifier.padding(start = 30.dp),
                    color = Color.Gray,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
                )
            }
        }
        Divider()
        Card(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .clickable {
                    onClick()
                }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.order_delivery),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(40.dp)
                    )
                    Text(
                        text = "My Orders",
                        modifier = Modifier.padding(start = 30.dp),
                        fontSize = 20.sp,
                        color = Color.Gray
                    )
                }
            }
        }
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate(MyShopScreens.CartScreen.name) }
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = "My Cart",
                modifier = Modifier.padding(start = 30.dp),
                fontSize = 20.sp,
                color = Color.Gray
            )
        }
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate(MyShopScreens.AboutScreen.name) }
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = "About Us",
                modifier = Modifier.padding(start = 30.dp),
                fontSize = 20.sp,
                color = Color.Gray
            )
        }
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    viewModel.signOut(
                        onSignOutSuccess = {
                            // Handle successful sign-out (e.g., navigate to login screen)
                            navController.navigate(MyShopScreens.LoginScreen.name)
                        },
                        onSignOutFailure = { exception ->
                            // Handle failure (e.g., show an error message)
                            println("Failed to sign out: ${exception.message}")
                        }
                    )
                }
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = "Log Out",
                modifier = Modifier.padding(start = 30.dp),
                fontSize = 20.sp,
                color = Color.Gray
            )
        }
        Divider()
    }
}