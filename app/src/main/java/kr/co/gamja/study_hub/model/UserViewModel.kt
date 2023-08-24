package kr.co.gamja.study_hub.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel :ViewModel(){
    // 로그인 여부
    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult:LiveData<Boolean> =_loginResult
}