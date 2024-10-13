package models.auth.responses

import models.auth.UserBasicInfo

data class GetUserResponse(
    val success: Boolean,
    val message: String,
    val data: UserBasicInfo
)
