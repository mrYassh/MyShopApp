package com.example.myshop.network

import com.example.myshop.model.Product
import com.example.myshop.model.ProductX
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface ProductsApi {
    @GET(value = "products/")
    suspend fun getProducts(@Query(value = "") query: String): Product

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): ProductX

    @GET(value = "products/search?")
    suspend fun getProductsBySearch(@Query(value = "q") query: String): Product


}