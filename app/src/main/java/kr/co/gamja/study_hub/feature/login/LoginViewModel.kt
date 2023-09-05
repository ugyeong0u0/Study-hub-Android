package kr.co.gamja.study_hub.feature.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.co.gamja.study_hub.data.model.LoginRequest
import kr.co.gamja.study_hub.data.model.LoginResponse
import kr.co.gamja.study_hub.data.repository.RetrofitManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
const val EMAIL ="^[a-zA-Z0-9+-\\_.]+(@inu\\.ac\\.kr)$"
const val PASSWORD="""^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#$%^&*])(?=.*[0-9!@#$%^&*]).{8,15}$"""
class LoginViewModel :ViewModel(){
    private val _loginEmail= MutableLiveData<String>()
    val loginEmail: LiveData<String> get() = _loginEmail

    private val _loginPassword = MutableLiveData<String>()
    val loginPassword: LiveData<String> get() = _loginPassword

    private val _validEmail=MutableLiveData<Boolean>()
    val validEmail: LiveData<Boolean> get() = _validEmail

    private val _validPassword=MutableLiveData<Boolean>()
    val validPassword: LiveData<Boolean> get() = _validPassword


    fun updateLoginEmail(newEmail:String){
        _loginEmail.value=newEmail
        _validEmail.value = newEmail.matches(EMAIL.toRegex())
    }
    fun updateLoginPassword(newPassword:String){
        _loginPassword.value=newPassword
        _validPassword.value=newPassword.matches(PASSWORD.toRegex())
    }

    fun goLogin(emailTxt:String,passwordTxt:String,params: LoginCallback){
        var loginReq = LoginRequest(emailTxt, passwordTxt)
        Log.d("로그인-request데이터", loginReq.toString())
        RetrofitManager.api.login(loginReq).enqueue(object: Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful){
                    val result=response.body() as LoginResponse
                    Log.d("로그인-로그인 성공 code", response.code().toString())
                    params.onSuccess(true, result.data.accessToken,result.data.refreshToken)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                val m= t.message.toString()
                Log.e("로그인 -통신 failure",m)
            }
        })
    }
}
interface LoginCallback{
fun onSuccess(isBoolean:Boolean=false,accessToken:String, refreshToken:String)
}