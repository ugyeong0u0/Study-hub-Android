package kr.co.gamja.study_hub.feature.signup.agreement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AgreementViewModel : ViewModel() {

    val tag = this.javaClass.simpleName

    // 전체 동의
    private val _allConsent = MutableLiveData<Boolean>()
    val allConsent: LiveData<Boolean> get() = _allConsent

    fun updateAllConsent(value : Boolean){
        _allConsent.value=value
        _serviceConsent.value=value
        _infoConsent.value=value
    }
    // 옵저버에서 updateAllConstent사용하면 계속 값이 업데이트됨에 따라 무한 참조 발생해서 따로 하나 더 만듦
    fun updateOnlyAllConsent(value: Boolean){
        _allConsent.value=value
    }



    // 서비스 이용약관 동의
    private val _serviceConsent = MutableLiveData<Boolean>()
    val serviceContent: LiveData<Boolean> get() = _serviceConsent

    fun updateServiceContent(value : Boolean){
        _serviceConsent.value=value
    }

    // 개인정보 수집 동의
    private val _infoConsent = MutableLiveData<Boolean>()
    val infoConsent: LiveData<Boolean> get() = _infoConsent

    fun updateInfoConsent(value : Boolean){
        _infoConsent.value=value
    }


}