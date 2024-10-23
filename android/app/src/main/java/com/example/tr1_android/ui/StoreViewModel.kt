package com.example.tr1_android.ui

import androidx.lifecycle.ViewModel
import com.example.tr1_android.data.ShopItem
import com.example.tr1_android.data.StoreUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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

}