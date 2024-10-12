package com.example.travelillay.models

data class Preferencias(
    val actividades_favoritas: String,  // Cambiado a String
    val hora_inicio_preferida: String?,  // Puede ser nulo
    val hora_fin_preferida: String?  // Puede ser nulo
)
