package com.example.myshop.data

data class Address(
    val id: String = "",
    val name: String = "",
    val street: String = "",
    val city: String = "",
    val state: String = "",
    val postalCode: String = "",
    val phoneNumber: String = "",
    val selected: Boolean = false,
)

data class OrderItem(
    val productId: String = "",
    val quantity: Int = 0,
    val price: Double = 0.0,

    )

data class Order(
    val orderId: String = "",
    val userId: String = "",
    val items: List<OrderItem> = emptyList(),
    val totalAmount: Double = 0.0,
    val timestamp: Long = 0L,
    val status: String = "Pending",
    val address: Address? = null,
)
