package com.example.myshop.screen.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myshop.components.MyShopLogoAndName
import com.example.myshop.navigation.MyShopScreens
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val scale = remember { Animatable(0.0f) }
    LaunchedEffect(key1 = true, block = {
        scale.animateTo(
            0.9f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = { OvershootInterpolator(8f).getInterpolation(it) })
        )
        delay(2000L)
        if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
            navController.navigate(MyShopScreens.LoginScreen.name) {
                popUpTo(0)
            }
        } else {
            navController.navigate(MyShopScreens.HomeScreen.name) {
                popUpTo(0)
            }
        }

    })
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(2.dp)
            .scale(scale.value)
    ) {
        MyShopLogoAndName()
    }
}