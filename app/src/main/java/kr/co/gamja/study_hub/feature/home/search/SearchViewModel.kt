package kr.co.gamja.study_hub.feature.home.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel: ViewModel() {

    val tag = this.javaClass.simpleName
    // editText 입력 단어 - 통신 보낼 변수
    val searchWord = MutableLiveData<String>()
    private val _searchImg =MutableLiveData<Boolean>(true)
    val searchImg:LiveData<Boolean> get() =_searchImg
    // 텍스트 지우기 검색 이미지 활성화
    fun updateSearchImg(){
        _searchImg.value = searchWord.value.toString().isEmpty()
    }

}