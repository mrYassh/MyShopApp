package com.example.myshop.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshop.data.Resource
import com.example.myshop.model.ProductX
import com.example.myshop.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(private val repository: ProductRepository) :
    ViewModel() {
    var product: List<ProductX> by mutableStateOf(value = listOf())
    var isLoading: Boolean by mutableStateOf(false)


    fun searchProduct(query: String) {
        viewModelScope.launch(Dispatchers.Main) {
            if (query.isEmpty()) {
                return@launch
            }
            try {
                isLoading = true
                when (val response = repository.getProductBySearch(query)) {

                    is Resource.Success -> {
                        product = response.data!!.products
                        if (product.isNotEmpty()) isLoading = false
                    }

                    is Resource.Error -> {
                        isLoading = false
                        Log.e("SearchViewModel", "searchProduct: Failed getting products")
                    }

                    else -> {
                        isLoading = false
                    }
                }
                isLoading = false
            } catch (exception: Exception) {
                isLoading = false
                Log.d("SearchViewModel", "searchProducts: ${exception.message.toString()}")
            }
        }
    }
}