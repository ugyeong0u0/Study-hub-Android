package kr.co.gamja.study_hub.data.model

data class PageableXXXXX(
    val offset: Int,
    val pageNumber: Int,
    val pageSize: Int,
    val paged: Boolean,
    val sort: SortXXXXXX,
    val unpaged: Boolean
)