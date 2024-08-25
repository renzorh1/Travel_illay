package com.example.travelillay.models

data class Preferencias(
    val actividades_favoritas: List<String>,
    val horario_preferido: Horario,
    val notificaciones_activadas: Boolean
)

data class Horario(
    val inicio: String,
    val fin: String
)
