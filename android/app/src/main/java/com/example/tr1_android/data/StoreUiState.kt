package com.example.tr1_android.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class StoreUiState (
    var shopItems: List<ShopItem> = emptyList(),
    var isLoading: Boolean = true,
    var token: String = "",
    var trolley: List<TrolleyItem> = emptyList(),
    val totalPrice: Double = 0.0,
    var userInfo: User = User()
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

@Serializable
data class User(
    var id: Int = 1,
    var nom: String = "",
    var email: String = "",
    var token: String = "",
)

sealed interface UserUiState {
    data class Success(val user: User) : UserUiState
    object Error : UserUiState
    object Loading : UserUiState
}

// Login

data class LoginRequest(
    var correu: String,
    var contrasenya: String
)

@Serializable
data class LoginResponse(
    var valid: Boolean,
    var usuari: User
)

// Compra

data class BuyItem(
    var idProducte: Int,
    var quantitat: Int
)

data class CompraRequest(
    var idUsuari: Int,
    var productes: List<BuyItem>,
    var preuTotal: Double
)

@Serializable
data class CompraResponse(
    var valid: Boolean,
    var comanda: Comanda
)

@Serializable
data class Comanda(
    var id: Int,
    var estatus: String,
    var productes: List<BuyItem>,
    var total: Double
)