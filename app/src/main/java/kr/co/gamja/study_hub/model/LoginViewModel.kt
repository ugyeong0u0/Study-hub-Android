package kr.co.gamja.study_hub.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel :ViewModel(){
    // 로그인 여부
    private val _loginResult = MutableLiveData(false)
    val loginResult:LiveData<Boolean> get() = _loginResult

    fun login(){

    }
}