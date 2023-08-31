package kr.co.gamja.study_hub.model

data class EmailValidRequest(
    val authCode: String,
    val email: String
)