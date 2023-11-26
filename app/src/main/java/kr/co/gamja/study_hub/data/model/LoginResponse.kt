package kr.co.gamja.study_hub.data.model

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String
)