package com.example.tr1_android.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.tr1_android.data.BuyItem
import com.example.tr1_android.data.BuyUiState

@Composable
fun OrderScreen(
    modifier: Modifier = Modifier,
    viewModel: StoreViewModel
) {
    val buyUiState by viewModel.buyUiState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    when (buyUiState) {
        is BuyUiState.Loading -> {
            Text(text = "Loading")
        }
        is BuyUiState.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                Text("Comanda Details")
                Spacer(modifier = Modifier.height(16.dp))

                // Display Comanda properties
                Text("ID: ${uiState.comandaActual.id}")
                Text("User ID: ${uiState.comandaActual.iduser}")
                Text("Status: ${uiState.comandaActual.estatus}")
                Text("Total Price: ${uiState.comandaActual.preu_total}")

                Spacer(modifier = Modifier.height(16.dp))

                // Display Productes (BuyItems)
                Text("Products:")
                LazyColumn {
                    items(uiState.comandaActual.productes) { buyItem ->
                        BuyItemCard(buyItem, viewModel)
                    }
                }
            }
        }
        is BuyUiState.Error -> {
            Text(text = "Error")

        }
    }

}

@Composable
fun BuyItemCard(buyItem: BuyItem, viewModel: StoreViewModel) {

    val uiState by viewModel.uiState.collectAsState()

    // Assuming BuyItem has properties like name, quantity, price, etc.
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = uiState.shopItems[buyItem.idProducte].nom)
            Text(text = "Quantity: ${buyItem.quantitat}")
            Text(text = "Price: ${uiState.shopItems[buyItem.idProducte].preu}")
            // Add more BuyItem properties as needed
        }
    }
}