package com.example.travelillay.models

data class UserBasicInfo(
    val id: Int,
    val nombre: String,
    val correo: String,
    val numero_celular: String,
    val contrasena: String // Asegúrate de que esto esté protegido adecuadamente
)
