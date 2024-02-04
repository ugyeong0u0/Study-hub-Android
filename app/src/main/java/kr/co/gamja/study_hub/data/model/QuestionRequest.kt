package kr.co.gamja.study_hub.data.model

data class QuestionRequest(
    val content: String,
    val title: String,
    val toEmail: String
)