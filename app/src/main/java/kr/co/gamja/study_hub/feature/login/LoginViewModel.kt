package kr.co.gamja.study_hub.feature.login

import android.util.Log
import androidx.lifecycle.ViewModel
import kr.co.gamja.study_hub.data.model.LoginRequest
import kr.co.gamja.study_hub.data.model.LoginResponse
import kr.co.gamja.study_hub.data.repository.RetrofitManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel :ViewModel(){

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