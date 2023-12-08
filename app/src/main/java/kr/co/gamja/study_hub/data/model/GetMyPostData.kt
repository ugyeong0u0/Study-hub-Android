package kr.co.gamja.study_hub.data.model

data class GetMyPostData(
    val content: List<ContentXXX>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: PageableXXXX,
    val size: Int,
    val sort: SortXXXXX
)