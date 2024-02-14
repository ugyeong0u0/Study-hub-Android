package kr.co.gamja.study_hub.data.model

data class NewPasswordRequest(
    val auth: Boolean,
    val email : String,
    val password: String
)