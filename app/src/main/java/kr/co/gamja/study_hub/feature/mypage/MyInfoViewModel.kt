package kr.co.gamja.study_hub.feature.mypage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.UsersErrorResponse
import kr.co.gamja.study_hub.data.model.UsersResponse
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager

class MyInfoViewModel : ViewModel() {
    private val tag = this.javaClass.simpleName

    private val _emailData = MutableLiveData<String>()
    val emailData: LiveData<String> get() = _emailData

    private val _nicknameData = MutableLiveData<String>()
    val nicknameData: LiveData<String> get() = _nicknameData

    // 회원 비회원 여부
    private val _isNicknameData = MutableLiveData<Boolean>()
    val isNicknameData: LiveData<Boolean> get() = _isNicknameData

    private val _majorData = MutableLiveData<String>()
    val majorData: LiveData<String> get() = _majorData

    // 회원 비회원 여부
    private val _isMajorData = MutableLiveData<Boolean>()
    val isMajorData: LiveData<Boolean> get() = _isMajorData

    private val _genderData = MutableLiveData<String>()
    val genderData: LiveData<String> get() = _genderData

    private val _imgData = MutableLiveData<Any>()
    val imgData: LiveData<Any> get() = _imgData

    // 회원 비회원 여부
    private val _isImgData = MutableLiveData<Boolean>()
    val isImgData: LiveData<Boolean> get() = _isImgData

    private lateinit var onClickListener: MyInfoCallbackListener
    fun setOnClickListener(listener: MyInfoCallbackListener) {
        onClickListener = listener
    }
    // 초기화
    fun init(){
        _isImgData.value=false
        _isMajorData.value=false
        _isNicknameData.value=false
    }

    // 회원조회
    fun getUsers() {
        viewModelScope.launch {
            try {
                val response = AuthRetrofitManager.api.getUserInfo()
                if (response.isSuccessful) {
                    val result = response.body() as UsersResponse
                    Log.d(tag, "회원조회 성공 code" + response.code().toString())
                    _emailData.value = result.email
                    _nicknameData.value = result.nickname
                    _isNicknameData.value = true
                    _majorData.value = result.major
                    _isMajorData.value = true
                    _genderData.value = result.gender
                    _imgData.value = result.imageUrl // TODO("이미지처리")
                    _isImgData.value = true
                    onClickListener.myInfoCallbackResult(true)
                } else {
                    Log.e(tag, "회원조회 실패")
                    onClickListener.myInfoCallbackResult(false)
                    val errorResponse: UsersErrorResponse? = response.errorBody()?.let {
                        val gson = Gson()
                        gson.fromJson(it.charStream(), UsersErrorResponse::class.java)
                    }
                    if (errorResponse != null) {
                        val status = errorResponse.status
                        Log.e(tag, status.toString())
                    }
                }
            } catch (e: Exception) {
                Log.e(tag, "회원조회 Excep: ${e.message}")
            }
        }
    }
}

interface MyInfoCallbackListener {
    fun myInfoCallbackResult(isSuccess: Boolean = false)
}
