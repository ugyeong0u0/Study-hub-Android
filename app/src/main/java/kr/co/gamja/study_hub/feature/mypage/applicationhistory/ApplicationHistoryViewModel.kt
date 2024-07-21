package kr.co.gamja.study_hub.feature.mypage.applicationhistory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.ContentXXXXX
import kr.co.gamja.study_hub.data.model.ErrorResponse
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.CallBackListener
import kr.co.gamja.study_hub.data.repository.StudyHubApi

class ApplicationHistoryViewModel(studyHubApi: StudyHubApi) : ViewModel() {
    val tag = this.javaClass.simpleName

    // paging 초기화
    private val _reloadTrigger = MutableStateFlow(Unit)

    fun setReloadTrigger() {
        _reloadTrigger.value = Unit
    }

    // 리스트 개수
    private val _listSize = MutableLiveData<Int>()
    val listSize: LiveData<Int> get() = _listSize


    fun updateListSize() {
        viewModelScope.launch {
            val response = AuthRetrofitManager.api.getUserApplyHistory(0, 10)
            try {
                if (response.isSuccessful) {
                    val result = response.body()
                    _listSize.value = result?.requestStudyData?.numberOfElements
                    Log.d("신청스터디개수", _listSize.toString())
                }
            } catch (e: Exception) {
                Log.e(tag, "내가 신청한 스터디api에서 개수 조회 실패 code" + response.code().toString())
            }
        }
    }


    // 리스트 있는지 여부
    var isList=MutableLiveData<Boolean>(true)


    val applicationHistoryFlow: Flow<PagingData<ContentXXXXX>> = _reloadTrigger.flatMapLatest {
        Pager(
            PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            )
        ) {
            ApplicationHistoryPagingSource(studyHubApi)
        }.flow.cachedIn(viewModelScope)
    }


    // 신청내역에서 x 버튼 눌러서 신청 삭제하기
    fun deleteApplyStudy(studyId: Int, params: CallBackListener){
        viewModelScope.launch {
            try {
                val response = AuthRetrofitManager.api.deleteStudyHistory(studyId)
                Log.d(tag, "스터디id : $studyId")
                if (response.isSuccessful) {
                    Log.d(tag, "스터디 신청 삭제 성공")
                    params.isSuccess(true)
                } else {
                    params.isSuccess(false)
                    /*val errorResponse: ErrorResponse? = response.errorBody()?.let {
                        val gson = Gson()
                        gson.fromJson(it.charStream(), ErrorResponse::class.java)
                    }
                    if (errorResponse != null) {
                        Log.e(tag, errorResponse.message)
                    }*/
                }
            } catch (e: Exception) {
                e.stackTrace
                Log.e(tag,  " 스터디 신청 삭제 Exception: ${e.message}")
            }
        }
    }
}