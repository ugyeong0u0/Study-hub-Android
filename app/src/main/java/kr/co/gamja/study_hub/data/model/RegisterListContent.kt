package kr.co.gamja.study_hub.data.model

data class RegisterListContent(
    val createdDate: List<Int>,
    val id: Int,
    val imageUrl: String,
    val inspection: String,
    val introduce: String,
    val major: String,
    val nickname: String
)