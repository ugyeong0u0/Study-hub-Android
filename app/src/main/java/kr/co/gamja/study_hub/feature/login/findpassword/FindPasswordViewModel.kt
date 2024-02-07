package kr.co.gamja.study_hub.feature.login.findpassword

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.EmailErrorResponse
import kr.co.gamja.study_hub.data.model.EmailRequest
import kr.co.gamja.study_hub.data.model.EmailValidRequest
import kr.co.gamja.study_hub.data.model.EmailValidResponse
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.CallBackListener
import kr.co.gamja.study_hub.data.repository.RetrofitManager
import kr.co.gamja.study_hub.feature.login.EMAIL

// 비밀번호 찾기 페이지 뷰모델
// todo(이메일 뒤로가기 시 초기화)
class FindPasswordViewModel : ViewModel() {

    val tag = this.javaClass.simpleName

    // ***이메일 입력 페이지 사용

    // 통신- 유저이메일, 이메일 인증번호 확인 페이지에서도 사용됨
    val userEmail = MutableLiveData<String>()

    // 버튼 클릭 enable
    private val _enableEmailBtn = MutableLiveData<Boolean>()
    val enableEmailBtn: LiveData<Boolean> get() = _enableEmailBtn

    // 유저 이메일이 되면 버튼 enable
    fun updateEnableEmailBtn() {
        _enableEmailBtn.value = userEmail.value.toString().matches(EMAIL.toRegex())
    }

    // *** 이메일 Auth 번호
    // 통신- 유저인증번호
    val userAuth = MutableLiveData<String>()

    // 버튼 클릭 enable
    private val _enableAuthBtn = MutableLiveData<Boolean>()
    val enableAuthBtn: LiveData<Boolean> get() = _enableAuthBtn


    // 유저 이메일이 되면 버튼 enable
    fun updateEnableAuthBtn() {
        _enableAuthBtn.value = userAuth.value.toString().isNotEmpty()
    }

    // 이메일 초기화
    fun initUserEmail() {
        userEmail.value = ""
    }
    // 인증번호 초기화
    fun initUserAuth() {
       userAuth.value = ""
    }
    // 이메일 인증 중 화면 전환시 비동기로 하면 안됨(프레그먼트 붙여지기전임)
    val emailVerificationResult = MutableLiveData<Boolean>()
    val emailVerificationResultTwo = MutableLiveData<Boolean>()

    // 이메일 보낼때 비동기함수 끝났는지 여부 변수 초기화
    fun initEmailVerification(){
        emailVerificationResult.value=false
    }
    // emailVerificationResult.value false시 observe때문에 놓음
    fun initEmailVerificationTwo(result :Boolean){
        emailVerificationResultTwo.value=result
    }


    // 비밀번호 찾기 이메일로 인증번호 받는 api
    fun emailForFindPassword() {
        val emailReq = EmailRequest(userEmail.value.toString())
        Log.d(tag, "비밀번호 찾기 이메일인증 $emailReq")
        // todo(로그인 화면에서 오는 경우 이상함 )
        viewModelScope.launch {
            try {
//                Log.e(tag,"이메일 인증시작")
                val response = AuthRetrofitManager.api.emailPassword(emailReq)
                if (response.isSuccessful) {
//                    Log.e(tag,"이메일 인증끝 response.isSuccessful안 ")
                    Log.d(tag, "비번찾기-이메일 인증번호 보내기 성공")
                    emailVerificationResult.postValue( true)
                    emailVerificationResultTwo.postValue(true)
                } else {
                    emailVerificationResult.postValue( false)
                    emailVerificationResultTwo.postValue(false)
                    Log.e(tag, "비번찾기-이메일 인증번호 보내기 실패")
                    val errorResponse: EmailErrorResponse? = response.errorBody()?.let {
                        val gson = Gson()
                        gson.fromJson(it.charStream(), EmailErrorResponse::class.java)
                    }
                    if (errorResponse != null) {
                        val status = errorResponse.status
                        Log.d(tag, status)
                    }

                }
            } catch (e: Exception) {
                Log.e(tag, "비번찾기-이메일 Exception: ${e.message}")
                emailVerificationResult.postValue( false)
                emailVerificationResultTwo.postValue(false)
            }
        }
    }

    // 이메일 인증코드 검증
    fun authForFindPassword(params: CallBackListener) {
        val authReq = EmailValidRequest(userAuth.value.toString(), userEmail.value.toString())
        Log.d(tag, "비밀번호 찾기 auth인증 $authReq")

        viewModelScope.launch {
            try {
                val response = RetrofitManager.api.emailValid(authReq)
                if (response.isSuccessful) {
                    val result = response.body() as EmailValidResponse
                    if (result.validResult) {
                        Log.d(tag, "비번찾기-이메일 인증번호 맞음")
                        params.isSuccess(true)
                    } else {
                        Log.d(tag, "비번찾기-이메일 인증번호 틀림")
                        params.isSuccess(false)
                    }

                }
            } catch (e: Exception) {
                Log.e(tag, "비번찾기-이메일 인증 Exception: ${e.message}")
                params.isSuccess(false)
            }
        }
    }


}