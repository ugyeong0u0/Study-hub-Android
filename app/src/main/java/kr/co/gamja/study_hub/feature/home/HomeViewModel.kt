package kr.co.gamja.study_hub.feature.home

import android.util.Log
import androidx.core.os.persistableBundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.FindStudyResponse
import kr.co.gamja.study_hub.data.model.HotStudyPostResponse
import kr.co.gamja.study_hub.data.repository.CallBackListener
import kr.co.gamja.study_hub.data.repository.OnScrollCallBackListener
import kr.co.gamja.study_hub.data.repository.RetrofitManager
import kr.co.gamja.study_hub.feature.studypage.studyHome.StudyMainAdapter

// TODO("스터디 메인 뷰모델 합치기")
class HomeViewModel : ViewModel() {
    val tag: String = this.javaClass.simpleName

    fun <T> getStudyPosts(adapter :T, isHot:Boolean, page:Int, size:Int,inquiryText:String?, titleaAndMajor:Boolean,params:OnScrollCallBackListener?){
        viewModelScope.launch {
            try {
                val response =RetrofitManager.api.getStudyPostAll(isHot,page,size,inquiryText,titleaAndMajor)
                if(response.isSuccessful){
                    val result =response.body() as FindStudyResponse
                    if(adapter is ItemOnRecruitingAdapter){
                        adapter.studyPosts = result
                        adapter.notifyDataSetChanged()
                    }else if(adapter is ItemCloseDeadlineAdapter){
                        adapter.studyPosts = result
                        adapter.notifyDataSetChanged()
                    }else if(adapter is StudyMainAdapter){
                        adapter.studyPosts=result
                        adapter.notifyDataSetChanged()
                        params?.isFirst(result.first)
                        params?.isLast(result.last)
                    }
                }

            }catch (e: Exception){
                Log.e(tag, "스터디 글 조회 Exception: ${e.message}")
            }
        }
    }

}