package com.example.travelillay.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Cambia esto a tu dirección IP y puerto
    private const val BASE_URL = "http://192.168.18.33:3000/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }

    val apiService: ApiService by lazy {
        createService(ApiService::class.java)
    }

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }




}
