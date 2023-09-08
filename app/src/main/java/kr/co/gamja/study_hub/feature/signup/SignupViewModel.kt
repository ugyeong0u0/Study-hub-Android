package kr.co.gamja.study_hub.feature.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.RegisterErrorResponse
import kr.co.gamja.study_hub.data.model.SignupRequest
import kr.co.gamja.study_hub.data.repository.RetrofitManager

class SignupViewModel : ViewModel() {
    private val tag = this.javaClass.simpleName

    fun requestSignup(user: User, params: RegisterCallback) {
        val signupReq = SignupRequest(
            "koung0706@inu.ac.kr", User.gender!!,
            User.grade!!, User.nickname!!, User.password!!
        )
        Log.d(tag, "회원가입 요청시 데이터확인" + signupReq.toString())

        viewModelScope.launch {
            try {
                val response = RetrofitManager.api.signup(signupReq)
                if (response.isSuccessful) {
                    Log.d(tag, "회원가입 성공" + response.code().toString())
                    params.onSucess(true)
                } else {
                    Log.d(tag, "회원가입 error code" + response.code().toString())
                    val errorResponse: RegisterErrorResponse? = response.errorBody()?.let {
                        val gson = Gson()
                        gson.fromJson(it.charStream(), RegisterErrorResponse::class.java)
                    }
                    if (errorResponse != null) {
                        val message = errorResponse.message.toString()
                        val status = errorResponse.status.toString()
                        params.onFail(true, status, message)
                    }
                }
            } catch (e: Exception) {
                Log.e(tag, "회원가입 Exception: ${e.message}")
            }

        }
    }
}

// 회원가입 성공여부 callback
interface RegisterCallback {
    fun onSucess(isValid: Boolean = false)
    fun onFail(eIsValid: Boolean = false, eStatus: String, eMessage: String)
}