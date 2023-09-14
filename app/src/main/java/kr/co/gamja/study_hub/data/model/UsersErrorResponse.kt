package kr.co.gamja.study_hub.data.model

data class UsersErrorResponse(
    val error: String,
    val path: String,
    val status: Int,
    val timestamp: Long
)