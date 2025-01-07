package com.example.myshop.repository

import android.util.Log
import com.example.myshop.data.DataOrException
import com.example.myshop.data.Resource
import com.example.myshop.model.Product
import com.example.myshop.model.ProductX
import com.example.myshop.network.ProductsApi
import javax.inject.Inject

class ProductRepository @Inject constructor(private val api: ProductsApi) {
    suspend fun getProducts(query: String): DataOrException<Product, Boolean, Exception> {
        val response = try {
            api.getProducts(query)
        } catch (e: Exception) {
            Log.d("exp", "getProducts: $e")
            return DataOrException(e = e)
        }
        return DataOrException(data = response)
    }

    suspend fun getProductsById(id: Int): DataOrException<ProductX, Boolean, Exception> {
        val response = try {
            api.getProduct(id)
        } catch (e: Exception) {
            Log.d("exp", "getProducts: $e")
            return DataOrException(e = e)
        }
        return DataOrException(data = response)
    }


    suspend fun getProductBySearch(query: String): Resource<Product> {
        return try {
            Resource.Loading(data = true)
            val itemList = api.getProductsBySearch(query)
            if (itemList.products.isNotEmpty()) Resource.Loading(data = false)
            Resource.Success(data = itemList)
        } catch (e: Exception) {
            Log.d("productrepository", "getProductBySearch: ${e.message}")
            return Resource.Error(message = e.message)
        }
    }
}