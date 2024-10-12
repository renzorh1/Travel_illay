package com.example.travelillay.models

data class RegisterRequest(
    val nombre: String,
    val numero_celular: String,  // Cambia 'celular' por 'numero_celular'
    val correo: String,
    val contrasena: String,
    val preferencias: List<String> // Asegúrate de que este campo sea esperado en tu backend
)
data class RegisterResponse(
    val message: String,
    val newUser: RegisteredUser
)

data class RegisteredUser(
    val id: Int,
    val nombre: String,
    val numero_celular: String,
    val correo: String,
    val idioma_preferencia: String,  // Añadido para reflejar el valor predeterminado
    val actividades_favoritas: List<String>  // Añadido para reflejar el valor predeterminado
)
