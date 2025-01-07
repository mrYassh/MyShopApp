package com.example.myshop.data

data class CartItem(val productId: String, var quantity: Int, var price: Double) {
    fun mapTo(): MutableMap<String, Any> {
        return mutableMapOf(
            "product_id" to this.productId,
            "quantity" to this.quantity,
            "price" to this.price
        )
    }
}
