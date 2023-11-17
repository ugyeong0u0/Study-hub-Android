package kr.co.gamja.study_hub.data.model

data class RelatedPost(
    val major: String,
    val postId: Int,
    val postedUser: PostedUserX,
    val remainingSeat: Int,
    val title: String
)