package com.example.tr1_android.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tr1_android.communication.StoreApi
import com.example.tr1_android.data.ShopItem
import com.example.tr1_android.data.StoreUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StoreViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(StoreUiState())
    val uiState: StateFlow<StoreUiState> = _uiState.asStateFlow()

    fun addItemToTrolley(trolley: ShopItem) {
        _uiState.update { currentState ->
            currentState.copy(
                trolley = currentState.trolley + trolley
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

            println(shopItems)


            _uiState.update { currentState ->
            currentState.copy(
                shopItems = shopItems,
                isLoading = false
            )}
        }

    }

}