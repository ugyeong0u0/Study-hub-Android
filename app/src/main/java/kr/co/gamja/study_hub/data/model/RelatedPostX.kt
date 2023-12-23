package kr.co.gamja.study_hub.data.model

data class RelatedPostX(
    val major: String,
    val postId: Int,
    val remainingSeat: Int,
    val title: String,
    val userData: UserData
)