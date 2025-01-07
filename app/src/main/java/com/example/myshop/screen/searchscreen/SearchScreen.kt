package com.example.myshop.screen.searchscreen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myshop.components.InputField
import com.example.myshop.components.MyShopTopAppBar
import com.example.myshop.components.ProductRowCard
import com.example.myshop.viewmodel.SearchScreenViewModel

@Composable
fun MyShopAppSearchScreen(
    navController: NavController,
    searchScreenViewModel: SearchScreenViewModel = hiltViewModel(),
) {

    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(topBar = {
            MyShopTopAppBar(
                title = "Search Products",
                isMainscreen = false,
            ) {
                navController.popBackStack()
            }
        }) {
            Column {
                Spacer(modifier = Modifier.padding(it))
                SearchForm() {
                    searchScreenViewModel.searchProduct(it)
                }
                Spacer(modifier = Modifier.height(15.dp))
                ProductList(
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun ProductList(
    navController: NavController,
    searchScreenViewModel: SearchScreenViewModel = hiltViewModel(),
) {
    val products = searchScreenViewModel.product
    if (searchScreenViewModel.isLoading) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(products) { item ->
                ProductRowCard(navController, item = item)
                Log.d("productitem", "ProductList: $item ")
            }
        }
    }
}


@Composable
fun SearchForm(modifier: Modifier = Modifier, onSearch: (search: String) -> Unit) {
    val searchState = rememberSaveable {
        mutableStateOf("")
    }
    val valid = remember(searchState.value) {
        searchState.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    InputField(
        valueState = searchState,
        labelId = "Search",
        enabled = true,
        onAction = KeyboardActions {
            if (!valid) return@KeyboardActions
            onSearch(searchState.value)
            keyboardController?.hide()
        })
}