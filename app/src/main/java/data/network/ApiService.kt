package com.example.travelillay.data.network

import models.auth.requests.*
import models.auth.responses.*
import models.itineraries.Actividad
import models.itineraries.Itinerario
import models.preferences.Preferencias
import models.preferences.PreferenciasRequest
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("api/users/register")
    fun registerUser(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("api/users/login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>

    @GET("api/users/user/{id}")
    fun getUserBasicInfo(@Path("id") userId: Int): Call<GetUserResponse>

    @PUT("api/users/update")
    fun updateUser(@Body user: UpdateUserRequest): Call<Void>

    @GET("api/users/user/{id}/preferences")
    fun getUserPreferences(@Path("id") userId: Int): Call<Preferencias>

    @PUT("api/users/user/update/preferences/{id}")
    fun updateUserPreferences(@Path("id") userId: Int, @Body preferenciasRequest: PreferenciasRequest): Call<Void>

    @POST("api/itinerarios/crear")
    fun crearItinerario(@Body itinerario: Itinerario): Call<Itinerario>

    @GET("api/actividades/obtener")
    fun obtenerActividades(): Call<List<Actividad>>
}
