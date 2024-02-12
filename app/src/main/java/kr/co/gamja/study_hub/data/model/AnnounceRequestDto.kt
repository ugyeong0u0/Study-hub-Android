package kr.co.gamja.study_hub.data.model

data class AnnounceRequestDto(
    val content: List<AnnouncementContentDto>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: Sort
)