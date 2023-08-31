package kr.co.gamja.study_hub.model

data class LoginResponse(
    val code: Int,
    val `data`: Data,
    val msg: String
)