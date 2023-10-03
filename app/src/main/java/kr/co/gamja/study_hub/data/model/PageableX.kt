package kr.co.gamja.study_hub.data.model

data class PageableX(
    val offset: Int,
    val pageNumber: Int,
    val pageSize: Int,
    val paged: Boolean,
    val sort: SortXX,
    val unpaged: Boolean
)