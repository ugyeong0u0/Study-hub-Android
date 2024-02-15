package kr.co.gamja.study_hub.data.model

data class AnnouncementContentDto(
    val content: String,
    val noticeId: Int,
    val createdDate : List<Int>,
    val title: String
)