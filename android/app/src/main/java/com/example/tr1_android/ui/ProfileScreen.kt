package com.example.tr1_android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.isEmpty
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tr1_android.data.BuyItem
import com.example.tr1_android.data.Comanda
import com.example.tr1_android.data.ShopItem
import com.example.tr1_android.data.UserUiState


@Composable
fun ProfileScreen (
    viewModel: StoreViewModel = viewModel(),
    modifier: Modifier = Modifier,
    orderDetails: (Comanda) -> Unit = {}
) {

    val uiState by viewModel.uiState.collectAsState()

    val userUiState by viewModel.userUiState.collectAsState()

    when(userUiState) {
        is UserUiState.Loading -> {
            Text(text = "Carregant...")
        }
        is UserUiState.Success -> {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                ) {
                    Text(text = "Nom: ${uiState.userInfo.nom}")
                    Text(text = "Correu: ${uiState.userInfo.email}")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Comandes:")
//        if (uiState.userInfo.orders.isEmpty()) {
                    if (uiState.comandes.isEmpty()) {
                        Text(text = "No orders yet.")
                    } else {
                        LazyColumn {
                            items(uiState.comandes) { comanda ->
                                Button(
                                    onClick = { orderDetails(comanda) },
                                    modifier = Modifier
                                        .background(Color.Transparent)
                                        .padding(0.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Transparent,
                                        contentColor = Color.Black // Or your desired text/icon color
                                    ),
                                    shape = RectangleShape,
                                    contentPadding = PaddingValues(0.dp),

//                                    elevation = ButtonDefaults.elevation(
//                                        defaultElevation = 0.dp,
//                                        pressedElevation = 0.dp,
//                                        disabledElevation = 0.dp
//                                    )
                                ) {
                                    OrderCard(comanda = comanda, viewModel, orderDetails)
                                }
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
fun OrderCard(comanda: Comanda, viewModel: StoreViewModel, orderDetails: (Comanda) -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "ID de la comanda: ${comanda.id}")

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier) {
                    Column(modifier = Modifier) {
                        comanda.productes.forEach { buyItem ->
                            BuyItemText(buyItem, viewModel)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Preu: ${comanda.preu_total}€")
            }
            Column(
                modifier = Modifier,

                ) {
                val estat = if (comanda.estat==0) "Rebuda" else if (comanda.estat==1) "En preparació" else if (comanda.estat==2) "Preparada" else "Recollida"
                Text(text = "Estat")
                Text(text = estat)
            }
        }
    }
}

@Composable
fun BuyItemText(buyItem: BuyItem, viewModel: StoreViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    val thisShopItem: ShopItem = uiState.shopItems.find { it.id == buyItem.idProducte }!!

    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "${thisShopItem.nom} - ${buyItem.quantitat}")
    }

}



