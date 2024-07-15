package kr.co.gamja.study_hub.feature.signup.email

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.EmailErrorResponse
import kr.co.gamja.study_hub.data.model.EmailRequest
import kr.co.gamja.study_hub.data.model.EmailValidRequest
import kr.co.gamja.study_hub.data.model.EmailValidResponse
import kr.co.gamja.study_hub.data.repository.CallBackListener
import kr.co.gamja.study_hub.data.repository.RetrofitManager
import kr.co.gamja.study_hub.feature.login.EMAIL

class EmailViewModel : ViewModel() {
    private val tag = this.javaClass.simpleName

    // 이메일
    private val _email = MutableLiveData<String>()
    val email:LiveData<String>  get()=_email

    // 이메일, 인증코드 형식 확인
    private val _validEmail = MutableLiveData<Boolean>()
    val validEmail: LiveData<Boolean> get() = _validEmail
    fun setEmail(newEmail: String) {
        _email.value = newEmail
        _validEmail.value = newEmail.matches(EMAIL.toRegex())
    }

    // 다음 버튼 활성화
    private val _nextBtn = MutableLiveData<Boolean>(false)
    val nextBtn:LiveData<Boolean> get()=_nextBtn

    fun setEnableNextBtn( newValue : Boolean){
        _nextBtn.value= newValue
    }


    // 인증 번호
    private val _authNumber = MutableLiveData<String>()
    val authNumber:LiveData<String> get()=_authNumber

    fun setAuthNumber(newAuthNumber: String) {
        _authNumber.value = newAuthNumber
        _validAuthNumber.value= newAuthNumber.length==8
    }

    private val _validAuthNumber = MutableLiveData<Boolean>()
    val validAuthNumber: LiveData<Boolean> get() = _validAuthNumber

    // 이메일 중복확인
    fun checkEmail(params: CallBackListener){
        val emailReq = EmailRequest(email.value.toString())
        viewModelScope.launch {
            val response=RetrofitManager.api.checkEmailDuplication(emailReq)
            if(response.isSuccessful){
                params.isSuccess(true)
            }else{
                val errorResponse: EmailErrorResponse?=response.errorBody()?.let {
                    val gson = Gson()
                    gson.fromJson(it.charStream(), EmailErrorResponse::class.java)
                }
                if(errorResponse!=null){
                    val status= errorResponse.status
                    Log.d(tag,status)
                    params.isSuccess(false)
                }
            }
        }
    }

    // 이메일 인증번호 보내기
    fun emailSend(params: CallBackListener) {
        val emailReq = EmailRequest(email.value.toString())
        Log.d(tag, "회원가입 $emailReq")

        viewModelScope.launch {
            try {
                val response = RetrofitManager.api.email(emailReq)
                if (response.isSuccessful) {
                    Log.d(tag, "회원가입 인증번호 보내기 성공")
                    params.isSuccess(true)
                } else {
                    Log.e(tag, "회원가입 인증번호 보내기 실패")
                    val errorResponse: EmailErrorResponse?=response.errorBody()?.let {
                        val gson = Gson()
                        gson.fromJson(it.charStream(), EmailErrorResponse::class.java)
                    }
                    if(errorResponse!=null){
                        val status= errorResponse.status
                        Log.d(tag,status)
                        params.isSuccess(false)
                    }

                }
            } catch (e: Exception) {
                Log.e(tag, "회원가입 Exception: ${e.message}")
            }

        }
    }

    // 이메일 인증번호 인증
    fun emailAuthcheck(params: CallBackListener) {
        val authNumberReq =
            EmailValidRequest(authNumber.value.toString(), email.value.toString())
        viewModelScope.launch {
            try {
                val response = RetrofitManager.api.emailValid(authNumberReq)
                if (response.isSuccessful) {
                    val result = response.body() as EmailValidResponse
                    Log.d(tag, "회원가입-이메일 인증 성공" + response.code().toString())
                    Log.d(tag, "회원가입 result.validResult" + result.validResult.toString())
                    params.isSuccess(result.validResult)
                } else {
                    Log.e(tag, "회원가입-이메일 인증코드 에러")
                    params.isSuccess(false)
                }
            } catch (e: Exception) {
                Log.e(tag, "회원가입 Exception: ${e.message}")
            }

        }
    }

}

