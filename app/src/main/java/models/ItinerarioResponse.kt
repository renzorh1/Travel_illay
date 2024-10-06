// ItinerarioResponse.kt
package com.example.travelillay.models

data class ItinerarioResponse(
    val id: Int,
    val Nombre: String,
    val Lugar: String,
    val HoraInicio: String,
    val HoraFin: String,
    val UsuarioId: Int
)