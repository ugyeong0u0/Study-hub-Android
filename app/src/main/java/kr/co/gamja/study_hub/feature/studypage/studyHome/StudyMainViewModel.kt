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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.BookmarkSaveDeleteResponse
import kr.co.gamja.study_hub.data.model.ContentXX
import kr.co.gamja.study_hub.data.model.ContentXXXX
import kr.co.gamja.study_hub.data.model.FindStudyResponseM
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.CallBackListener
import kr.co.gamja.study_hub.data.repository.RetrofitManager
import kr.co.gamja.study_hub.data.repository.StudyHubApi

class StudyMainViewModel(val studyHubApiAuth: StudyHubApi, val studyHubApi: StudyHubApi) :
    ViewModel() {
    private val tag = this.javaClass.simpleName

    // 둘러보기인지
    var isUserLogin = MutableLiveData<Boolean>(false)

    // 프로그래스바 로딩여부
    var studyProBar = MutableLiveData<Boolean>(true)

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
    var isList = MutableLiveData<Boolean>(true)

    val studyMainFlow: Flow<PagingData<ContentXXXX>>
        get() = if (isUserLogin.value == true) {
            _reloadTrigger.flatMapLatest {
                Pager(
                    PagingConfig(
                        pageSize = 10,
                        enablePlaceholders = false,
                        initialLoadSize = 10
                    )
                ) {
                    StudyMainPagingSource(studyHubApiAuth, _isHotStudy.value ?: false)
                }.flow.cachedIn(viewModelScope)
            }

        } else {
            Log.e(tag, "비회원 스터디 탭 ")
            _reloadTrigger.flatMapLatest {
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

    // 회원인지 확인만 함 -> 회원 단권 조회로 함
    fun isUserOrNotUser(params: CallBackListener) {
        viewModelScope.launch {
            try {
                val response = AuthRetrofitManager.api.getUserInfo()
                isUserLogin.value = response.isSuccessful
                Log.e(tag,"유저로그인"+isUserLogin.value.toString())
            } catch (e: Exception) {
                isUserLogin.value = false
                Log.e(tag, "회원인지 아닌지 확인 불가 Exception: ${e.message}")
            }
            finally {
                params.isSuccess(true)
            }
        }
    }
}