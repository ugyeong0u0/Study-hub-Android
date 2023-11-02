package kr.co.gamja.study_hub.feature.mypage.newPassword

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.ErrorResponse
import kr.co.gamja.study_hub.data.model.NewPasswordRequest
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.CallBackListener
import kr.co.gamja.study_hub.feature.login.PASSWORD

class NewPasswordViewModel : ViewModel() {
    private val tag = this.javaClass.simpleName

    // 첫 번째 비번값
    val password = MutableLiveData<String>()

    // 첫번째 비번 error
    private val _errorPassword = MutableLiveData<Boolean?>()
    val errorPassword: LiveData<Boolean?> get() = _errorPassword

    // 두 번째 비번값
    val rePassword = MutableLiveData<String>()

    // 두 번째 비번 error - true: 초록, false : 빨강, 다음 버튼 활성화에도 쓰임
    private val _errorRePassword = MutableLiveData<Boolean?>()
    val errorRePassword: LiveData<Boolean?> get() = _errorRePassword


    // 완료 버튼 enable
    fun updateEnableBtn() {
        if (password.value.toString().matches(PASSWORD.toRegex())) { // 1번 비번부터 입력된 경우
            _errorPassword.value = true
            if (rePassword.value.isNullOrEmpty()) _errorRePassword.value = null // 2번 비번이 빈 경우
            else _errorRePassword.value = password.value.toString() == rePassword.value.toString()
        } else if (password.value.isNullOrEmpty() && rePassword.value.isNullOrEmpty()) { // 1번 2번 둘다 빈 경우(초기)
            _errorPassword.value = null
            _errorRePassword.value = null
        } else {
            _errorPassword.value = false
            _errorRePassword.value = false
        }
    }

    fun changePassword(auth: Boolean, params: CallBackListener) {
        val req = NewPasswordRequest(auth, rePassword.value.toString())
        Log.d(tag, req.toString())
        viewModelScope.launch {
            val response = AuthRetrofitManager.api.putNewPassword(req)
            if (response.isSuccessful) {
                params.isSuccess(true)
            } else {
                val errorResponse: ErrorResponse? = response.errorBody()?.let {
                    val gson = Gson()
                    gson.fromJson(it.charStream(), ErrorResponse::class.java)
                }
                if (errorResponse != null) {
                    Log.e(tag, errorResponse.message)
                    params.isSuccess(false)
                }
            }
        }
    }

}