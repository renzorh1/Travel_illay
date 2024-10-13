package models.auth.responses

data class RegisterResponse(
    val message: String,
    val newUser: RegisteredUser
)

data class RegisteredUser(
    val id: Int,
    val nombre: String,
    val numero_celular: String,
    val correo: String,
    val idioma_preferencia: String,
    val actividades_favoritas: List<String>
)
