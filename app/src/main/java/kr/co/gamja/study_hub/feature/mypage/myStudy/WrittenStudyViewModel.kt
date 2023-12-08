package kr.co.gamja.study_hub.feature.mypage.myStudy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WrittenStudyViewModel :ViewModel() {
    private val tag = this.javaClass.simpleName

    // 리스트 개수
    private val _listSize = MutableLiveData<Int>()
    val listSize: LiveData<Int> get() = _listSize

    fun getMyStudy(){
        viewModelScope.launch {
            try {

            }catch (e :Exception){

            }
        }
    }
}