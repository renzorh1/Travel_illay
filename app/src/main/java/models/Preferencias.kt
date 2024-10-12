package com.example.travelillay.models

data class Preferencias(
    val actividades_favoritas: List<String>,  // Cambiado a List<String> para representar múltiples actividades
    val hora_inicio_preferida: String?,  // Puede ser nulo
    val hora_fin_preferida: String?  // Puede ser nulo
)
