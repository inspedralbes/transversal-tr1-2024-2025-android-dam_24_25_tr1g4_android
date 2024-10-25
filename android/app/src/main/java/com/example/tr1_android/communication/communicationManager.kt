package com.example.tr1_android.communication

import com.example.tr1_android.data.ShopItem
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

const val DEV_URL = "http://10.0.2.2:3001"
//const val DEV_URL = "http://dam.inspedralbes.cat:24590"


private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
    .baseUrl(DEV_URL)
    .build()

interface StoreApiService {

    @GET("producteAndroidApp")
    suspend fun getProductes(): List<ShopItem>

}

object StoreApi {
    val retrofitService : StoreApiService by lazy {
        retrofit.create(StoreApiService::class.java)
    }
}