package com.example.tr1_android.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tr1_android.data.ShopItem

@Composable
fun ShopScreen(
    storeViewModel: StoreViewModel = viewModel(),
    modifier: Modifier = Modifier,
    comprarItem: (ShopItem) -> Unit = {}
) {
    val storeUiState by storeViewModel.uiState.collectAsState()

   /* var exampleData: List<ShopItem> = listOf(
        ShopItem(0, "Apple", "https://example.com/apple.jpg", 10, 1.99),
        ShopItem(1, "Pear", "https://example.com/apple.jpg", 10, 2.99),
        ShopItem(2, "Orange", "https://example.com/apple.jpg", 10, 3.99),
        ShopItem(3, "Banana", "https://example.com/apple.jpg", 10, 4.99),
        ShopItem(4, "Lime", "https://example.com/apple.jpg", 10, 5.99),
        ShopItem(5, "Mango", "https://example.com/apple.jpg", 10, 6.99),
    )*/
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xffdddddd), Color(0xffdddddd))
                )
            ),
    ){
        Column {
            storeUiState.shopItems.forEach {
                ShopItemCard(shopItem = it, comprarItem = comprarItem)
            }
        }
    }
}

@Composable
fun ShopItemCard(
    shopItem: ShopItem,
    comprarItem: (ShopItem) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween // Arrange items with space between
        ) {
//            ShopItemPhotoCard(shopItem = shopItem)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = shopItem.nom)
                Text(text = "Quantity: ${shopItem.estoc}")
                Text(text = "Price: $${shopItem.preu}")
            }
            Button(
                onClick = { comprarItem(shopItem) }
            ) {
                Text("Buy")
            }
        }
    }
}

@Composable
fun ShopItemPhotoCard(shopItem: ShopItem, modifier: Modifier = Modifier) {

    AsyncImage(
        model = ImageRequest.Builder(context = LocalContext.current)
            .data(shopItem.imatge)
//            .data("https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.applesfromny.com%2Fvarieties%2F&psig=AOvVaw2SSDNXCRWiQ3cRbSd11c3C&ust=1729750904050000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCNiY2bPuo4kDFQAAAAAdAAAAABAE")
            .build(),
        contentDescription = shopItem.nom,
        contentScale = ContentScale.Fit,
        modifier = modifier,
        onError = { error ->
            println("Error loading image: ${error}")
            println("trying to load: ${shopItem.imatge}")
        }
    )
}