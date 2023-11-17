package kr.co.gamja.study_hub.feature.mypage.currentPassword

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.CurrentPasswordErrorResponse
import kr.co.gamja.study_hub.data.model.CurrentPasswordRequest
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.CallBackListener
import kr.co.gamja.study_hub.feature.login.PASSWORD

class CurrentPasswordViewModel : ViewModel() {
    val tag = this.javaClass.simpleName

    // 통신- 현재 비번 값
    val currentPassword = MutableLiveData<String>()

    // 버튼 클릭 enable
    private val _enableBtn = MutableLiveData<Boolean>()
    val enableBtn: LiveData<Boolean> get() = _enableBtn

    // 비번 입력이 되면 버튼 enable
    fun updateEnableBtn() {
        if (currentPassword.value.toString().matches(PASSWORD.toRegex())) {
            _enableBtn.value = true
        }
    }

    // 현재 비번 확인 api
    fun isCurrentPasswordValid(currentPass: String, params: CallBackListener) {
        val req = CurrentPasswordRequest(currentPass)
        viewModelScope.launch {
            val response = AuthRetrofitManager.api.postCurrentPassword(req)
            if (response.isSuccessful) {
                params.isSuccess(true)
            } else {
                val errorResponse: CurrentPasswordErrorResponse? = response.errorBody()?.let {
                    val gson = Gson()
                    gson.fromJson(it.charStream(), CurrentPasswordErrorResponse::class.java)
                }
                if (errorResponse != null) {
                    Log.e(tag, errorResponse.message)
                    params.isSuccess(false)
                }
            }
        }

    }
}