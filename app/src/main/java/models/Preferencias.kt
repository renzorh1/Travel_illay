package com.example.travelillay.models

data class Preferencias(
    val actividades_favoritas: List<String>,
    val horario_preferido: Horario,
    val idioma_preferido: String // Añadir el campo para el idioma preferido
)

data class Horario(
    val inicio: String,
    val fin: String
)