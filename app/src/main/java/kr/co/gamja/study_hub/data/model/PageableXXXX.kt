package kr.co.gamja.study_hub.data.model

data class PageableXXXX(
    val offset: Int,
    val pageNumber: Int,
    val pageSize: Int,
    val paged: Boolean,
    val sort: SortXXXXX,
    val unpaged: Boolean
)