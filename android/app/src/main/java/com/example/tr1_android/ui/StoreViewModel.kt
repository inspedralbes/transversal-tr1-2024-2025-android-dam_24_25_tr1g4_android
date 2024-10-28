package com.example.tr1_android.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tr1_android.communication.StoreApi
import com.example.tr1_android.data.BuyItem
import com.example.tr1_android.data.CompraRequest
import com.example.tr1_android.data.LoginRequest
import com.example.tr1_android.data.ShopItem
import com.example.tr1_android.data.StoreUiState
import com.example.tr1_android.data.TrolleyItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.text.toDouble

class StoreViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(StoreUiState())
    val uiState: StateFlow<StoreUiState> = _uiState.asStateFlow()

    fun addItemToTrolley(shopItem: ShopItem) {
        _uiState.update { currentState ->
            val existingItemIndex = currentState.trolley.indexOfFirst { it.item.id == shopItem.id }

            if (existingItemIndex != -1 && currentState.trolley[existingItemIndex].quantity < shopItem.estoc) {
                var nouPreu = currentState.totalPrice + shopItem.preu
                var nouPreuRounded = BigDecimal(nouPreu).setScale(2, RoundingMode.HALF_UP).toDouble()

                println(nouPreu)

                if (existingItemIndex != -1) {
                    // Item already exists in the trolley, increment quantity
                    val updatedTrolley = currentState.trolley.toMutableList()
                    updatedTrolley[existingItemIndex] = updatedTrolley[existingItemIndex].copy(quantity = updatedTrolley[existingItemIndex].quantity + 1)
                    currentState.copy(
                        trolley = updatedTrolley,
                        totalPrice = nouPreuRounded
                    )
                } else {
                    // Item not in trolley, add it with quantity 1
                    currentState.copy(
                        trolley = currentState.trolley + TrolleyItem(shopItem, quantity = 1),
                        totalPrice = nouPreuRounded
                    )
                }
            } else {
                println("Insuficient stock")
                currentState.copy(
                    trolley = currentState.trolley,
                    totalPrice = currentState.totalPrice
                )
            }


        }
    }

    fun removeItemFromTrolley(shopItem: ShopItem) {
        _uiState.update { currentState ->
            var updatedTrolley: MutableList<TrolleyItem> = currentState.trolley.toMutableList()
            var nouPreuRounded: Double = currentState.totalPrice
            println("removing item from trolley")
            val existingItemIndex = currentState.trolley.indexOfFirst { it.item.id == shopItem.id }

            if (existingItemIndex != -1 && currentState.trolley[existingItemIndex].quantity > 0) {
                println("found item in trolley")

                updatedTrolley[existingItemIndex] = updatedTrolley[existingItemIndex].copy(quantity = updatedTrolley[existingItemIndex].quantity - 1)

                var nouPreu = currentState.totalPrice - shopItem.preu
                nouPreuRounded = BigDecimal(nouPreu).setScale(2, RoundingMode.HALF_UP).toDouble()

                println("removed item from trolley, new price: $nouPreuRounded")
            } else {
                println("Didn't find item in trolley or quantity is already 0")
                // Item not in trolley or quantity is already 0, no action needed
            }

            currentState.copy(
                trolley = updatedTrolley,
                totalPrice = nouPreuRounded
            )
        }
    }

    fun login(loginRequest: LoginRequest): Boolean {
        var valid: Boolean = false
        viewModelScope.launch {
            val response = StoreApi.retrofitService.login(loginRequest)

            println(response)

            _uiState.update { currentState ->
                currentState.copy(
                    userInfo = response.usuari
                )}
            valid = response.valid
            }

        return valid

    }


    fun postCompra() {
        viewModelScope.launch {
                val preuTotal = _uiState.value.totalPrice
                val idUser = _uiState.value.userInfo.id
                val productes: MutableList<BuyItem> = mutableListOf()

                _uiState.value.trolley.map { trolleyItem ->
                    if (trolleyItem.quantity > 0)
                        productes.add(BuyItem(trolleyItem.item.id, trolleyItem.quantity))
                }

                println(idUser)
                println(productes)
                println(preuTotal)

                StoreApi.retrofitService.postComanda(CompraRequest(idUser,productes,preuTotal))




        }
    }

    fun clearTrolley() {
        _uiState.update { currentState ->

            val resetTrolley = currentState.trolley.map { trolleyItem ->
                TrolleyItem(trolleyItem.item, 0)
            }
            currentState.copy(
                trolley = resetTrolley,
                totalPrice = 0.0
                        )
        }
    }


    init {

        viewModelScope.launch {
            println("calling api")
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true
                )
            }

            val shopItems = StoreApi.retrofitService.getProductes()

            val trolleyItems = shopItems.map { shopItem ->
                TrolleyItem(shopItem, 0)
            }

            println(shopItems)


            _uiState.update { currentState ->
            currentState.copy(
                shopItems = shopItems,
                trolley = trolleyItems,
                isLoading = false,
            )}
        }

    }

}