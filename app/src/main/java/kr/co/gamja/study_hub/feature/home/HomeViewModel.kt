package kr.co.gamja.study_hub.feature.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.BookmarkSaveDeleteResponse
import kr.co.gamja.study_hub.data.model.FindStudyResponseM
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.CallBackListener

class HomeViewModel : ViewModel() {
    val tag: String = this.javaClass.simpleName

    // 프로그래스 보이기 안보이기 처리
    var progressRecruiting = MutableLiveData<Boolean>(false)

    var progressDeadLine = MutableLiveData<Boolean>(false)

    private val _visibleProgress = MutableLiveData<Boolean>(true)
    val visibleProgress: LiveData<Boolean> get() = _visibleProgress

    fun updateProgressBar(result : Boolean){
        _visibleProgress.value=result
    }

    // ItemOnRecruitingAdapter 모집 중 리스트 값
    fun getRecruitingStudy(adapter: ItemOnRecruitingAdapter,
                           isHot: Boolean,
                           page: Int,
                           size: Int,
                           inquiryText: String?,
                           titleAndMajor: Boolean,
                           params : CallBackListener){
        viewModelScope.launch{
            try {
                val response =
                    AuthRetrofitManager.api.getStudyPostAll(
                        isHot,
                        page,
                        size,
                        inquiryText,
                        titleAndMajor
                    )

                Log.d("HomeViewModel: ItemOnRecruitingAdapter viewModel응답코드 " , response.code().toString())
                if(response.isSuccessful){
                    val result = response.body() as FindStudyResponseM
                    Log.d("HomeViewModel: ItemOnRecruitingAdapter viewModel 안" , "")
                    adapter.studyPosts = result
                    adapter.notifyDataSetChanged()
                    params.isSuccess(true)
                }
            }catch (e : Exception){
                Log.e(tag, "스터디 글 조회 Exception: ${e.message}")
            }
        }
    }


    fun <T> getStudyPosts(
        adapter: T,
        isHot: Boolean,
        page: Int,
        size: Int,
        inquiryText: String?,
        titleaAndMajor: Boolean,
        params : CallBackListener
    ) {
        viewModelScope.launch {
            try {
                val response =
                    AuthRetrofitManager.api.getStudyPostAll(
                        isHot,
                        page,
                        size,
                        inquiryText,
                        titleaAndMajor
                    )
                if (response.isSuccessful) {
                    val result = response.body() as FindStudyResponseM
                    if (adapter is ItemOnRecruitingAdapter) {
                        Log.d("ItemOnRecruitingAdapter viewModel 안" , "")
                        adapter.studyPosts = result
                        adapter.notifyDataSetChanged()
                        params.isSuccess(true)
                    } else if (adapter is ItemCloseDeadlineAdapter) {
                        adapter.studyPosts = result
                        adapter.notifyDataSetChanged()
                        params.isSuccess(true)
                    }
                }

            } catch (e: Exception) {
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