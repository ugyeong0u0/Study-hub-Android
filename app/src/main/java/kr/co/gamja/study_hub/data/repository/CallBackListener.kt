package kr.co.gamja.study_hub.data.repository

import kr.co.gamja.study_hub.feature.toolbar.bookmark.PostingId

//  단일 성공여부를 나타내주는 콜백
interface CallBackListener {
    fun isSuccess(result: Boolean)
}

// 단일 성공여부 정수 리턴 콜백
interface CallBackIntegerListener {
    fun isSuccess(result: Int)
}

// 리스너 안에 리스너 호출로 인해 Boolean 반환 callBack 하나 더 만듦
interface SecondCallBackListener {
    fun isSuccess(result: Boolean)
}

// 아이템 뷰 클릭
interface OnViewClickListener {
    fun onViewClick(postId: Int?)
}

// 뷰 아이템 클릭 리스너
interface OnItemsClickListener {
    // 아이템 구분 map key에 맞는 int 값과 itemId
    fun getItemValue(whatItem: Int, itemValue: Int)
}
// 북마크용 리스너
interface OnBookmarkClickListener {
    fun onItemClick(tagId: String?, postId: Int? = 0)
}
// 댓글 수정용 리스너 (아이템 구분 map key에 맞는 int 값과 itemId, 댓글내용)
interface OnCommentClickListener{
    fun getCommentValue(whatItem: Int, itemValue: Int, comment: String)
}
// 북마크, 참여자페이지로 넘어갈 때 전용 뷰 클릭 리스너
interface OnPostingIdClickListener{
    fun getItemValue(whatItem: Int, postingId: PostingId)
}