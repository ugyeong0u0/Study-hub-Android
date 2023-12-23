package kr.co.gamja.study_hub.data.model

data class PostDataByInquiries(
    val content: List<ContentXXXX>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: PageableXXXXX,
    val size: Int,
    val sort: SortXXXXXX
)