package kr.co.gamja.study_hub.data.model

data class ContentX(
    val bookmarked: Boolean,
    val close: Boolean,
    val createdDate: List<Int>,
    val filteredGender: String,
    val major: String,
    val penalty: Int,
    val penaltyWay: String,
    val postId: Int,
    val studyEndDate: List<Int>,
    val studyPerson: Int,
    val studyStartDate: List<Int>,
    val title: String,
    val userData: UserData
)