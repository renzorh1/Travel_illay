package com.example.travelillay.network

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
