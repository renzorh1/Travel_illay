package com.example.travelillay.models

data class RegisterRequest(
    val Nombre: String,
    val Celular: String, // Cambiar de NumeroCelular a Celular
    val Correo: String,
    val Contrasena: String,
    val Preferencias: Preferencias? = null // Mantener Preferencias en caso se necesite
)


data class RegisterResponse(
    val message: String,
    val newUser: RegisteredUser
)

data class RegisteredUser(
    val id: Int,
    val Nombre: String,
    val Celular: String,
    val Correo: String,
    val Contrasena: String,
    val Preferencias: Preferencias? = null // Mantener para recibir la respuesta con Preferencias
)
