// ItinerarioRequest.kt
package com.example.travelillay.models

data class ItinerarioRequest(
    val Nombre: String,
    val Lugar: String,
    val HoraInicio: String,
    val HoraFin: String,
    val UsuarioId: Int
)

