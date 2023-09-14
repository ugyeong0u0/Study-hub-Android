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

    private val _emailData = MutableLiveData<String>("")
    val emailData: LiveData<String> get() = _emailData

    private val _nicknameData = MutableLiveData<String>("")
    val nicknameData: LiveData<String> get() = _nicknameData

    private val _majorData = MutableLiveData<String>("")
    val majorData: LiveData<String> get() = _majorData

    private val _genderData = MutableLiveData<String>("")
    val genderData: LiveData<String> get() = _genderData

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
                    _majorData.value = result.major
                    _genderData.value = result.gender

                } else {
                    Log.e(tag, "회원조회 실패")
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
