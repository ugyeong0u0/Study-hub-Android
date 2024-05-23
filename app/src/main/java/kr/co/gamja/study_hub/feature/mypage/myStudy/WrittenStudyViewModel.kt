package kr.co.gamja.study_hub.feature.mypage.myStudy

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.ContentXX
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.CallBackListener
import kr.co.gamja.study_hub.data.repository.StudyHubApi

class WrittenStudyViewModel(studyHubApi: StudyHubApi) : ViewModel() {
    private val tag = this.javaClass.simpleName

    // paging 초기화
    private val _reloadTrigger = MutableStateFlow(Unit)

    fun setReloadTrigger() {
        _reloadTrigger.value = Unit
    }

    // 리스트 개수
    private val _listSize = MutableLiveData<Int>(1)
    val listSize: LiveData<Int> get() = _listSize

    val myStudyFlow: Flow<PagingData<ContentXX>> = _reloadTrigger.flatMapLatest {
        Pager(
            PagingConfig(
                pageSize = 10, // 한페이지당 로드할 수
                enablePlaceholders = false, // 비로딩된 데이터 임시 표시 false
                initialLoadSize = 10 // 초기 로드 사이즈
            )
        ) { // 데이터 로드
            MyStudyPagingSource(studyHubApi)
        }.flow.cachedIn(viewModelScope) // 뷰모델 생명주기에 캐싱
    }

    // 내가 쓴 스터디 글 총 개수
    fun updateMyStudyListSize() {
        viewModelScope.launch {
            val response = AuthRetrofitManager.api.getMyStudy(0, 10)
            try {
                if (response.isSuccessful) {
                    val result = response.body()
                    _listSize.value = result?.totalCount
                    Log.d("내스터디개수", _listSize.toString())
                }
            } catch (e: Exception) {
                Log.e(tag, "내가 쓴 스터디api에서 개수 조회 실패 code" + response.code().toString())
            }
        }
    }

    fun shutDownStudy(postId: Int, params : CallBackListener) {
        viewModelScope.launch {
            val response = AuthRetrofitManager.api.deleteStudy(postId)
            try {
                if (response.isSuccessful) {
                    params.isSuccess(true)
                    Log.d(tag, "마감 성공")
                }else{
                    params.isSuccess(false)
                }
            } catch (e: Exception) {
                Log.e(tag, "스터디 삭제 오류 code" + response.code().toString())
            }
        }
    }

    //전체 스터디 삭제
    fun deleteAllStudy(){
        viewModelScope.launch(Dispatchers.IO){
            val response = AuthRetrofitManager.api.deleteAllStudy()
            try{
                if (response.isSuccessful){
                    Log.d(tag, "삭제 성공")
                } else {
                    Log.d(tag, "삭제 실패")
                }
            } catch (e : Exception){
                Log.e(tag, "전체 삭제 오류 : ${response.errorBody().toString()}")
            }
        }
    }
}