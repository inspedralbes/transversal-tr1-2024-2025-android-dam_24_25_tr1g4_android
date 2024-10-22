package com.example.tr1_android.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.tr1_android.data.ShopItem

@Composable
fun ShopScreen(
    modifier: Modifier = Modifier
) {
    var exampleData: List<ShopItem> = listOf(
        ShopItem(0, "Apple", "https://example.com/apple.jpg", 10, 1.99),
        ShopItem(1, "Pear", "https://example.com/apple.jpg", 10, 2.99),
        ShopItem(2, "Orange", "https://example.com/apple.jpg", 10, 3.99),
        ShopItem(3, "Banana", "https://example.com/apple.jpg", 10, 4.99),
        ShopItem(4, "Lime", "https://example.com/apple.jpg", 10, 5.99),
        ShopItem(5, "Mango", "https://example.com/apple.jpg", 10, 6.99),
    )
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
            exampleData.forEach {
                ShopItemCard(shopItem = it)
            }
        }
    }
}

@Composable
fun ShopItemCard(shopItem: ShopItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberImagePainter(data = shopItem.imatge),
                contentDescription = shopItem.nom,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = shopItem.nom)
                Text(text = "Quantity: ${shopItem.estoc}")
                Text(text = "Price: $${shopItem.preu}")
            }
        }
    }
}