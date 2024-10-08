// ItinerarioResponse.kt
package com.example.travelillay.models

import com.google.gson.annotations.SerializedName

data class ItinerarioResponse(
    @SerializedName("Id") val id: Int,
    @SerializedName("Nombre") val nombre: String,
    @SerializedName("Lugar") val lugar: String,
    @SerializedName("HoraInicio") val horaInicio: String,
    @SerializedName("HoraFin") val horaFin: String,
    @SerializedName("UsuarioId") val usuarioId: Int
)