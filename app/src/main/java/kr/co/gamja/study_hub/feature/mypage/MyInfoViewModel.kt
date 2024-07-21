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
import kr.co.gamja.study_hub.data.repository.CallBackListener
import kr.co.gamja.study_hub.global.Functions

class MyInfoViewModel : ViewModel() {
    private val tag = this.javaClass.simpleName
    private val functions = Functions()

    // 둘러보기인지
    var isUserLogin = MutableLiveData<Boolean>(false) // 회원인지 비동기값 얻어오는데 시간걸려서 애초 초기값 false로 줌

    // 유저 이메일
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

    private val _imgData = MutableLiveData<String>()
    val imgData: LiveData<String> get() = _imgData

    // 회원 비회원 여부
    private val _isImgData = MutableLiveData<Boolean>()
    val isImgData: LiveData<Boolean> get() = _isImgData

    // 마이페이지 게시글 수
    private val _writtenData = MutableLiveData<String>("0")
    val writtenData: LiveData<String> get() = _writtenData
    // 참여자 수
    private val _participantData = MutableLiveData<String>("0")
    val participantData: LiveData<String> get() = _participantData
    // 신청내역 수
    private val _bookmarkData = MutableLiveData<String>("0")
    val bookmarkData: LiveData<String> get() = _bookmarkData


    // 프로그래스 바 (사진 업로드 시 )
    private val _progressBar = MutableLiveData<Boolean>(false)
    val progressBar: LiveData<Boolean> get() = _progressBar

    fun setProgressBar(status : Boolean){

        _progressBar.value = status

    }


    private lateinit var onClickListener: MyInfoCallbackListener
    fun mSetOnClickListener(listener: MyInfoCallbackListener) {
        onClickListener = listener
    }

    // 비회원일 시 초기화
    fun init() {
        _isImgData.value = false
        _isMajorData.value = false
        _isNicknameData.value = false
        _writtenData.value="0" // 게시글 수
        _participantData.value="0"// 참여자 수
        _bookmarkData.value="0" // 북마크 수
        isUserLogin.value=false
    }

    // 회원조회
    fun getUsers(params : CallBackListener) {
        viewModelScope.launch {
            try {
                val response = AuthRetrofitManager.api.getUserInfo()
                if (response.isSuccessful) {
                    val result = response.body() as UsersResponse
                    Log.d(tag, "회원조회 성공 code" + response.code().toString())
                    Log.d(tag, "회원조회 닉네임" + result.nickname.toString())
                    _emailData.value = result.email
                    _nicknameData.value = result.nickname
                    _isNicknameData.value = true
                    val koreanMajor = functions.convertToKoreanMajor(result.major)
                    _majorData.value = koreanMajor
                    _isMajorData.value = true
                    val koreanGender = functions.convertToKoreanGender(result.gender)
                    _genderData.value = koreanGender
                    _imgData.value = result.imageUrl
                    _isImgData.value = true
                    _writtenData.value=result.postCount.toString()
                    _bookmarkData.value=result.applyCount.toString() // 북마크수가 아니라 신청내역수임(변수명 변경해야함)
                    _participantData.value=result.participateCount.toString()
                    onClickListener.myInfoCallbackResult(true)
                    isUserLogin.value=true
                    params.isSuccess(true)
                } else {
                    params.isSuccess(false) // 회원조회가 안된 경우
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
                params.isSuccess(true) // 비회원인 경우에도 프로그래스바 제어해야해서 필요
                Log.e(tag, "회원조회 Excep: ${e.message}")
            }
        }
    }

    // 유저 사진 삭제
    fun deleteImg() {
        viewModelScope.launch {
            try {
                val response = AuthRetrofitManager.api.deleteUserImg()
                if (response.isSuccessful) {
                    Log.d(tag, "유저 사진 삭제 성공 code : " + response.code().toString())
                } else {
                    Log.e(tag, "유저 사진 삭제 실패 code : " + response.code().toString())
                }
            } catch (e: Exception) {
                Log.e(tag, "유저 사진 삭제 Excep: ${e.message}")
            }
        }
    }

}

interface MyInfoCallbackListener {
    fun myInfoCallbackResult(isSuccess: Boolean = false)
}
