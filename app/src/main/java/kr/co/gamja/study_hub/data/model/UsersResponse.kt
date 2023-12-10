package kr.co.gamja.study_hub.data.model

data class UsersResponse(
    val bookmarkCount: Int,
    val email: String,
    val gender: String,
    val imageUrl: String,
    val major: String,
    val nickname: String,
    val participateCount: Int,
    val postCount: Int
)