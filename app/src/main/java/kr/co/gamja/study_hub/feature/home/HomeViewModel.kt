package kr.co.gamja.study_hub.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.FindStudyResponse
import kr.co.gamja.study_hub.data.model.HotStudyPostResponse
import kr.co.gamja.study_hub.data.repository.RetrofitManager
// TODO("스터디 메인 뷰모델 합치기")
class HomeViewModel:ViewModel() {
    val tag = this.javaClass.simpleName
    fun getStudyPosts(adapter:ItemOnRecruitingAdapter){
        viewModelScope.launch {
            try {
                val response=RetrofitManager.api.getStudyPostAll(0,5)
                if (response.isSuccessful) {
                    val result = response.body() as FindStudyResponse
                    adapter.studyPosts=result
                    adapter.notifyDataSetChanged()
                }
            }catch (e: Exception) {
                Log.e(tag, "스터디 글 조회 Exception: ${e.message}")
            }
        }
    }
    // 인기순 5개 포스트
    fun getHotStudyPosts(adapter: ItemCloseDeadlineAdapter){
        viewModelScope.launch {
            try {
                val response=RetrofitManager.api.getHotStudyPostAll(0,4)
                if (response.isSuccessful) {
                    val result = response.body() as HotStudyPostResponse
                    adapter.studyPosts=result
                    adapter.notifyDataSetChanged()
                }
            }catch (e: Exception) {
                Log.e(tag, "스터디 글 조회 Exception: ${e.message}")
            }
        }
    }

}