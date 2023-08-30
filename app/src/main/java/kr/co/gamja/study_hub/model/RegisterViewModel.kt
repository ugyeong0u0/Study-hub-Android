package kr.co.gamja.study_hub.model


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.co.gamja.study_hub.RetrofitManager
import kr.co.gamja.study_hub.StudyHubApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RegisterViewModel : ViewModel() {
    //이메일 인증 여부
    private val _emailValudation = MutableLiveData(false)
    val emailValidation: LiveData<Boolean> get() = _emailValudation


    // 이메일 인증번호 보내기
    fun emailSend(txt_email:String) {
        val emailReq =ApiRequest(txt_email)
        Log.d("회원가입  val emailReq =ApiRequest(txt_email)","$emailReq")

        RetrofitManager.api.email(emailReq).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                Log.d("회원가입 인증번호 보내기 성공",response.code().toString())
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                t.stackTrace
                Log.d("회원가입 인증번호 보내기 실패", "")
            }
        })
    }

    // 이메일 인증번호 인증
    fun emailAuthcheck(authNumber:String, txt_email:String) {
        val authNumberReq=EmailValidRequest(authNumber,txt_email)
        RetrofitManager.api.emailValid(authNumberReq).enqueue(object:Callback<EmailValidResponse>{
            override fun onResponse(
                call: Call<EmailValidResponse>,
                response: Response<EmailValidResponse>
            ) {
                if(response.isSuccessful){
                    var result=response.body() as EmailValidResponse
                    _emailValudation.value=result.validResult
                    Log.d("회원가입-이메일 인증", response.code().toString())
                }
            }
            override fun onFailure(call: Call<EmailValidResponse>, t: Throwable) {
                t.stackTrace
                Log.d("회원가입-이메일 인증 에러","")
            }
        })

    }
}