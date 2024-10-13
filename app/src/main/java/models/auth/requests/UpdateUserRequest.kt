package models.auth.requests

data class UpdateUserRequest(
    val id: Int,
    val nombre: String?,
    val numero_celular: String?,
    val correo: String?,
    val contrasena: String?
)
