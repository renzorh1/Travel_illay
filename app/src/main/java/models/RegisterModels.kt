package com.example.travelillay.models

data class RegisterRequest(
    val Nombre: String,
    val NumeroCelular: String,
    val Correo: String,
    val Contrasena: String
)

data class RegisterResponse(
    val message: String,
    val newUser: RegisteredUser
)

data class RegisteredUser(
    val id: Int,
    val Nombre: String,
    val NumeroCelular: String,
    val Correo: String,
    val Contrasena: String
)
