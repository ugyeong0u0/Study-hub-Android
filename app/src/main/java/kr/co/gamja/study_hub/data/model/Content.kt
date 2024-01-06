package kr.co.gamja.study_hub.data.model

data class Content(
    val commentId: Int,
    val commentedUserData: CommentedUserData,
    val content: String,
    val createdDate: List<Int>,
    val usersComment: Boolean
)