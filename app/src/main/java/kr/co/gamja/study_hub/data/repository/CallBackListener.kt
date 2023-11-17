package kr.co.gamja.study_hub.data.repository

//  단일 성공여부를 나타내주는 콜백
interface CallBackListener {
    fun isSuccess(result:Boolean)
}
// 아이템 뷰 클릭
interface OnViewClickListener {
    fun onViewClick(postId: Int?)
}