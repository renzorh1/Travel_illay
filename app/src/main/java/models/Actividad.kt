package com.example.travelillay.models

import com.google.gson.annotations.SerializedName

data class Actividad(
    @SerializedName("Id") val id: Int?,
    @SerializedName("Nombre") val nombre: String?,
    @SerializedName("Tipo") val tipo: String?,
    @SerializedName("Lugar") val lugar: String?,
    @SerializedName("Calificacion") val calificacion: Double?,
    @SerializedName("HoraInicio") val horaInicio: String?,
    @SerializedName("HoraFin") val horaFin: String?
)
