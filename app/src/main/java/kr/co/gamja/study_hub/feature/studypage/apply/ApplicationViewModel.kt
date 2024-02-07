package kr.co.gamja.study_hub.feature.studypage.apply

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.CallBackListener

class ApplicationViewModel: ViewModel() {

    val tag = this.javaClass.simpleName
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
    fun applyStudy(studyId:Int,params:CallBackListener){
        viewModelScope.launch {
            try {
                val response = AuthRetrofitManager.api.applyStudy(applyEditText.value.toString(),studyId )
                if(response.isSuccessful){
                    params.isSuccess(true)
                }else{
                    params.isSuccess(false)
                }
            }catch  (e : Exception){
                Log.e(tag, "스터디 신청 Exception: ${e.message}")
                Log.e(tag, "스터디 신청 Exception: ${e.printStackTrace()}")
            }
        }

    }

}