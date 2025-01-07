import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshop.data.CartItem
import com.example.myshop.data.MyProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CartViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> get() = _cartItems
    private val _directItem = MutableLiveData<List<CartItem>?>(emptyList())
    val directItem: LiveData<List<CartItem>?> get() = _directItem
    private val _totalAmount = MutableStateFlow(0.0)
    val totalAmount: StateFlow<Double> get() = _totalAmount
    private val _directAmount = MutableStateFlow(0.0)
    val directAmount: StateFlow<Double> get() = _directAmount
    private val auth = FirebaseAuth.getInstance()

    init {
        fetchCartItems()
        fetchdirectItem()
    }

    private fun fetchCartItems() {
        firestore.collection("users").document(auth.uid.toString()).collection("cart")
            .document("carts")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    println("Error fetching cart items: ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    viewModelScope.launch {
                        val items = snapshot.data?.map { (productId, quantity) ->
                            val price = getProductPriceById(productId)
                            CartItem(productId, (quantity as Long).toInt(), price)
                        } ?: emptyList()
                        _cartItems.value = items
                        calculateTotalAmount()
                        println("Cart items fetched: $items")
                    }
                } else {
                    println("No cart items found")
                }
            }
    }

    private fun fetchdirectItem() {
        firestore.collection("users").document(auth.uid.toString()).collection("cart")
            .document("directItem")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    println("Error fetching direct items: ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    viewModelScope.launch {
                        val items = snapshot.data?.map { (productId, quantity) ->
                            val price = getProductPriceById(productId)
                            CartItem(productId, (quantity as Long).toInt(), price)
                        } ?: emptyList()
                        _directItem.value = items
                        calculateTotalAmount()
                        println("Direct items fetched: $items")
                    }
                } else {
                    println("No direct items found")
                }
            }
    }

    private suspend fun getProductPriceById(productId: String): Double {
        val productSnapshot =
            firestore.collection("products").document(productId).get().await()
        return if (productSnapshot.exists()) {
            productSnapshot.getDouble("price") ?: 0.0
        } else {
            0.0
        }
    }

    fun addProductToCart(product: MyProduct) {
        viewModelScope.launch {
            val existingItem = _cartItems.value.find { it.productId == product.id }
            val updatedList = if (existingItem != null) {
                _cartItems.value.map {
                    if (it.productId == product.id) it.copy(quantity = it.quantity + 1) else it
                }
            } else {
                _cartItems.value + CartItem(product.id, 1, product.price)
            }
            _cartItems.value = updatedList
            updateCartInFirestore(updatedList)
            addProductAndPrice(product)
            calculateTotalAmount()
        }
    }

    fun addProductToDirect(product: MyProduct) {
        viewModelScope.launch {
            removeAllItemsFromCart()
            _directItem.value = emptyList()
            val existingItem = _directItem.value!!.find { it.productId == product.id }
            val updatedList = if (existingItem != null) {
                _directItem.value?.map {
                    if (it.productId == product.id) it.copy(quantity = it.quantity + 1) else it
                }
            } else {
                _directItem.value?.plus(CartItem(product.id, 1, product.price))
            }
            _directItem.value = updatedList
            if (updatedList != null) {
                updateDirectInFirestore(updatedList)
            }
            addProductAndPrice(product)
        }
    }

    fun increaseQuantity(productId: String) {
        viewModelScope.launch {
            val updatedList = _cartItems.value.map {
                if (it.productId == productId) it.copy(quantity = it.quantity + 1) else it
            }
            _cartItems.value = updatedList
            updateCartInFirestore(updatedList)
            calculateTotalAmount()
        }
    }

    fun increaseDirectItemQuantity(productId: String) {
        viewModelScope.launch {
            val updatedList = _directItem.value?.map {
                if (it.productId == productId) it.copy(quantity = it.quantity + 1) else it
            }
            _directItem.value = updatedList
            if (updatedList != null) {
                updateDirectInFirestore(updatedList)
            }
        }
    }

    fun decreaseQuantity(productId: String) {
        viewModelScope.launch {
            val updatedList = _cartItems.value.mapNotNull {
                if (it.productId == productId) {
                    if (it.quantity > 1) it.copy(quantity = it.quantity - 1) else null
                } else {
                    it
                }
            }
            _cartItems.value = updatedList
            removeItemInCartInFirestore(updatedList)
            calculateTotalAmount()
        }
    }

    fun decreaseDirectItemQuantity(productId: String) {
        viewModelScope.launch {
            val updatedList = _directItem.value?.mapNotNull {
                if (it.productId == productId) {
                    if (it.quantity > 1) it.copy(quantity = it.quantity - 1) else it.copy(quantity = 1)
                } else {
                    it
                }
            }
            _directItem.value = updatedList
            if (updatedList != null) {
                updateDirectInFirestore(updatedList)
            }

        }
    }

    fun removeItemFromCart(productId: String) {
        viewModelScope.launch {
            val updatedList = _cartItems.value.filterNot { it.productId == productId }
            _cartItems.value = updatedList
            removeItemInCartInFirestore(updatedList)
            calculateTotalAmount()
        }
    }

    private fun removeItemInCartInFirestore(cartItems: List<CartItem>) {
        val cartMap = cartItems.associate { it.productId to it.quantity }
        println("Updating Firestore with cart map: $cartMap")
        firestore.collection("users").document(auth.uid.toString()).collection("cart")
            .document("carts")
            .set(cartMap)
            .addOnSuccessListener {
                println("Cart updated successfully in Firestore")
            }
            .addOnFailureListener { error ->
                println("Error updating cart in Firestore: ${error.message}")
            }
    }

    private fun calculateTotalAmount() {
        _totalAmount.value = _cartItems.value.sumOf { it.quantity * it.price }
    }


    private fun updateCartInFirestore(cartItems: List<CartItem>) {
        val cartMap = cartItems.associate { it.productId to it.quantity }
        println("Updating Firestore with cart map: $cartMap")
        firestore.collection("users").document(auth.uid.toString()).collection("cart")
            .document("carts")
            .set(cartMap, SetOptions.merge())
            .addOnSuccessListener {
                println("Cart updated successfully in Firestore")
            }
            .addOnFailureListener { error ->
                println("Error updating cart in Firestore: ${error.message}")
            }
    }

    private fun updateDirectInFirestore(directItems: List<CartItem>) {
        val directMap = directItems.associate { it.productId to it.quantity }
        println("Updating Firestore with cart map: $directMap")
        firestore.collection("users").document(auth.uid.toString()).collection("cart")
            .document("directItem")
            .set(directMap)
            .addOnSuccessListener {
                println("Direct updated successfully in Firestore")
            }
            .addOnFailureListener { error ->
                println("Error updating Direct in Firestore: ${error.message}")
            }
    }


    private fun addProductAndPrice(product: MyProduct) {
        val productMap = product.mapTo()
        firestore.collection("products").document(product.id).set(productMap, SetOptions.merge())
            .addOnSuccessListener {
                println("product updated successfully in Firestore")
            }
            .addOnFailureListener { error ->
                println("Error adding product in Firestore: ${error.message}")
            }
    }

    fun removeAllItemsFromCart() {
        val cartRef = firestore.collection("users").document(auth.uid.toString()).collection("cart")
            .document("directItem")
        _directItem.value = emptyList()
        cartRef.delete()
        updateDirectInFirestore(_directItem.value!!)
        fetchdirectItem()
    }

    fun removeCart() {
        viewModelScope.launch {
            val cartRef =
                firestore.collection("users").document(auth.uid.toString()).collection("cart")
                    .document("carts")
            _cartItems.value = emptyList()
            cartRef.delete()
            _totalAmount.value = 0.0
            updateCartInFirestore(_cartItems.value)
            calculateTotalAmount()
        }
    }

}
//firestore.collection("users").document(auth.uid.toString()).collection("cart").document("carts")