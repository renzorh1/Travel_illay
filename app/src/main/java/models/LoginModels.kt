package com.example.travelillay.models

data class LoginRequest(
    val Correo: String,
    val Contrasena: String
)

data class LoginResponse(
    val id: Int,
    val message: String
)
