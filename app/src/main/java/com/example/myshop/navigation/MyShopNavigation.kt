package com.example.myshop.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myshop.screen.AboutScreen.AboutScreen
import com.example.myshop.screen.cartscreen.CartScreen
import com.example.myshop.screen.homescreen.HomeScreen
import com.example.myshop.screen.loginscreen.LoginScreen
import com.example.myshop.screen.orderscreen.AddressScreen
import com.example.myshop.screen.orderscreen.OrderScreen
import com.example.myshop.screen.orderscreen.OrderSummaryScreen
import com.example.myshop.screen.orderscreen.PaymentScreen
import com.example.myshop.screen.productscreen.ProductScreen
import com.example.myshop.screen.searchscreen.MyShopAppSearchScreen
import com.example.myshop.screen.splash.SplashScreen

@Composable
fun MyShopNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = MyShopScreens.SplashScreen.name) {

        composable(MyShopScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }
        composable(MyShopScreens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }
        composable(MyShopScreens.HomeScreen.name) {
            HomeScreen(navController = navController)
        }
        val route = MyShopScreens.ProductScreen.name
        composable(
            "$route/{item}",
            arguments = listOf(navArgument(name = "item") { type = NavType.StringType }
            )
        ) { navback ->
            navback.arguments?.getString("item").let { item ->
                ProductScreen(navController, itemId = item.toString())
            }
        }
        composable(MyShopScreens.SearchScreen.name) {
            MyShopAppSearchScreen(navController = navController)
        }
        composable(MyShopScreens.CartScreen.name) {
            CartScreen(navController = navController)
        }
        composable(MyShopScreens.OrderScreen.name) {
            OrderScreen(navController = navController)
        }
        composable(MyShopScreens.AddressScreen.name) {
            AddressScreen(navController = navController)
        }
        composable(MyShopScreens.OrderSummaryScreen.name) {
            OrderSummaryScreen(navController = navController)
        }
        composable(MyShopScreens.PaymentScreen.name) {
            PaymentScreen(navController = navController)
        }
        composable(MyShopScreens.AboutScreen.name) {
            AboutScreen(navController = navController)
        }
    }
}