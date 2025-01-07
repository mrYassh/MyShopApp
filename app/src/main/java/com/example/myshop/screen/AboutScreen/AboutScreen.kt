package com.example.myshop.screen.AboutScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myshop.R
import com.example.myshop.components.MyShopTopAppBar

@Composable
fun AboutScreen(navController: NavController) {

    Column {
        Scaffold(topBar = {
            MyShopTopAppBar(title = "About us", isMainscreen = false) {
                navController.popBackStack()
            }
        }) {
            it
            Surface(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 50.dp, horizontal = 25.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.myshoplogo),
                        contentDescription = "App Logo"
                    )
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "MyShop App", fontSize = 25.sp, fontWeight = FontWeight.Bold)
                        Text(text = "version : 1.0", fontSize = 20.sp, fontWeight = FontWeight.W500)
                    }
                    Text(
                        text = " This app is an e-commerce platform designed for Android using Jetpack Compose. It features user authentication, product browsing, and a shopping cart. Users can place orders, manage their addresses, and track order statuses. Key technologies include Firebase for backend services, Hilt and Dagger for dependency injection, and Retrofit for network operations.\n" +
                                "\n",
                        textAlign = TextAlign.Justify
                    )
                    Text(
                        text = "This App is developed by\n Mr. Yash Dipke",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(text = "© All rights reserved", fontWeight = FontWeight.Bold)
                }
            }

        }
    }
}