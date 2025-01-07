package com.example.myshop.data

data class MyProduct(val id: String, val name: String, val price: Double) {
    fun mapTo(): MutableMap<String, Any> {
        return mutableMapOf(
            "product_id" to this.id,
            "quantity" to this.name,
            "price" to this.price
        )
    }
}
