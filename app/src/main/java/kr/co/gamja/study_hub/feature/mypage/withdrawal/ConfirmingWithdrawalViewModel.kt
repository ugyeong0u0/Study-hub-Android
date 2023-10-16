package kr.co.gamja.study_hub.feature.mypage.withdrawal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.co.gamja.study_hub.feature.login.PASSWORD

class ConfirmingWithdrawalViewModel: ViewModel() {
    val tag = this.javaClass.simpleName
    // 통신- 비번 값
    val currentPassword = MutableLiveData<String>()

    // 버튼 클릭 enable
    private val _enableBtn = MutableLiveData<Boolean>()
    val enableBtn: LiveData<Boolean> get() = _enableBtn

    // 비번 입력이 되면 버튼 enable
    fun updateEnableBtn() {
        if (currentPassword.value.toString().matches(PASSWORD.toRegex())) {
            _enableBtn.value = true
        }
    }
}