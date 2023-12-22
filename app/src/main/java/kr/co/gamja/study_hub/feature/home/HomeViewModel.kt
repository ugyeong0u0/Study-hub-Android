package kr.co.gamja.study_hub.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.BookmarkSaveDeleteResponse
import kr.co.gamja.study_hub.data.model.FindStudyResponse
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.RetrofitManager

// TODO("스터디 메인 뷰모델 합치기")
class HomeViewModel : ViewModel() {
    val tag: String = this.javaClass.simpleName

    fun <T> getStudyPosts(adapter :T, isHot:Boolean, page:Int, size:Int,inquiryText:String?, titleaAndMajor:Boolean){
        viewModelScope.launch {
            try {
                val response =
                    AuthRetrofitManager.api.getStudyPostAll(isHot,page,size,inquiryText,titleaAndMajor)
                if(response.isSuccessful){
                    val result =response.body() as FindStudyResponse
                    if(adapter is ItemOnRecruitingAdapter){
                        adapter.studyPosts = result
                        adapter.notifyDataSetChanged()
                    }else if(adapter is ItemCloseDeadlineAdapter){
                        adapter.studyPosts = result
                        adapter.notifyDataSetChanged()
                    }
                }

            }catch (e: Exception){
                Log.e(tag, "스터디 글 조회 Exception: ${e.message}")
            }
        }
    }

    fun saveDeleteBookmarkItem(postId: Int?) {
        Log.d(tag, postId.toString())
        viewModelScope.launch {
            try {
                val response = AuthRetrofitManager.api.saveDeleteBookmark(postId)
                if (response.isSuccessful) {
                    Log.d(tag, "북마크 저장 코드 code" + response.code().toString())
                    val result = response.body() as BookmarkSaveDeleteResponse
                    Log.d(tag, "저장인지 삭제인지 :" + result.created.toString())

                }
            } catch (e: Exception) {
                Log.e(tag, "북마크 저장삭제 Exception: ${e.message}")
            }
        }
    }

}