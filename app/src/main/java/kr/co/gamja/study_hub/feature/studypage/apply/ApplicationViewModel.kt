package kr.co.gamja.study_hub.feature.studypage.apply

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.co.gamja.study_hub.data.repository.CallBackListener

class ApplicationViewModel: ViewModel() {

    // 내용
    val applyEditText = MutableLiveData<String>()

    // 내용 길이
    private val _applyEditTextLength = MutableLiveData<Int>(0)
    val applyEditTextLength: LiveData<Int> get() = _applyEditTextLength

    // 버튼 클릭 enable
    private val _enableBtn = MutableLiveData<Boolean>()
    val enableBtn: LiveData<Boolean> get() = _enableBtn


    // 내용 길이 구독
    init{
        applyEditText.observeForever { text->
            text?.let {
                _applyEditTextLength.value = it.length
                _enableBtn.value = it.isNotEmpty()
            }
        }
    }
    // todo("신청하기 api")
    fun applyStudy(params:CallBackListener){


    }

}