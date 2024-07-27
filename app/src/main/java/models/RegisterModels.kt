package com.example.travelillay.models

data class RegisterRequest(
    val Nombre: String,
    val NumeroCelular: String,
    val Correo: String,
    val Contrasena: String
)

data class RegisterResponse(
    val message: String,
    val newUser: User
)

data class User(
    val id: Int,
    val Nombre: String,
    val NumeroCelular: String,
    val Correo: String,
    val Contrasena: String
)
