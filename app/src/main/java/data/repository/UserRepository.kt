package com.example.travelillay.data.repository

import com.example.travelillay.data.network.ApiService
import com.example.travelillay.data.network.RetrofitClient
import com.example.travelillay.models.LoginRequest
import com.example.travelillay.models.LoginResponse
import com.example.travelillay.models.RegisterRequest
import com.example.travelillay.models.RegisterResponse
import retrofit2.Call

class UserRepository {
    private val apiService: ApiService = RetrofitClient.createService(ApiService::class.java)

    fun registerUser(registerRequest: RegisterRequest): Call<RegisterResponse> {
        return apiService.registerUser(registerRequest)
    }

    fun loginUser(loginRequest: LoginRequest): Call<LoginResponse> {
        return apiService.loginUser(loginRequest)
    }
}
