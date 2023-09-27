package kr.co.gamja.study_hub.data.model

data class CreateStudyRequest(
    val chatUrl: String,
    val close: Boolean,
    val content: String,
    val gender: String,
    val major: String,
    val penalty: Int,
    val studyEndDate: String,
    val studyPerson: Int,
    val studyStartDate: String,
    val studyWay: String,
    val title: String
)