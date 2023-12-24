package kr.co.gamja.study_hub.feature.mypage.engagedStudy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EngagedStudyViewModel :ViewModel() {
    private val tag = this.javaClass.simpleName

    // 리스트 개수
    private val _listSize = MutableLiveData<Int>()
    val listSize: LiveData<Int> get() = _listSize

    // todo("참여 스터디 리스트 총 개수 가져오기")
    fun getEngagedStudy(){

    }
}