package kr.co.gamja.study_hub.feature.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.*
import kr.co.gamja.study_hub.data.repository.RetrofitManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    // 이메일
    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    // 인증 번호
    private val _authNumber = MutableLiveData<String>()
    val authNumber: LiveData<String> get() = _authNumber

    private val tag = this.javaClass.simpleName

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun updateAuthNumber(newAuthNumber: String) {
        _authNumber.value = newAuthNumber
    }

    // 이메일 인증번호 보내기
    fun emailSend() {
        val emailReq = EmailRequest(_email.value.toString())
        Log.d(tag, "회원가입 $emailReq")

        viewModelScope.launch {
            try {
                val response = RetrofitManager.api.email(emailReq)
                if (response.isSuccessful) Log.d(tag, "회원가입 인증번호 보내기 성공")
                else Log.e(tag, "회원가입 인증번호 보내기 실패")
            }catch (e:Exception){
                Log.e(tag, "회원가입 Exception: ${e.message}")
            }

        }
    }

    // 이메일 인증번호 인증
    fun emailAuthcheck(params: EmailValidCallback) {
        val authNumberReq =
            EmailValidRequest(_authNumber.value.toString(), _email.value.toString()!!)
        viewModelScope.launch {
            try {
                val response = RetrofitManager.api.emailValid(authNumberReq)
                if (response.isSuccessful) {
                    val result = response.body() as EmailValidResponse
                    Log.d(tag, "회원가입-이메일 인증 성공"+response.code().toString())
                    Log.d(tag, "회원가입 result.validResult"+result.validResult.toString())
                    params.onEmailValidResult(result.validResult)
                } else {
                    Log.e(tag, "회원가입-이메일 인증코드 에러")
                    params.onEmailValidResult(false)
                }
            }catch(e:Exception){
                Log.e(tag,"회원가입 Exception: ${e.message}")
            }

        }
    }

    fun requestSignup(user: User, params: RegisterCallback) {
        val signupReq = SignupRequest(
            "koung0706@inu.ac.kr", User.gender!!,
            User.grade!!, User.nickname!!, User.password!!)
        Log.d(tag,"회원가입 요청시 데이터확인"+ signupReq.toString())

        viewModelScope.launch {
            try {
                val response = RetrofitManager.api.signup(signupReq)
                if (response.isSuccessful) {
                    Log.d(tag,"회원가입 성공"+ response.code().toString())
                    params.onSucess(true)
                } else {
                    Log.d(tag,"회원가입 error code"+ response.code().toString())
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
            }catch (e:Exception){
                Log.e(tag,"회원가입 Exception: ${e.message}")
            }

        }
    }
}

// 이메일 인증 체크 callback
interface EmailValidCallback {
    fun onEmailValidResult(isValid: Boolean)
}

// 회원가입 성공여부 callback
interface RegisterCallback {
    fun onSucess(isValid: Boolean = false)
    fun onFail(eIsValid: Boolean = false, eStatus: String, eMessage: String)
}