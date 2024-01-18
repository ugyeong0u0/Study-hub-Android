package kr.co.gamja.study_hub.feature.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.LoginErrorResponse
import kr.co.gamja.study_hub.data.model.LoginRequest
import kr.co.gamja.study_hub.data.model.LoginResponse
import kr.co.gamja.study_hub.data.repository.RetrofitManager

const val EMAIL = "^[a-zA-Z0-9+-\\_.]+(@inu\\.ac\\.kr)$"
const val PASSWORD = """^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{10,15}$"""

class LoginViewModel : ViewModel() {
    private val tag = this.javaClass.simpleName

    private val _loginEmail = MutableLiveData<String>()
    val loginEmail: LiveData<String> get() = _loginEmail

    private val _loginPassword = MutableLiveData<String>()
    val loginPassword: LiveData<String> get() = _loginPassword

    private val _validEmail = MutableLiveData<Boolean>()
    val validEmail: LiveData<Boolean> get() = _validEmail

    private val _validPassword = MutableLiveData<Boolean>()
    val validPassword: LiveData<Boolean> get() = _validPassword

    // 자동 로그인-> refreshToken 유효한지
    private val _isValidAutoLogin = MutableLiveData<Boolean>()
    val isValidAutoLogin: LiveData<Boolean> get() = _isValidAutoLogin


    fun updateLoginEmail(newEmail: String) {
        _loginEmail.value = newEmail
        _validEmail.value = newEmail.matches(EMAIL.toRegex())
    }

    fun updateLoginPassword(newPassword: String) {
        _loginPassword.value = newPassword
        _validPassword.value = newPassword.matches(PASSWORD.toRegex())
    }


    fun goLogin(emailTxt: String, passwordTxt: String, params: LoginCallback) {
        val loginReq = LoginRequest(emailTxt, passwordTxt)
        Log.d(tag, "로그인-request데이터" + loginReq.toString())
        viewModelScope.launch {
            try {
                val response = RetrofitManager.api.login(loginReq)
                if (response.isSuccessful) {
                    val result = response.body() as LoginResponse
                    Log.d(tag, "로그인 성공 code" + response.code().toString())
                    params.onSuccess(true, result.accessToken, result.refreshToken)
                } else {
                    params.onfail(true)
                    Log.e(tag, "로그인 실패")
                    val errorResponse: LoginErrorResponse? = response.errorBody()?.let {
                        val gson = Gson()
                        gson.fromJson(it.charStream(), LoginErrorResponse::class.java)
                    }
                    if (errorResponse != null) {
                        val status = errorResponse.status
                        Log.e(tag, status.toString())
                    }
                }
            } catch (e: Exception) {
                Log.e(tag, "로그인 Exception: ${e.message}")
            }

        }
    }
}

interface LoginCallback {
    fun onSuccess(isBoolean: Boolean = false, accessToken: String, refreshToken: String)
    fun onfail(isBoolean: Boolean = false)
}
