package kr.co.gamja.study_hub.data.model

data class StudyContentResponseM(
    val bookmarked: Boolean,
    val chatUrl: String,
    val content: String,
    val createdDate: List<Int>,
    val filteredGender: String,
    val major: String,
    val penalty: Int,
    val penaltyWay: Any,
    val postId: Int,
    val postedUser: PostedUserXX,
    val relatedPost: List<RelatedPostX>,
    val remainingSeat: Int,
    val studyEndDate: List<Int>,
    val studyPerson: Int,
    val studyStartDate: List<Int>,
    val studyWay: String,
    val title: String,
    val usersPost: Boolean
)