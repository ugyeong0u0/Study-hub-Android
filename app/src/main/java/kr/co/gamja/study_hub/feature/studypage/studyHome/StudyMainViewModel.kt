package kr.co.gamja.study_hub.feature.studypage.studyHome

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.BookmarkSaveDeleteResponse
import kr.co.gamja.study_hub.data.model.ContentXXXX
import kr.co.gamja.study_hub.data.model.FindStudyResponseM
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.CallBackListener
import kr.co.gamja.study_hub.data.repository.RetrofitManager
import kr.co.gamja.study_hub.data.repository.StudyHubApi

class StudyMainViewModel(studyHubApi: StudyHubApi) : ViewModel() {
    private val tag = this.javaClass.simpleName

    // paging 초기화
    private val _reloadTrigger = MutableStateFlow(Unit)

    fun setReloadTrigger() {
        _reloadTrigger.value = Unit
    }

    private val _listSize = MutableLiveData<Int>()
    val listSize: LiveData<Int> get() = _listSize

    // 스터디 전체 가져올지 인기 스터지 가져오는지
    private var _isHotStudy = MutableLiveData<Boolean>(false) // false가 전체 스터디

    fun setIsHot(result: Boolean) {
        _isHotStudy.value = result
    }

    // 리스트 있는지 여부
    private val _isList = MutableLiveData<Boolean>()
    val isList: LiveData<Boolean> get() = _isList


    val studyMainFlow: Flow<PagingData<ContentXXXX>> = _reloadTrigger.flatMapLatest {
        Pager(
            PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            )
        ) {
            StudyMainPagingSource(studyHubApi, _isHotStudy.value ?: false)
        }.flow.cachedIn(viewModelScope)
    }


    // 스터디 리스트 총개수
    fun getStudyList() {
        viewModelScope.launch {
            try {
                val response = RetrofitManager.api.getStudyPostAll(false, 0, 10, null, false)
                if (response.isSuccessful) {
                    val result = response.body() as FindStudyResponseM
                    _listSize.value = result.totalCount
                    _isList.value = result.totalCount>0 // 리스트 개수가 0 이상으로 없어요 그림 노출 결정
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
                    Log.d(tag, "북마크 저장인지 삭제인지 :" + result.created.toString())
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