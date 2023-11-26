package kr.co.gamja.study_hub.data.model

data class PageableXXX(
    val offset: Int,
    val pageNumber: Int,
    val pageSize: Int,
    val paged: Boolean,
    val sort: SortXXXX,
    val unpaged: Boolean
)