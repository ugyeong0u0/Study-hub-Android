package kr.co.gamja.study_hub.data.model

data class UsersResponse(
    val bookmarkCount: Int,
    val email: String,
    val gender: String,
    val imageUrl: Any,
    val major: String,
    val nickname: String,
    val participateCount: Int,
    val postCount: Int
)