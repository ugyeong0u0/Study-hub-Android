package kr.co.gamja.study_hub.feature.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NicknameViewModel :ViewModel(){
    private val tag = this.javaClass.simpleName
    // TODO("닉네임 중복 api)
    // 닉네임
    private val _nickname = MutableLiveData<String>()
    val nickname: LiveData<String> get()=_nickname

}