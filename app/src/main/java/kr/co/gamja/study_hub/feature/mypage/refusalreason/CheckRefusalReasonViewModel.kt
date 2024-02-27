package kr.co.gamja.study_hub.feature.mypage.refusalreason

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager

class CheckRefusalReasonViewModel : ViewModel() {
    val tag = this.javaClass.simpleName

    // 거절 이유
    private val _reason = MutableLiveData<String>()
    val reason: LiveData<String> get() = _reason
    // 거절 이유
    private val _title = MutableLiveData<String>()
    val title: LiveData<String> get() = _title


    fun getRefusalReasonAboutStudy(studyId: Int) {
        viewModelScope.launch {
            val response = AuthRetrofitManager.api.getDenyReason(studyId)
            try {
                if (response.isSuccessful) {
                    val result = response.body()
                    _reason.value=result?.reason
                    _title.value= result?.studyTitle
                }
            } catch (e: Exception) {
                Log.e(tag, "거절 사유 조회 실패" + response.code().toString())
            }
        }
    }

}