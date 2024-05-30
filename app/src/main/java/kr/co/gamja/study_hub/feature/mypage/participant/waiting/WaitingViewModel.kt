package kr.co.gamja.study_hub.feature.mypage.participant.waiting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.ApplyAcceptRequest
import kr.co.gamja.study_hub.data.model.ApplyRejectDto
import kr.co.gamja.study_hub.data.model.DuplicationNicknameErrorResponse
import kr.co.gamja.study_hub.data.model.RegisterListContent
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.CallBackListener
import kr.co.gamja.study_hub.data.repository.RetrofitManager

class WaitingViewModel : ViewModel() {

    //대기 목록
    private val _participantWaitingList = MutableLiveData<List<RegisterListContent>>()
    val participantWaitingList: LiveData<List<RegisterListContent>>
        get() = _participantWaitingList

    private val _errMsg = MutableLiveData<String>()
    val errMsg: LiveData<String>
        get() = _errMsg


    //waitingList 갱신
    fun fetchData(
        isAdd : Boolean,
        studyId: Int,
        page: Int,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            //신청 리스트 받아오기
            try {
                val response = RetrofitManager.api.getRegisterList("STANDBY", page, 10, studyId)

                if (response.isSuccessful) {
                    val result = response.body() ?: throw NullPointerException("Result is NULL")

                    //page 증가 시
                    if (isAdd) {
                        _participantWaitingList.postValue(
                            participantWaitingList.value?:emptyList<RegisterListContent>()
                                .plus(result.applyUserData.content)
                        )
                    }
                    // 데이터 갱신 시 (초기)
                    else {
                        _participantWaitingList.postValue(result.applyUserData.content)
                    }

                } else {
                    /** fetch data 실패 로직 */
                }
            } catch (e: Exception) {
                throw IllegalArgumentException(e)
            }
        }
    }

    fun reject(
        rejectReason: String,
        studyId: Int,
        userId: Int,
        params: CallBackListener
    ) {
        viewModelScope.launch {
            try {
                val requestDto = ApplyRejectDto(
                    rejectReason = rejectReason,
                    rejectedUserId = userId,
                    studyId = studyId
                )
                val response = AuthRetrofitManager.api.applyReject(requestDto)
                if (response.isSuccessful) {
                    params.isSuccess(true)
                } else {
                    params.isSuccess(false)
                    val errorResponse: DuplicationNicknameErrorResponse? =
                        response.errorBody()?.let {
                            val gson = Gson()
                            gson.fromJson(
                                it.charStream(),
                                DuplicationNicknameErrorResponse::class.java
                            )
                        }
                    if (errorResponse != null) {
                        val status = errorResponse.message
                    }
                }
            } catch (e: Exception) {
                throw IllegalArgumentException(e.message)
            }
        }
    }

    //수락
    fun accept(
        studyId: Int,
        userId: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestDto = ApplyAcceptRequest(
                    rejectedUserId = userId,
                    studyId = studyId
                )
                val response = AuthRetrofitManager.api.applyAccept(requestDto)
                if (response.isSuccessful) {
                    if (response.code() != 200) {
                        when (response.code()) {
                            401 -> _errMsg.postValue("Unauthorized")
                            403 -> _errMsg.postValue("Forbidden")
                            404 -> _errMsg.postValue("Not Found")
                        }
                    }
                } else {
                    /** accept api 실패 로직 */
                }
            } catch (e: Exception) {
                throw IllegalArgumentException(e)
            }
        }
    }

}