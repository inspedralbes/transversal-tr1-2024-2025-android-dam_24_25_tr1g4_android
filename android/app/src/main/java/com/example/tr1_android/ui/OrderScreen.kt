package com.example.tr1_android.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.tr1_android.data.BuyUiState

@Composable
fun OrderScreen(
    modifier: Modifier = Modifier,
    viewModel: StoreViewModel
) {
    val buyUiState by viewModel.buyUiState.collectAsState()

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

            }
        }
        is BuyUiState.Error -> {
            Text(text = "Error")

        }
    }

}