package kr.co.gamja.study_hub.data.model

data class EmailValidRequest(
    val authCode: String,
    val email: String
)