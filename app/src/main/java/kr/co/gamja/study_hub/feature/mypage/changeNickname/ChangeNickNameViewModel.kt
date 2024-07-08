package kr.co.gamja.study_hub.feature.mypage.changeNickname

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.ChangeNicknameRequest
import kr.co.gamja.study_hub.data.model.DuplicationNicknameErrorResponse
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.CallBackListener
import kr.co.gamja.study_hub.data.repository.RetrofitManager

class ChangeNickNameViewModel : ViewModel() {
    val tag = this.javaClass.simpleName

    // 통신- 닉네임 값
    private val _nickname = MutableLiveData<String>()
    val nickname : LiveData<String> get() = _nickname

    fun updateNickname(newNickname: String) {

        _nickname.value = newNickname
    }

    fun filterText(text: String): String {
        val builder = StringBuilder()
        val pattern = Regex("^[a-zA-Z\\dㄱ-ㅎ가-힣]*$")

        text.forEach { char ->
            val type = Character.getType(char)
            if (char.toString().matches(pattern)) {
                builder.append(char)
            } else if (type != Character.SURROGATE.toInt() && type != Character.OTHER_SYMBOL.toInt() && char != ' ') {
                // 영어, 숫자, 한글 외의 문자를 무시하고 추가하지 않음
                builder.append("")
            }
        }
        return builder.toString()
    }


    // 닉네임 입력 길이
    private val _nicknameLength = MutableLiveData<Int>(0)
    val nicknameLength: LiveData<Int> get() = _nicknameLength

    // 닉네임 길이를 업데이트
    fun updateNicknameLength(length: Int) {
        _nicknameLength.value = length
        if (nicknameLength.value!! > 1) {
            _enableBtn.value = true
        }
    }

    // 버튼 클릭 enable
    private val _enableBtn = MutableLiveData<Boolean>()
    val enableBtn: LiveData<Boolean> get() = _enableBtn

    // 닉네임 중복 검사
    fun isDuplicationNickname(params: CallBackListener) {
        viewModelScope.launch {
            val response =
                RetrofitManager.api.checkNicknameDuplication(nickname = nickname.value.toString())
            if (response.isSuccessful) {
                params.isSuccess(true)
            } else {
                Log.e(tag, "닉네임 중복검사 에러처리로 ")
                val errorResponse: DuplicationNicknameErrorResponse? = response.errorBody()?.let {
                    val gson = Gson()
                    gson.fromJson(it.charStream(), DuplicationNicknameErrorResponse::class.java)
                }
                if (errorResponse != null) {
                    Log.d(tag, errorResponse.status)
                    params.isSuccess(false)
                }
            }
        }
    }

    // 닉네임 변경 api
    fun putChangedNickname(params: CallBackListener) {
        val req=ChangeNicknameRequest(nickname.value.toString())
        viewModelScope.launch {
            val response=AuthRetrofitManager.api.putNewNickname(req)
            if (response.isSuccessful){
                Log.d(tag, "닉네임 수정 성공 ${response.code()}")
                params.isSuccess(true)
            }else{
                Log.e(tag, "닉네임수정api 에러 ${response.code()} ")
            }
        }

    }

}
