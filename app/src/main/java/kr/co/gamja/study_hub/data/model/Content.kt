package kr.co.gamja.study_hub.data.model

data class Content(
    val close: Boolean,
    val content: String,
    val major: String,
    val postId: Int,
    val remainingSeat: Int,
    val title: String
)