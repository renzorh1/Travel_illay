package com.example.travelillay.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("users/user/{id}")
    fun getUserById(@Path("id") id: Int): Call<User>
}
