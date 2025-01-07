package com.example.myshop.screen.loginscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myshop.components.EmailInput
import com.example.myshop.components.MyShopLogoAndName
import com.example.myshop.components.PasswordInput
import com.example.myshop.components.ShowButton
import com.example.myshop.navigation.MyShopScreens
import com.example.myshop.viewmodel.LoginScreenViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginScreenViewModel = viewModel(),
) {
    val showLoginForm = rememberSaveable {
        mutableStateOf(true)
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            MyShopLogoAndName(modifier = Modifier.padding(top = 50.dp, bottom = 40.dp))
            val message = loginViewModel.message.value
            if (showLoginForm.value) {
                UserLoginForm(createAccount = false, isLoading = false, message) { eml, psd ->
                    loginViewModel.loginUserWithEmailAndPassword(eml, psd) {
                        navController.navigate(MyShopScreens.HomeScreen.name) {
                            popUpTo(0)
                        }
                    }
                }
            } else {
                UserLoginForm(createAccount = true, isLoading = false, message) { eml, psd ->
                    loginViewModel.createUserWithEmailAndPassword(eml, psd)
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            if (showLoginForm.value) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "New User? ")
                    Text(
                        text = "Sign Up",
                        modifier = Modifier.clickable {
                            showLoginForm.value = !showLoginForm.value
                        },
                        color = Color.Yellow,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Already User? ")
                    Text(
                        text = "Login",
                        modifier = Modifier.clickable {
                            showLoginForm.value = !showLoginForm.value
                        },
                        color = Color.Yellow,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


@Composable
fun UserLoginForm(
    createAccount: Boolean,
    isLoading: Boolean = false, message: String,
    onDone: (String, String) -> Unit = { email, pass -> },
) {
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val passwordVisibility = rememberSaveable {
        mutableStateOf(false)
    }
    val passwordFocusRequest = FocusRequester()
    val valid = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val modifier = Modifier

    Column(
        modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailInput(emailState = email, enabled = !isLoading, onAction = KeyboardActions {
            passwordFocusRequest.requestFocus()
        })
        PasswordInput(modifier = Modifier.focusRequester(passwordFocusRequest),
            passwordState = password,
            enabled = !isLoading,
            passwordVisibility = passwordVisibility,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onDone(email.value.trim(), password.value.trim())
                keyboardController?.hide()
            })
        if (message.trim().isNotEmpty()) {
            Text(text = message, fontWeight = FontWeight.Bold, color = Color.Red.copy(alpha = 0.5f))
        }
        ShowButton(
            textId = if (createAccount) "Create Account" else "Login",
            loading = isLoading,
            validInputs = valid
        ) {
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }
    }
}


