package com.example.travelillay.models

data class LoginRequest(
    val correo: String,  // Minúsculas para ser consistente con tu backend
    val contrasena: String
)

data class LoginResponse(
    val message: String,
    val id: Int,
    val nombre: String,  // Añadido para reflejar la respuesta de login en el backend
    val correo: String   // Añadido para reflejar la respuesta de login en el backend
)
