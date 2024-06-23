package kr.co.gamja.study_hub.feature.signup.nickname

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.ErrorResponse
import kr.co.gamja.study_hub.data.repository.RetrofitManager

class NicknameViewModel :ViewModel(){
    private val tag = this.javaClass.simpleName
    // 통신 - 닉네임
    val nickname = MutableLiveData<String>()

    // 닉네임 길이
    private val _nicknameLength = MutableLiveData<Int>(0)
    val nicknameLength: LiveData<Int> get()=_nicknameLength

    // 닉네임 길이 update함수
    fun setNicknameLength(length :Int){
        _nicknameLength.value=length
    }

    // 닉네임 editText 에러문구 표시여부
    private val _nicknameError = MutableLiveData<Boolean?>(null)
    val nicknameError: LiveData<Boolean?> get()=_nicknameError

    // 다음 버튼 활성화
    private val _nextBtn = MutableLiveData<Boolean>(false)
    val nextBtn: LiveData<Boolean> get()=_nextBtn

    fun setEnableNextBtn( newValue : Boolean){
        _nextBtn.value= newValue
    }


    fun isDuplicationNickname() {
        viewModelScope.launch {
            val response =
                RetrofitManager.api.checkNicknameDuplication(nickname = nickname.value.toString())
            if (response.isSuccessful) {
                _nicknameError.value=false
            } else {

                val errorResponse: ErrorResponse? = response.errorBody()?.let {
                    val gson = Gson()
                    gson.fromJson(it.charStream(), ErrorResponse::class.java)
                }
                if (errorResponse != null) {
                    Log.d(tag, errorResponse.status)
                    _nicknameError.value=true
                }
            }
        }
    }
}