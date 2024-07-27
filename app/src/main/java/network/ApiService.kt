package com.example.travelillay.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/users/register")
    fun registerUser(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("api/users/login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>
}
