package kr.co.gamja.study_hub.feature.login.findpassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.co.gamja.study_hub.feature.login.EMAIL

// 비밀번호 찾기 페이지 뷰모델
class FindPasswordViewModel : ViewModel() {

    val tag= this.javaClass.simpleName

    // ***이메일 입력 페이지 사용

    // 통신- 유저이메일
    val userEmail = MutableLiveData<String>()

    // 버튼 클릭 enable
    private val _enableEmailBtn = MutableLiveData<Boolean>()
    val enableEmailBtn: LiveData<Boolean> get() = _enableEmailBtn

    // 유저 이메일이 되면 버튼 enable
    fun updateEnableEmailBtn() {
        if (userEmail.value.toString().matches(EMAIL.toRegex())) {
            _enableEmailBtn.value = true
        }
    }
    // *** 이메일 Auth 번호
    // 통신- 유저이메일
    val userAuth = MutableLiveData<String>()

    // 버튼 클릭 enable
    private val _enableAuthBtn = MutableLiveData<Boolean>()
    val enableAuthBtn: LiveData<Boolean> get() = _enableAuthBtn

    // 유저 이메일이 되면 버튼 enable
    fun updateEnableAuthBtn() {
        if (userAuth.value.toString().isNotEmpty()) {
            _enableAuthBtn.value = true
        }
    }


}