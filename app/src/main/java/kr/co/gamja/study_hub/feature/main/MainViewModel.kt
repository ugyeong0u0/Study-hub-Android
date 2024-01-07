package kr.co.gamja.study_hub.feature.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.AccessTokenErrorResponse
import kr.co.gamja.study_hub.data.model.AccessTokenRequest
import kr.co.gamja.study_hub.data.model.AccessTokenResponse
import kr.co.gamja.study_hub.data.repository.RetrofitManager
import kr.co.gamja.study_hub.feature.login.LoginCallback

class MainViewModel: ViewModel() {
    private val tag = this.javaClass.simpleName

    fun autoLogin(refreshToken: String, params: LoginCallback) {
        val refreashValidReq = AccessTokenRequest(refreshToken)
        Log.d(tag, "자동로그인 refresh토큰 $refreshToken")
        viewModelScope.launch {
            try {
                val response = RetrofitManager.api.accessTokenIssued(refreashValidReq)
                if (response.isSuccessful) {
                    val result = response.body() as AccessTokenResponse
                    params.onSuccess(true, result.accessToken, result.refreshToken)
                } else {
                    Log.e(tag, "레트로핏 안 refresh토큰 만료")
                    val errorResponse: AccessTokenErrorResponse? = response.errorBody()?.let {
                        val gson = Gson()
                        gson.fromJson(it.charStream(), AccessTokenErrorResponse::class.java)
                    }
                    if (errorResponse != null) {
                        val message = errorResponse.message
                        Log.e(tag, message)
                        params.onfail(true)
                    }
                }
            } catch (e: Exception) {
                Log.e(tag, "자동 로그인 Exception: ${e.message}")
            }
        }
    }

}