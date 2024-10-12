package com.example.travelillay.data.network

import com.example.travelillay.models.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("api/users/register")
    fun registerUser(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("api/users/login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>

    @GET("api/users/user/{id}")
    fun getUserBasicInfo(@Path("id") userId: Int): Call<GetUserResponse>

    @GET("api/users/user/{id}/preferences")
    fun getUserPreferences(@Path("id") userId: Int): Call<Preferencias>

    @PUT("api/users/update")
    fun updateUser(@Body user: UpdateUserRequest): Call<Void>


    @PUT("api/users/update/schedule/{id}")
    fun updateUserSchedule(@Path("id") userId: Int, @Body schedule: Map<String, String>): Call<Void>

    @PUT("api/users/update/activities/{id}")
    fun updateUserActivities(@Path("id") userId: Int, @Body activities: List<Actividad>): Call<Void>

}
