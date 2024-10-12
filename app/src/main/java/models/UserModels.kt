package com.example.travelillay.models

// Para obtener los datos del usuario
data class GetUserResponse(
    val success: Boolean,
    val message: String,
    val data: UserBasicInfo
)


// Para actualizar el usuario
data class UpdateUserRequest(
    val id: Int,
    val nombre: String?,
    val numero_celular: String?,
    val correo: String?,
    val contrasena: String?
)

data class UpdateUserResponse(
    val success: Boolean,
    val message: String,
    val data: UserBasicInfo
)
