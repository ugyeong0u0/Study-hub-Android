package kr.co.gamja.study_hub.feature.mypage.aboutstudyhub

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
// 문의하기 .... 공용 뷰모델
class AboutStudyHubViewModel: ViewModel() {

    val tag= this.javaClass.simpleName

    // 제목 editText
    val title = MutableLiveData<String>()

    // 내용 editText
    val content = MutableLiveData<String>()

    // 제목 editText
    val email = MutableLiveData<String>()

    // 버튼 enable
    private val _btnEnable = MutableLiveData<Boolean>()
    val btnEnable: LiveData<Boolean> get() = _btnEnable

    // todo("이메일 검사 등 하기")
    // 버튼 enable 변경 함수
    fun updateBtn(result : Boolean){
        _btnEnable.value=result
    }
    // 문의하기 내용 초기화
    fun initComplaint(){
        title.value=""
        content.value=""
        email.value=""
    }

}