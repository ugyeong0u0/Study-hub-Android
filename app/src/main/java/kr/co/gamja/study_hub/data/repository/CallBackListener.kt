package kr.co.gamja.study_hub.data.repository

//  단일 성공여부를 나타내주는 콜백
interface CallBackListener {
    fun isSuccess(result:Boolean)
}
// 단일 성공여부 정수 리턴 콜백
interface CallBackIntegerListener{
    fun isSuccess(result :Int)
}
// 리스너 안에 리스너 호출로 인해 Boolean 반환 callBack 하나 더 만듦
interface SecondCallBackListener{
    fun isSuccess(result:Boolean)
}
// 아이템 뷰 클릭
interface OnViewClickListener {
    fun onViewClick(postId: Int?)
}
// 스터디 무한 스크롤 first/ last
interface OnScrollCallBackListener{
    fun isFirst(result: Boolean)
    fun isLast(result: Boolean)
}
