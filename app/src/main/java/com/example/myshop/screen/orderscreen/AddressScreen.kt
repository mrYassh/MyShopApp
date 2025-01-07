package com.example.myshop.screen.orderscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myshop.components.MyShopTopAppBar
import com.example.myshop.data.Address
import com.example.myshop.navigation.MyShopScreens
import com.example.myshop.viewmodel.OrderViewModel

@Composable
fun AddressScreen(navController: NavController) {
    Scaffold(topBar = {
        MyShopTopAppBar(title = "Select Address", isMainscreen = false) {
            navController.popBackStack()
        }
    },
        bottomBar = { DeliverHere(navController = navController) }) {
        Column(modifier = Modifier.padding(it)) {
            AddressSummaryPaymentBar(aColor = Color.Blue.copy(0.6f), aTextColor = Color.White)
            AddressComponent()
        }
    }
}

@Composable
fun AddressSummaryPaymentBar(
    aColor: Color = Color.White,
    aTextColor: Color = Color.Blue.copy(0.6f),
    sColor: Color = Color.White,
    sTextColor: Color = Color.Blue.copy(0.6f),
    pColor: Color = Color.White,
    pTextColor: Color = Color.Blue.copy(0.6f),
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .padding(5.dp),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier.size(25.dp),
                    shape = CircleShape,
                    color = aColor,
                    border = BorderStroke(1.dp, aTextColor)
                ) {
                    Text(
                        text = "1",
                        color = aTextColor,
                        textAlign = TextAlign.Center
                    )
                }
                Text(text = "Address")
            }
            Divider(
                modifier = Modifier.width(50.dp),
            )
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier.size(25.dp),
                    shape = CircleShape,
                    color = sColor,
                    border = BorderStroke(1.dp, sTextColor)
                ) {
                    Text(
                        text = "2",
                        modifier = Modifier.padding(2.dp),
                        color = sTextColor,
                        textAlign = TextAlign.Center
                    )
                }
                Text(text = "Order Summary")
            }
            Divider(
                modifier = Modifier.width(50.dp),
            )
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier.size(25.dp),
                    shape = CircleShape,
                    color = pColor,
                    border = BorderStroke(1.dp, pTextColor)
                ) {
                    Text(
                        text = "3",
                        modifier = Modifier.padding(2.dp),
                        color = pTextColor,
                        textAlign = TextAlign.Center
                    )
                }
                Text(text = "Payment")
            }
        }
    }
}

@Composable
fun AddAddressScreen(onAddressAdded: () -> Unit) {
    val viewModel: OrderViewModel = viewModel()
    val name = remember { mutableStateOf("") }
    val street = remember { mutableStateOf("") }
    val city = remember { mutableStateOf("") }
    val state = remember { mutableStateOf("") }
    val postalCode = remember { mutableStateOf("") }
    val phoneNumber = remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = street.value,
            onValueChange = { street.value = it },
            label = { Text("Street") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = city.value,
            onValueChange = { city.value = it },
            label = { Text("City") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = state.value,
            onValueChange = { state.value = it },
            label = { Text("State") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = postalCode.value,
            onValueChange = { postalCode.value = it },
            label = { Text("Postal Code") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = phoneNumber.value,
            onValueChange = { phoneNumber.value = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            viewModel.addAddress(
                Address(
                    name = name.value,
                    street = street.value,
                    city = city.value,
                    state = state.value,
                    postalCode = postalCode.value,
                    phoneNumber = phoneNumber.value
                )
            )
            onAddressAdded()
        }) {
            Text("Add Address")
        }
    }
}

@Composable
fun AddressComponent() {
    val viewModel: OrderViewModel = viewModel()
    val addresses by viewModel.addresses.collectAsState()
    val showAddAddressForm = remember { mutableStateOf(false) }


    if (showAddAddressForm.value) {
        AddAddressScreen(onAddressAdded = {
            showAddAddressForm.value = false
        })
    } else {
        Column(modifier = Modifier.padding(8.dp)) {
            AddressSection(
                addresses,
                onAddressSelect = { addressId ->
                    viewModel.selectAddress(addressId)
                },
                onAddNewAddress = {
                    showAddAddressForm.value = true
                }
            )
        }
    }

}

@Composable
fun AddressSection(
    addresses: List<Address>,
    onAddressSelect: (String) -> Unit,
    onAddNewAddress: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = "Deliver to:", style = MaterialTheme.typography.headlineLarge)
        LazyColumn {
            items(addresses) { address ->
                AddressItem(address, address.selected, onSelect = {
                    onAddressSelect(address.id)
                })
            }
        }
        Button(onClick = onAddNewAddress) {
            Text("Add a new address")
        }
    }
}

@Composable
fun DeliverHere(navController: NavController) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable { navController.navigate(MyShopScreens.OrderSummaryScreen.name) },
        color = Color.Yellow
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Deliver Here", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
