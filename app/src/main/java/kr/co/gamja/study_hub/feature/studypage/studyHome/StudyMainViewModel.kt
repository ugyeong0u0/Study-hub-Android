package kr.co.gamja.study_hub.feature.studypage.studyHome

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.BookmarkSaveDeleteResponse
import kr.co.gamja.study_hub.data.model.FindStudyResponseM
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.CallBackListener
import kr.co.gamja.study_hub.data.repository.RetrofitManager
import kr.co.gamja.study_hub.data.repository.StudyHubApi

class StudyMainViewModel(studyHubApi: StudyHubApi) : ViewModel() {
    private val tag = this.javaClass.simpleName

    private val _listSize = MutableLiveData<Int>()
    val listSize: LiveData<Int> get() = _listSize

    // 스터디 전체 가져올지 인기 스터지 가져오는지
    private var _isHotStudy = MutableLiveData<Boolean>(false) // false가 전체 스터디

    fun setIsHot(result: Boolean) {
        _isHotStudy.value = result
    }

    val studyMainFlow = Pager(
        PagingConfig(
            pageSize = 10,
            enablePlaceholders = false,
            initialLoadSize = 10
        )
    ) {
        StudyMainPagingSource(studyHubApi, _isHotStudy.value ?: false)
    }.flow.cachedIn(viewModelScope)

    // 스터디 리스트 총개수
    fun getStudyList() {
        viewModelScope.launch {
            try {
                val response = RetrofitManager.api.getStudyPostAll(false, 0, 10, null, false)
                if (response.isSuccessful) {
                    val result = response.body() as FindStudyResponseM
                    _listSize.value = result.totalCount
                }

            } catch (e: Exception) {
                Log.e(tag, "북마크조회 Exception: ${e.message}")
            }
        }
    }

    fun saveDeleteBookmarkItem(postId: Int?, params: CallBackListener) {
        viewModelScope.launch {
            try {
                val response = AuthRetrofitManager.api.saveDeleteBookmark(postId)
                if (response.isSuccessful) {
                    Log.d(tag, "북마크 저장 코드 code" + response.code().toString())
                    val result = response.body() as BookmarkSaveDeleteResponse
                    Log.d(tag, "저장인지 삭제인지 :" + result.created.toString())
                    params.isSuccess(true)
                } else {
                    // todo("회원이아닌경우")
                    params.isSuccess(false) // 비회원인 경우
                }
            } catch (e: Exception) {
                Log.e(tag, "북마크 저장삭제 Exception: ${e.message}")
            }
        }
    }
}