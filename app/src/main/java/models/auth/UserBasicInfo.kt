package models.auth

data class UserBasicInfo(
    val id: Int,
    val nombre: String,
    val correo: String,
    val numero_celular: String,
    val contrasena: String
)
