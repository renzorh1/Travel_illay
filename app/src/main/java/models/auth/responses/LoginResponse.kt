package models.auth.responses

data class LoginResponse(
    val message: String,
    val id: Int,
    val nombre: String,
    val correo: String
)
