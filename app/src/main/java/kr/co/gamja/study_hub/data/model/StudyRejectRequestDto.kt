package kr.co.gamja.study_hub.data.model

data class StudyRejectRequestDto(
    val rejectReason: String,
    val rejectedUserId: Int,
    val studyId: Int
)