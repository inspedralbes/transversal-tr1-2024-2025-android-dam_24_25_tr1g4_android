package com.example.tr1_android.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tr1_android.communication.StoreApi
import com.example.tr1_android.data.ShopItem
import com.example.tr1_android.data.StoreUiState
import com.example.tr1_android.data.TrolleyItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StoreViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(StoreUiState())
    val uiState: StateFlow<StoreUiState> = _uiState.asStateFlow()

    fun addItemToTrolley(shopItem: ShopItem) {
        _uiState.update { currentState ->
            val existingItemIndex = currentState.trolley.indexOfFirst { it.item.id == shopItem.id }

            var nouPreu = currentState.totalPrice + shopItem.preu

            println(nouPreu)

            if (existingItemIndex != -1) {
                // Item already exists in the trolley, increment quantity
                val updatedTrolley = currentState.trolley.toMutableList()
                updatedTrolley[existingItemIndex] = updatedTrolley[existingItemIndex].copy(quantity = updatedTrolley[existingItemIndex].quantity + 1)
                currentState.copy(
                    trolley = updatedTrolley,
                    totalPrice = nouPreu
                )
            } else {
                // Item not in trolley, add it with quantity 1
                currentState.copy(
                    trolley = currentState.trolley + TrolleyItem(shopItem, quantity = 1),
                    totalPrice = nouPreu
                )
            }
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