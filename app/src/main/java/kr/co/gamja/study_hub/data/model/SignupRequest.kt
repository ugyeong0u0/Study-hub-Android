package kr.co.gamja.study_hub.data.model

data class SignupRequest(
    val email: String,
    val gender: String,
    val major: String,
    val nickname: String,
    val password: String
)