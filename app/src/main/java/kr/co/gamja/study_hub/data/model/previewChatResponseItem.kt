package kr.co.gamja.study_hub.data.model

data class previewChatResponseItem(
    val commentId: Int,
    val commentedUserData: CommentedUserDataX,
    val content: String,
    val createdDate: List<Int>,
    val usersComment: Boolean
)