package com.example.travelillay.data.network

import com.example.travelillay.models.*
import com.example.travelillay.models.UserBasicInfo
import com.example.travelillay.models.Preferencias
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @POST("api/users/register")
    fun registerUser(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("api/users/login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>

    @GET("api/users/user/{id}")
    fun getUserBasicInfo(@Path("id") userId: Int): Call<UserBasicInfo>

    @GET("api/users/user/{id}/preferences")
    fun getUserPreferences(@Path("id") userId: Int): Call<Preferencias>

    @PUT("api/users/update")
    fun updateUser(@Body user: UserBasicInfo): Call<Void>


}
