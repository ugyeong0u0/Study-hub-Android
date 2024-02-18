package kr.co.gamja.study_hub.data.model

data class RequestStudyData(
    val content: List<ContentX>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: PageableX,
    val size: Int,
    val sort: SortXX
)