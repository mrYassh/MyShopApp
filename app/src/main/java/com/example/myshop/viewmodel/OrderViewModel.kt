package com.example.myshop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshop.data.Address
import com.example.myshop.data.CartItem
import com.example.myshop.data.Order
import com.example.myshop.data.OrderItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class OrderViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val _addresses = MutableStateFlow<List<Address>>(emptyList())
    val addresses: StateFlow<List<Address>> get() = _addresses
    private val _selectedAddress = MutableStateFlow<Address?>(null)
    val selectedAddress: StateFlow<Address?> get() = _selectedAddress
    private val _order = MutableStateFlow<Order?>(null)
    val order: StateFlow<Order?> get() = _order
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> get() = _orders

    init {
        fetchAddresses()
        fetchOrders()
        fetchSelectedAddress()
    }

    fun placeOrder(order: Order, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val orderCollection = firestore.collection("orders")
        val orderId = orderCollection.document().id
        val orderWithId = order.copy(orderId = orderId, userId = auth.currentUser?.uid ?: "")

        orderCollection.document(orderId)
            .set(orderWithId)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun onOrderButtonClick(cartItems: List<CartItem>, totalAmount: Double) {
        val selectedAddress = _selectedAddress.value
        if (selectedAddress == null) {
            // Handle the case where no address is selected
            println("No address selected")
            return
        }

        val orderItems = cartItems.map { cartItem ->
            OrderItem(
                productId = cartItem.productId,

                quantity = cartItem.quantity,
                price = cartItem.price,

                )
        }

        val order = Order(
            userId = auth.currentUser?.uid ?: "",
            items = orderItems,
            totalAmount = totalAmount,
            timestamp = System.currentTimeMillis(),
            address = selectedAddress
        )

        placeOrder(order,
            onSuccess = {
                println("Order placed successfully")
                // Handle successful order placement (e.g., navigate to a confirmation screen)
            },
            onFailure = { exception ->
                println("Failed to place order: ${exception.message}")
                // Handle failure (e.g., show an error message to the user)
            }
        )
    }


    private fun fetchAddresses() {
        viewModelScope.launch {
            val userId = auth.uid.toString()
            val addressesSnapshot = firestore.collection("users").document(userId)
                .collection("addresses").get().await()

            val addresses = addressesSnapshot.documents.map { document ->
                val address = document.toObject(Address::class.java)
                address?.copy(id = document.id) ?: Address()
            }
            _addresses.value = addresses
        }
    }

    private fun fetchOrders() {
        viewModelScope.launch {
            try {
                val userId = auth.uid ?: return@launch
                val orderCollectionSnapshot = firestore.collection("orders")
                    .whereEqualTo("userId", userId)
                    .get()
                    .await()

                val orders = orderCollectionSnapshot.documents.mapNotNull { document ->
                    document.toObject(Order::class.java)
                }

                _orders.value = orders
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle the error
            }
        }
    }

    private fun fetchSelectedAddress() {
        viewModelScope.launch {
            val userId = auth.uid.toString()
            val selectedAddressSnapshot = firestore.collection("users").document(userId)
                .collection("addresses")
                .whereEqualTo("selected", true)
                .get()
                .await()

            val selectedAddress = selectedAddressSnapshot.documents.mapNotNull { document ->
                document.toObject(Address::class.java)?.copy(id = document.id)
            }.firstOrNull()

            _selectedAddress.value = selectedAddress
        }
    }

    fun selectAddress(addressId: String) {
        viewModelScope.launch {
            val userId = auth.uid.toString()
            val addressCollection =
                firestore.collection("users").document(userId).collection("addresses")

            // Fetch and update all addresses to deselect them
            val addressesSnapshot = addressCollection.get().await()
            for (document in addressesSnapshot.documents) {
                addressCollection.document(document.id).update("selected", false).await()
            }

            // Select the new address
            addressCollection.document(addressId).update("selected", true).await()
            fetchAddresses() // Refresh addresses
        }
    }


    fun addAddress(address: Address) {
        viewModelScope.launch {
            val userId = auth.uid.toString()
            firestore.collection("users").document(userId)
                .collection("addresses").add(address).await()
            fetchAddresses()
        }
    }

}