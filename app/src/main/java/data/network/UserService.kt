package com.example.travelillay.data.network

import com.example.travelillay.models.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("users/user/{id}")
    fun getUserById(@Path("id") id: Int): Call<User>
}
