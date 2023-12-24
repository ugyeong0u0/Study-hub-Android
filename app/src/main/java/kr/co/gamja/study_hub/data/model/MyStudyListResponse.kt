package kr.co.gamja.study_hub.data.model

data class MyStudyListResponse(
    val posts: GetMyPostData,
    val totalCount: Int
)