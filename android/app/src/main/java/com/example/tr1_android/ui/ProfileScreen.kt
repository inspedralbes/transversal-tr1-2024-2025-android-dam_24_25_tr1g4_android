package com.example.tr1_android.ui

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.isEmpty
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tr1_android.data.Comanda
import com.example.tr1_android.data.UserUiState


@Composable
fun ProfileScreen (
    viewModel: StoreViewModel = viewModel(),
    modifier: Modifier = Modifier
) {

    val uiState by viewModel.uiState.collectAsState()

    val userUiState by viewModel.userUiState.collectAsState()

    when(userUiState) {
        is UserUiState.Loading -> {
            Text(text = "Loading")
        }
        is UserUiState.Success -> {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                ) {
                    Text(text = "Name: ${uiState.userInfo.nom}")
                    Text(text = "Email: ${uiState.userInfo.email}")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Orders:")
//        if (uiState.userInfo.orders.isEmpty()) {
                    if (uiState.comandes.isEmpty()) {
                        Text(text = "No orders yet.")
                    } else {
                        LazyColumn {
                            items(uiState.comandes) { comanda ->
                                OrderCard(comanda = comanda)
                            }
                        }
                    }
                }
            }
        is UserUiState.Error -> {
            Text(text = "Error")
        }
    }
}

@Composable
fun OrderCard(comanda: Comanda) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Order ID: ${comanda.id}")
            Text(text = "Items: ${comanda.productes}")
            Text(text = "Preu: ${comanda.preu_total}")
        }
    }
}


