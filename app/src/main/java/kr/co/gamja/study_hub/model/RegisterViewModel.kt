package kr.co.gamja.study_hub.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.model.dto.*
import kr.co.gamja.study_hub.model.retrofit.RetrofitManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    private val _email = MutableLiveData("")
    val email: LiveData<String> = _email

    private val tag = this.javaClass.simpleName

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    // 이메일 인증번호 보내기
    fun emailSend() {
        val emailReq = EmailRequest(email.value!!)
        Log.d("회원가입  val emailReq =ApiRequest(txt_email)", "$emailReq")

        viewModelScope.launch {
            val response = RetrofitManager.api.email2(emailReq)
            if (response.isSuccessful) Log.d(tag, "회원가입 인증번호 보내기 성공")
            else Log.e(tag, "회원가입 인증번호 보내기 실패")
        }
    }

    // 이메일 인증번호 인증
   fun emailAuthcheck(authNumber:String, txt_email:String, params:EmailValidCallback) {
        val authNumberReq= EmailValidRequest(authNumber,txt_email)
        RetrofitManager.api.emailValid(authNumberReq).enqueue(object:Callback<EmailValidResponse>{
            override fun onResponse(
                call: Call<EmailValidResponse>,
                response: Response<EmailValidResponse>
            ) {
                if(response.isSuccessful){
                    val result=response.body() as EmailValidResponse
                    Log.d("회원가입-이메일 인증 성공", response.code().toString())
                    Log.d("회원가입 result.validResult",result.validResult.toString())
                    params.onEmailValidResult(result.validResult)
                }
            }
            override fun onFailure(call: Call<EmailValidResponse>, t: Throwable) {
                val m= t.message.toString()
                Log.e("회원가입-이메일 인증코드 에러",m)
                params.onEmailValidResult(false)
            }
        })

    }

    fun requestSignup(user: User, params: RegisterCallback){
        val signupReq= SignupRequest(
            User.email.toString(), User.gender.toString(),
            User.grade.toString(), User.nickname.toString(), User.password.toString()
        )
        Log.d("회원가입 요청시 데이터확인", signupReq.toString())
        RetrofitManager.api.signup(signupReq).enqueue(object:Callback<SignupResponse>{
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                if(response.isSuccessful){
                    Log.d("회원가입 레트로핏 성공", response.code().toString())
                    params.onSucess(true)
                }else{
                    Log.d("회원가입 레트로핏 error로 넘어감 ","")
                    Log.d("회원가입 레트로핏 error code", response.code().toString())
                    val errorResponse: RegisterErrorResponse?= response.errorBody()?.let {
                        val gson=Gson()
                        gson.fromJson(it.charStream(), RegisterErrorResponse::class.java)
                    }
                    if (errorResponse!=null){
                        val message=errorResponse.message.toString()
                        val status = errorResponse.status.toString()
                        params.onFail(true, status, message)
                    }
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                val m= t.message.toString()
                Log.e("회원가입 onfailure 에러",m)
            }

        })

    }
}
// 이메일 인증 체크 callback
interface EmailValidCallback{
    fun onEmailValidResult(isValid:Boolean)
}
// 회원가입 성공여부 callback
interface RegisterCallback{
    fun onSucess(isValid: Boolean=false)
    fun onFail(eIsValid: Boolean=false, eStatus:String, eMessage : String)
}