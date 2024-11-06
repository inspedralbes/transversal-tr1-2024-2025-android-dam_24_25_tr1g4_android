package com.example.tr1_android.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tr1_android.data.ShopItem

@Composable
fun PaymentScreen(
    modifier: Modifier = Modifier,
    viewModel: StoreViewModel,
    onPaymentSuccess: () -> Unit = {}
) {

    val shopUiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(shopUiState.trolley.filter { it.quantity > 0 }) { trolleyItem ->
                ShopItemRow(trolleyItem.item, trolleyItem.quantity)
            }
        }

        Text(text = "Preu total: ${shopUiState.totalPrice}€",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp))

        Button(onClick = { onPaymentSuccess() }) {
            Text("Confirmar compra")
        }
    }
}

@Composable
fun ShopItemRow(
    shopItem: ShopItem,
    quantity: Int
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Text(text = shopItem.nom, modifier = Modifier.weight(1f))
        Text(text = "${quantity} x ${shopItem.preu}€")
    }
}