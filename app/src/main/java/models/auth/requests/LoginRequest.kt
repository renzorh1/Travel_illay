package models.auth.requests

data class LoginRequest(
    val correo: String,
    val contrasena: String
)
