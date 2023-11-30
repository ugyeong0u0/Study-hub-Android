package kr.co.gamja.study_hub.data.model

data class CorrectStudyRequest(
    val chatUrl: String,
    val content: String,
    val gender: String,
    val major: String,
    val penalty: Int,
    val penaltyWay: String,
    val postId: Int,
    val studyEndDate: String,
    val studyPerson: Int,
    val studyStartDate: String,
    val studyWay: String,
    val title: String
)