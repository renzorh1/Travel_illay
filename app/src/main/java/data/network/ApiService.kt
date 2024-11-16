package com.example.travelillay.data.network

import models.auth.requests.*
import models.auth.responses.*
import models.itineraries.Actividad
import models.itineraries.Itinerario
import models.preferences.Preferencias
import models.preferences.PreferenciasRequest
import models.itineraries.ProximoItinerarioIdResponse
import models.itineraries.ItinerariosResponse
import models.itineraries.ActividadesResponse
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
    fun crearItinerario(@Body itinerario: Itinerario): Call<Itinerario> // Asegúrate de que sea Call<Itinerario>

    @GET("api/googlePlaces/nearby")
    fun getNearbyPlaces(): Call<List<Actividad>>

    @GET("api/googlePlaces/places")
    fun getNearbyPlaces(@Query("types") types: String): Call<List<Actividad>>

    // Actualiza el método para eliminar el último itinerario de un usuario
    @DELETE("api/itinerarios/eliminar/{usuario_id}")
    fun eliminarUltimoItinerario(@Path("usuario_id") usuarioId: Int): Call<Void>

    @GET("api/googlePlaces/activity")
    fun getActivityByName(@Query("name") name: String): Call<Actividad>

    @POST("api/actividad/Guardaractividades")
    fun guardarActividad(@Body actividad: Actividad): Call<Actividad>

    @POST("api/itinerarioactividad/guardarelacion")
    fun guardarRelacionItinerarioActividad(@Body body: RelacionRequest): Call<RelacionResponse>

    @GET("api/itinerarios/ultimo-id/{usuario_id}")
    fun obtenerProximoItinerarioId(@Path("usuario_id") usuarioId: Int): Call<ProximoItinerarioIdResponse>

    @GET("api/itinerarios/usuario/{usuario_id}/itinerarios")
    fun obtenerItinerariosPorUsuario(@Path("usuario_id") usuarioId: Int): Call<ItinerariosResponse>

    @GET("api/itinerarios/{itinerarioId}/actividades")
    fun obtenerActividadesDeItinerario(@Path("itinerarioId") itinerarioId: Int): Call<ActividadesResponse>

    @DELETE("api/itinerarios/{itinerarioId}")
    fun eliminarItinerario(@Path("itinerarioId") itinerarioId: Int): Call<Void>

    @DELETE("api/actividad/eliminar/{actividadId}")
    fun eliminarActividad(@Path("actividadId") actividadId: Int): Call<Void>

    @GET("api/actividad/actividades-con-id/{itinerarioId}")
    fun obtenerActividadesConId(@Path("itinerarioId") itinerarioId: Int): Call<List<Actividad>>
}

