package kr.co.gamja.study_hub.feature.mypage.engagedStudy

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
import kr.co.gamja.study_hub.data.model.ContentX
import kr.co.gamja.study_hub.data.repository.StudyHubApi

class EngagedStudyViewModel(studyHubApi: StudyHubApi) :ViewModel() {
    private val tag = this.javaClass.simpleName

    // 리스트 개수
    private val _listSize = MutableLiveData<Int>()
    val listSize: LiveData<Int> get() = _listSize

    // paging 초기화
    private val _reloadTrigger = MutableStateFlow(Unit)

    fun setReloadTrigger() {
        _reloadTrigger.value = Unit
    }
    // 리스트 있는지 여부
    var isList=MutableLiveData<Boolean>(true)

    // todo("참여 스터디 리스트 총 개수 가져오기")
    fun getEngagedStudy(){

    }

    val engagedStudyFlow : Flow<PagingData<ContentX>> = _reloadTrigger.flatMapLatest {
        Pager(
            PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            )
        ){
            EngagedStudyPagingSource(studyHubApi)
        }.flow.cachedIn(viewModelScope)
    }
}