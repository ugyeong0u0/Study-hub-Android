package kr.co.gamja.study_hub.model.dto

data class EmailValidRequest(
    val authCode: String,
    val email: String
)