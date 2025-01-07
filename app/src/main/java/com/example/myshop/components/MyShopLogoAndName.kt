package com.example.myshop.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.myshop.R

@Composable
fun MyShopLogoAndName(modifier: Modifier=Modifier){
    Column(modifier = modifier, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = R.drawable.myshoplogo), contentDescription ="Logo" )
        Text(text = "MyShop App", style = MaterialTheme.typography.headlineSmall.copy(
            Color(0xFF4F2E8A)
        ), fontWeight = FontWeight.Bold, fontSize = 25.sp
        )
    }
}