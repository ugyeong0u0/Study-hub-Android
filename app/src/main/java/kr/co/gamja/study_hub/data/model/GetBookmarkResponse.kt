package kr.co.gamja.study_hub.data.model

data class GetBookmarkResponse(
    val getBookmarkedPostsData: GetBookmarkedPostsData,
    val totalCount: Int
)