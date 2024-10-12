package com.example.travelillay.models

data class PreferenciasRequest(
    val actividades_favoritas: List<String>,
    val hora_inicio_preferida: String,
    val hora_fin_preferida: String
)
