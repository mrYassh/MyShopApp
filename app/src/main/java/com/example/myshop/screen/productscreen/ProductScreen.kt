package com.example.myshop.screen.productscreen

import CartViewModel
import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myshop.components.BuyAndCartButton
import com.example.myshop.components.ImageSlider
import com.example.myshop.components.MyShopTopAppBar
import com.example.myshop.components.ReviewAndRating
import com.example.myshop.data.DataOrException
import com.example.myshop.data.MyProduct
import com.example.myshop.model.ProductX
import com.example.myshop.model.Review
import com.example.myshop.navigation.MyShopScreens
import com.example.myshop.viewmodel.ProductViewModel

@Composable
fun ProductScreen(
    navController: NavController,
    itemId: String,
    viewModel: ProductViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = viewModel(),
) {
    Log.d("productscreen", "ProductScreen: $itemId")
    val productInfo = produceState<DataOrException<ProductX, Boolean, Exception>>(
        initialValue = DataOrException(loading = true)
    ) {
        value = viewModel.getProductById(itemId.toInt())

    }.value
    if (productInfo.loading == true) {
        Log.d("home", "HomeScreen: ${productInfo.loading.toString()}")
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }
    } else if (productInfo.data != null) {
        val item = productInfo.data!!
        var state: ScrollState = rememberScrollState()
        Log.d("productscreen", "ProductScreen: $item")
        Scaffold(
            topBar = {
                MyShopTopAppBar(title = "MyShop App",
                    isMainscreen = false,
                    icon1 = Icons.Default.Search,
                    icon2 = Icons.Default.ShoppingCart,
                    onIcon1 = { navController.navigate(MyShopScreens.SearchScreen.name) },
                    onIcon2 = { navController.navigate(MyShopScreens.CartScreen.name) }) {
                    navController.popBackStack()
                }
            },
            bottomBar = {
                BuyAndCartButton(onBag = {
                    cartViewModel.addProductToCart(MyProduct(itemId, item.title, item.price))
                }) {
                    cartViewModel.removeAllItemsFromCart()
                    cartViewModel.addProductToDirect(MyProduct(itemId, item.title, item.price))
                    navController.navigate(MyShopScreens.AddressScreen.name)
                }
            },

            ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding(), bottom = 65.dp)
            ) {
                Column(

                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(state = state)
                ) {
                    if (item?.brand != null) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 5.dp),
                            shape = RectangleShape
                        ) {
                            Text(
                                text = item.brand,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 5.dp, horizontal = 5.dp),
                                color = Color.Black
                            )
                        }
                    }



                    if (item?.images != null) {
                        ImageSlider(images = item.images)
                    }
                    if (item?.title != null) {
                        /*  Text(
                              text = item.title,
                              modifier = Modifier.padding(vertical = 2.dp),
                              fontSize = MaterialTheme.typography.labelMedium.fontSize,
                              fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
                              color = Color.Gray,
                              lineHeight = 15.sp
                          )
                          */
                        Text(text = buildAnnotatedString {
                            if (item.brand != null) {
                                withStyle(
                                    style = SpanStyle(
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append(
                                        item.brand
                                    )
                                }
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 20.sp,
                                    color = Color.Black
                                )
                            ) { append(" ${item.title}") }
                        }, modifier = Modifier.padding(bottom = 10.dp))
                    }
                    Row {
                        ReviewAndRating(
                            review = item.reviews.size.toString(),
                            rating = item.rating.toString()
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        if (item.availabilityStatus == "In Stock") {

                            Text(
                                text = "In Stock",
                                color = Color.Blue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )

                        } else {
                            Text(
                                text = "Only ${item.stock} left ",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.Red
                            )
                        }
                    }
                    Text(text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 25.sp
                            )
                        ) {
                            append(
                                "$${item.price}"
                            )
                        }
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF964B00),
                                fontSize = 22.sp
                            )
                        ) { append(" ${item.discountPercentage}%off") }
                    }, modifier = Modifier.padding(vertical = 15.dp))
                    Text(
                        text = "Product Description",
                        color = Color.Black.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        text = item.description,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 5.dp), textAlign = TextAlign.Justify
                    )
                    Text(
                        text = "Return Policy",
                        color = Color.Black.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        text = item.returnPolicy,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 5.dp), textAlign = TextAlign.Justify
                    )
                    Text(
                        text = "Warranty Information",
                        color = Color.Black.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        text = item.warrantyInformation,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 5.dp), textAlign = TextAlign.Justify
                    )
                    Text(
                        text = "Customer Reviews",
                        color = Color.Black.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    for (review in item.reviews) {
                        CustomerReview(review)
                    }
                }
            }
        }
    }
}

@Composable
fun CustomerReview(review: Review) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp), shape = RectangleShape
    ) {
        Column {


            Row {
                for (i in 1..review.rating) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "rating",
                        tint = Color.Yellow
                    )
                }
                Text(
                    text = " By ${review.reviewerName}",

                    fontWeight = FontWeight.Bold
                )
            }
            Text(text = review.comment)
        }
    }
}