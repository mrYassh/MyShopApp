package com.example.myshop.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myshop.data.DataOrException
import com.example.myshop.model.ProductX
import com.example.myshop.navigation.MyShopScreens
import com.example.myshop.viewmodel.ProductViewModel

@Composable
fun MyShopProductCard(item: ProductX?, navController: NavController) {
    Card(
        modifier = Modifier
            .width(250.dp)
            .padding(10.dp)
            .clickable {
                if (item != null) {
                    navController.navigate(MyShopScreens.ProductScreen.name + "/${item.id}")
                    Log.d("cardclickk", "MyShopProductCard: ${item.id}")
                }
            }
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            if (item != null) {
                AsyncImage(
                    model = item.images[0],
                    contentDescription = "product image",
                    modifier = Modifier
                        .height(250.dp)
                        .width(240.dp)
                )
                if (item.brand != null) {
                    Text(
                        text = item.brand,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontWeight = MaterialTheme.typography.titleMedium.fontWeight
                    )
                }
                if (item.title != null) {
                    Text(
                        text = item.title,
                        modifier = Modifier.padding(vertical = 2.dp),
                        fontSize = MaterialTheme.typography.labelMedium.fontSize,
                        fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
                        color = Color.Gray,
                        lineHeight = 15.sp
                    )
                }
                ReviewAndRating(
                    review = item.reviews.size.toString(),
                    rating = item.rating.toString()
                )
                Text(text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.W500,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize
                        )
                    ) { append("$${item.price}") }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF964B00)
                        )
                    ) { append("  ${item.discountPercentage}%off") }
                })
            } else Box {}
        }
    }
}

@Composable
fun ReviewAndRating(review: String, rating: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RectangleShape,
        color = Color.Green.copy(alpha = 0.6f)
    ) {
        Text(
            text = "$rating ★ | ${review}k",
            modifier = Modifier.padding(5.dp),
            fontSize = 15.sp,
            color = Color.White
        )
    }
}


@Composable
fun ProductRowCard(navController: NavController, item: ProductX?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(10.dp)
            .clickable {
                if (item != null) {
                    navController.navigate(MyShopScreens.ProductScreen.name + "/${item.id}")
                }
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            if (item != null) {
                AsyncImage(
                    model = item.images[0],
                    contentDescription = "product",
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(200.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Column {
                if (item!!.brand != null) {
                    Text(
                        text = item.brand,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontWeight = MaterialTheme.typography.titleMedium.fontWeight
                    )
                }
                if (item.title != null) {
                    Text(
                        text = item.title,
                        modifier = Modifier.padding(vertical = 2.dp),
                        fontSize = MaterialTheme.typography.labelMedium.fontSize,
                        fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
                        color = Color.Gray,
                        lineHeight = 15.sp
                    )
                }
                ReviewAndRating(
                    review = item.reviews.size.toString(),
                    rating = item.rating.toString()
                )
                Text(text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.W500,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize
                        )
                    ) { append("$${item.price}") }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF964B00)
                        )
                    ) { append("  ${item.discountPercentage}%off") }
                })
            }
        }
    }
}


@Composable
fun MyShopProductByIdCard(
    itemId: Int,
    quantity: Int = 0,
    navController: NavController,
    viewModel: ProductViewModel = hiltViewModel(),
) {
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
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(10.dp)
                .clickable {
                    if (item != null) {
                        navController.navigate(MyShopScreens.ProductScreen.name + "/${item.id}")
                    }
                }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                if (item != null) {
                    AsyncImage(
                        model = item.images[0],
                        contentDescription = "product",
                        modifier = Modifier
                            .width(200.dp)
                            .fillMaxHeight(),
                        contentScale = ContentScale.Fit
                    )
                }
                Column {
                    if (item!!.brand != null) {
                        Text(
                            text = item.brand,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            fontWeight = MaterialTheme.typography.titleMedium.fontWeight
                        )
                    }
                    if (item.title != null) {
                        Text(
                            text = item.title,
                            modifier = Modifier.padding(vertical = 2.dp),
                            fontSize = MaterialTheme.typography.labelMedium.fontSize,
                            fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
                            color = Color.Gray,
                            lineHeight = 15.sp
                        )
                    }

                    Text(text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.W500,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize
                            )
                        ) { append("$${item.price}") }
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF964B00)
                            )
                        ) { append("  ${item.discountPercentage}%off") }
                    }

                    )
                    if (quantity != 0) {
                        Text(
                            text = "Quantity : $quantity",
                            modifier = Modifier.padding(top = 10.dp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}