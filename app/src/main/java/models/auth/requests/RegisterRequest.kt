package models.auth.requests

data class RegisterRequest(
    val nombre: String,
    val numero_celular: String,
    val correo: String,
    val contrasena: String,
    val preferencias: List<String>
)
