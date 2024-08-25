package com.example.travelillay.models

data class UserWithPreferences(
    val id: Int,
    val Nombre: String,
    val Celular: String,
    val Correo: String,
    val Contrasena: String,
    val Preferencias: Preferencias
)