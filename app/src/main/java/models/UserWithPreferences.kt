package com.example.travelillay.models

data class UserWithPreferences(
    val id: Int,
    val actividades_favoritas: List<String>?,  // Convertido desde JSON en el backend
    val hora_inicio_preferida: String?,  // Puede ser nulo
    val hora_fin_preferida: String?  // Puede ser nulo
)
