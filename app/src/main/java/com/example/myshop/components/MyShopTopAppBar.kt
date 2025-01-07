package com.example.myshop.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyShopTopAppBar(
    title: String,
    isMainscreen: Boolean,
    icon1: ImageVector? = null,
    icon2: ImageVector? = null,
    onIcon1: () -> Unit = {},
    onIcon2: () -> Unit = {},
    onNav: () -> Unit,
) {
    CenterAlignedTopAppBar(title = { Text(text = title) },
        navigationIcon = {
            if (isMainscreen) {
                IconButton(onClick = onNav) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                }
            } else {
                IconButton(onClick = onNav) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {

            if (icon1 != null) {
                IconButton(onClick = onIcon1) {
                    Icon(imageVector = icon1, contentDescription = "Search")
                }
            }
            if (icon2 != null) {
                IconButton(onClick = onIcon2) {
                    Icon(imageVector = icon2, contentDescription = "My Cart")
                }
            }


        })
}


@Composable
fun BuyAndCartButton(onBag: () -> Unit, onBuy: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight()
                .clickable { onBag() },
            shape = RectangleShape,
            color = Color.White
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Add to Bag",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()

                .clickable { onBuy() },
            shape = RectangleShape,
            color = Color.Black
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Buy now",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

}