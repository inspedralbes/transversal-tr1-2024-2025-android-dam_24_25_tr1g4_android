package com.example.tr1_android.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class StoreUiState (
    var shopItems: List<ShopItem> = emptyList(),
    var isLoading: Boolean = true,
    var token: String = "",
    var trolley: List<TrolleyItem> = emptyList(),
    val totalPrice: Double = 0.0
)

@Serializable
data class ShopItem(

    @SerialName(value = "id")
    var id: Int,

    @SerialName (value = "nom")
    var nom: String,

    @SerialName (value = "imatge")
    var imatge: String,

    @SerialName (value = "estoc")
    var estoc: Int,

    @SerialName (value = "preu")
    var preu: Double,
)

data class TrolleyItem(
    val item: ShopItem,
    val quantity: Int
)