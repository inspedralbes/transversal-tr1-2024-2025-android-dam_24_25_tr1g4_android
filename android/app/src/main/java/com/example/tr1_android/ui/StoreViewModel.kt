package com.example.tr1_android.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.tr1_android.StoreScreen
import com.example.tr1_android.communication.DEV_URL
import com.example.tr1_android.communication.StoreApi
import com.example.tr1_android.data.BuyItem
import com.example.tr1_android.data.BuyUiState
import com.example.tr1_android.data.Comanda
import com.example.tr1_android.data.ComandaWithString
import com.example.tr1_android.data.CompraRequest
import com.example.tr1_android.data.CompraResponse
import com.example.tr1_android.data.LoginRequest
import com.example.tr1_android.data.RegisterRequest
import com.example.tr1_android.data.ShopItem
import com.example.tr1_android.data.StoreUiState
import com.example.tr1_android.data.TrolleyItem
import com.example.tr1_android.data.User
import com.example.tr1_android.data.UserUiState
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter

class StoreViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(StoreUiState())
    val uiState: StateFlow<StoreUiState> = _uiState.asStateFlow()

    private val _userUiState = MutableStateFlow<UserUiState>(UserUiState.Loading)
    val userUiState: StateFlow<UserUiState> = _userUiState.asStateFlow()

    private val _buyUiState = MutableStateFlow<BuyUiState>(BuyUiState.Loading)
    val buyUiState: StateFlow<BuyUiState> = _buyUiState.asStateFlow()

    val gson = Gson()

    lateinit var mSocket: Socket

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

    fun login(loginRequest: LoginRequest, navController: NavHostController): Boolean {
        var valid = false
        viewModelScope.launch {

            try {
                val response = StoreApi.retrofitService.login(loginRequest)

                if (response.usuari != null) {
                    println(response)

                    _uiState.update { currentState ->
                        currentState.copy(
                            userInfo = response.usuari
                        )}
                }
                if (response.valid) {
                    valid = true
                    navController.navigate(StoreScreen.Tenda.name)
                } else {
                    valid = false
                    setShowDialog(value = true)
                    println("Credentials error")
                }
            } catch (e: Exception) {
                println("error: ${e.message}")
            }
        }

        return valid

    }

    fun register(registerRequest: RegisterRequest, navController: NavHostController) {
        println("registrant")
        viewModelScope.launch {
            val response = StoreApi.retrofitService.register(registerRequest)
            println(response)
            if (response.valid) {
                _uiState.update { currentState ->
                    currentState.copy(
                        userInfo = response.usuari
                    )}
                navController.navigate(StoreScreen.Tenda.name)
            } else {
                setShowDialog(value = true)
                println("Credentials error")
            }
        }
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
            val preuTotalRounded = BigDecimal(preuTotal).setScale(2, RoundingMode.HALF_UP).toDouble()


            val response = StoreApi.retrofitService.postComanda(CompraRequest(idUser,productes,preuTotalRounded))

            if (response.valid) {
                _buyUiState.value = BuyUiState.Success(response)
                _uiState.update { currentState ->
                    currentState.copy(
                        comandaActual = Comanda(0,idUser,0, productes, preuTotalRounded)
                    )}
                clearTrolley()
            } else {
                _buyUiState.value = BuyUiState.Error
            }

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

    fun setShowDialog(value: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                showDialog = value
            )
        }
    }

    fun getComandes() {
        viewModelScope.launch {
            println("getting comandes")
            _userUiState.value = UserUiState.Loading
            val comandes = StoreApi.retrofitService.getComandes()

            println("comandes rebudes")

            val comandesFiltered = comandes.filter { comanda -> comanda.iduser == _uiState.value.userInfo.id }

            parseAndSaveComandes(comandesFiltered)

            println("comandes guardades")

            /*var comandesTransformed: MutableList<Comanda> = mutableListOf()
            comandesFiltered.forEach { comanda ->
                println(comanda.productes)
                val productesList: List<BuyItem> = gson.fromJson(comanda.productes, Array<BuyItem>::class.java).toList()

                comandesTransformed.add(Comanda(comanda.id, comanda.iduser, comanda.estatus, productesList, comanda.preu_total))

            }

            println(comandesTransformed)


            _uiState.update { currentState ->
                currentState.copy(
                    comandes = comandesTransformed
                )
            }*/
            _userUiState.value = UserUiState.Success(User())
        }
    }

    fun setComandaActual(comanda: Comanda) {
        _buyUiState.value = BuyUiState.Loading
        _uiState.update { currentState ->
            currentState.copy(
                comandaActual = comanda
            )
        }
        _buyUiState.value = BuyUiState.Success(CompraResponse(true))
    }

    private val actualitzarComandes = Emitter.Listener { args ->
        val comandesJson = args[0] as String

        println("comandes rebudes: ${comandesJson}")


//        val comandes: List<ComandaWithString> = comandesJson.filterIsInstance<ComandaWithString>().toList()
//        val comandes: List<ComandaWithString> = comandesJson.mapNotNull {
//            if (it is ComandaWithString) it else null
//        }
//        val comandesJson: String = args[0] as String

        val comandes = Gson().fromJson(comandesJson, Array<ComandaWithString>::class.java)

        println("comandes tractades: ${comandes}")

        val comandesFiltered = comandes.filter { comanda -> comanda.iduser == _uiState.value.userInfo.id }

        val comandesCorrectes = parseAndSaveComandes(comandesFiltered)

        resetComandaActual(comandesCorrectes)

    }

    private fun resetComandaActual(comandes: List<Comanda>) {
        val comandaActualitzada = comandes.find { comanda -> comanda.id == _uiState.value.comandaActual.id }

        if (comandaActualitzada != null) {
            setComandaActual(comandaActualitzada)
        }
    }

    private fun parseAndSaveComandes(comandes: List<ComandaWithString>): List<Comanda> {

        println("comandes filtrades: ${comandes}")

        var comandesTransformed: MutableList<Comanda> = mutableListOf()
        comandes.forEach { comanda ->
            println(comanda.productes)
            val productesList: List<BuyItem> = gson.fromJson(comanda.productes, Array<BuyItem>::class.java).toList()

            comandesTransformed.add(Comanda(comanda.id, comanda.iduser, comanda.estat, productesList, comanda.preu_total))

        }

        println(comandesTransformed)


        _uiState.update { currentState ->
            currentState.copy(
                comandes = comandesTransformed
            )
        }

        return comandesTransformed
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

            // Socket init

            try {
                mSocket = IO.socket(DEV_URL)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("SocketIO", "Failed to connect to socket", e)
            }
            mSocket.connect()
            mSocket.on(Socket.EVENT_CONNECT) {
                Log.d("SocketIO", "Connected to socket: ${mSocket.id()}")
                mSocket.on("actualizarArrayComandes", actualitzarComandes)
            }
            mSocket.on(Socket.EVENT_DISCONNECT) {
                Log.d("SocketIO", "Disconnected from socket")
            }
        }

    }

}