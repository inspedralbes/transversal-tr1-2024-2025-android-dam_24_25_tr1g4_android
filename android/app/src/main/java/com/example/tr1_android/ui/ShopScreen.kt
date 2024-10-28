package com.example.tr1_android.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.semantics.error
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.tr1_android.communication.DEV_URL
import com.example.tr1_android.data.ShopItem
import coil.request.CachePolicy
import com.example.tr1_android.R

@Composable
fun ShopScreen(
    storeViewModel: StoreViewModel = viewModel(),
    modifier: Modifier = Modifier,
    afegirACarro: (ShopItem) -> Unit = {},
    treureDelCarro: (ShopItem) -> Unit = {},
) {
    val storeUiState by storeViewModel.uiState.collectAsState()
    var imgBaseURL = "${DEV_URL}/assets"
    Box(
        modifier = modifier
//            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xffdddddd), Color(0xffdddddd))
                )
            ),
    ){
        LazyColumn (contentPadding = PaddingValues(top = 0.dp)) {
            items(storeUiState.trolley) { trolleyItem ->
                ShopItemCard(
                    shopItem = trolleyItem.item,
                    afegirACarro = afegirACarro,
                    treureDelCarro = treureDelCarro,
                    quantitat = trolleyItem.quantity,
                    imgBaseURL = imgBaseURL
                )
            }
        }
    }
}

@Composable
fun ShopItemCard(
    shopItem: ShopItem,
    afegirACarro: (ShopItem) -> Unit,
    treureDelCarro: (ShopItem) -> Unit,
    quantitat: Int,
    modifier: Modifier = Modifier,
    imgBaseURL: String = ""
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.SpaceBetween // Arrange items with space between
        ) {
            ShopItemPhotoCard(shopItem = shopItem, imgBaseURL = imgBaseURL)
            Spacer(modifier = Modifier.width(16.dp))
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Column {
                    Text(text = shopItem.nom)
                    Text(text = "Quantity: ${quantitat}")
                    Text(text = "Price: $${shopItem.preu}")
                    Text(text = "Stock: ${shopItem.estoc}")
                }
            }
            Column {
                Button(
                    onClick = { afegirACarro(shopItem) }
                ) {
                    Text("+")
                }
                Button(
                    onClick = { treureDelCarro(shopItem) }
                ) {
                    Text("-")
                }
            }
        }
    }
}

@Composable
fun ShopItemPhotoCard(shopItem: ShopItem, modifier: Modifier = Modifier, imgBaseURL: String) {

    val painter = // Enable disk caching
        rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current)
                .data(data = "${imgBaseURL}/${shopItem.imatge}")
                .apply {
                    diskCachePolicy(CachePolicy.ENABLED) // Use Coil's CachePolicy
                    crossfade(true)
                    placeholder(R.drawable.loading)
                    error(R.drawable.error)
                }
                .build()
        )

    Image(
        painter = painter,
        contentDescription = shopItem.nom,
        contentScale = ContentScale.Fit,
        modifier = modifier
            .width(125.dp)
    )
}

//@Preview(showBackground = true)
//@Composable
//fun ShopItemCardPreview() {
//    val exampleData: List<ShopItem> = listOf(
//        ShopItem(0, "Apple", "https://example.com/apple.jpg", 10, 1.99),
//        ShopItem(0, "Apple", "https://example.com/apple.jpg", 10, 1.99),
//        ShopItem(0, "Apple", "https://example.com/apple.jpg", 10, 1.99))
//
//    ShopItemCard(
//        shopItem = exampleData[0],
//        afegirACarro = {},
//        treureDelCarro = {},
//        quantitat = 1
//    )
//}

@Preview(showBackground = true)
@Composable
fun ShopScreenPreview() {
    val exampleData: List<ShopItem> = listOf(
        ShopItem(0, "Apple", "https://example.com/apple.jpg", 10, 1.99),
        ShopItem(0, "Apple", "https://example.com/apple.jpg", 10, 1.99),
        ShopItem(0, "Apple", "https://example.com/apple.jpg", 10, 1.99))

    ShopScreen(
    )


}