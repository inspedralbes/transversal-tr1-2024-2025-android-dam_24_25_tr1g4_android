package com.example.tr1_android.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class StoreUiState (
    var shopItems: List<ShopItem> = emptyList(),
    var isLoading: Boolean = true,
    var token: String = "",
    var trolley: List<TrolleyItem> = emptyList(),
    val totalPrice: Double = 0.0,
    var userInfo: User = User(),
    var showDialog: Boolean = false,
    var comandes: List<Comanda> = emptyList(),
    var comandaActual: Comanda = Comanda(0,0,1, emptyList(), 0.0)
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
    var id: Int = 0,
    var nom: String = "",
    var email: String = "",
    var token: String = "",
)

sealed interface UserUiState {
    data class Success(val user: User) : UserUiState
    object Error : UserUiState
    object Loading : UserUiState
}

sealed interface BuyUiState {
    data class Success(val buy: CompraResponse) : BuyUiState
    object Error : BuyUiState
    object Loading : BuyUiState
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

// Register

data class RegisterRequest(
    var name: String,
    var correo: String,
    var password: String,
    var pagament: String

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
)

@Serializable
data class ComandaWithString(

    @SerialName(value = "id")
    var id: Int,

    @SerialName(value = "iduser")
    var iduser: Int,

    @SerialName(value = "estat")
    var estat: Int,

    @SerialName(value = "productes")
    var productes: String,

    @SerialName(value = "preu_total")
    var preu_total: Double
)

@Serializable
data class Comanda(

    @SerialName(value = "id")
    var id: Int,

    @SerialName(value = "iduser")
    var iduser: Int,

    @SerialName(value = "estat")
    var estat: Int,

    @SerialName(value = "productes")
    var productes: List<BuyItem>,


    @SerialName(value = "preu_total")
    var preu_total: Double
)