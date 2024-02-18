package kr.co.gamja.study_hub.data.model

data class ParticipateStudyData(
    val content: List<ContentX>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: Sort
)