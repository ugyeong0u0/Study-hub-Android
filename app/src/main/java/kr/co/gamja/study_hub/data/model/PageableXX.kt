package kr.co.gamja.study_hub.data.model

data class PageableXX(
    val offset: Int,
    val pageNumber: Int,
    val pageSize: Int,
    val paged: Boolean,
    val sort: SortXXX,
    val unpaged: Boolean
)