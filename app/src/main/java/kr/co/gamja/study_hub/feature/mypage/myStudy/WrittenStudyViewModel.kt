package kr.co.gamja.study_hub.feature.mypage.myStudy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.repository.StudyHubApi

class WrittenStudyViewModel(studyHubApi: StudyHubApi) :ViewModel() {
    private val tag = this.javaClass.simpleName

    // 리스트 개수
    private val _listSize = MutableLiveData<Int>()
    val listSize: LiveData<Int> get() = _listSize

    val myStudyFlow = Pager(
        PagingConfig(
            pageSize = 10, // 한페이지당 로드할 수
            enablePlaceholders = false, // 비로딩된 데이터 임시 표시 false
            initialLoadSize = 10 // 초기 로드 사이즈
    )
        ){ // 데이터 로드
            MyStudyPagingSource(studyHubApi)
        }.flow.cachedIn(viewModelScope) // 뷰모델 생명주기에 캐싱

}