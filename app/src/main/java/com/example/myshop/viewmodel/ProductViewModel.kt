package com.example.myshop.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myshop.data.DataOrException
import com.example.myshop.model.ProductX
import com.example.myshop.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val repository: ProductRepository) :
    ViewModel() {
    suspend fun getProductById(id: Int): DataOrException<ProductX, Boolean, Exception> {
        return repository.getProductsById(id)
    }
}