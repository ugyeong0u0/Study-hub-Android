package kr.co.gamja.study_hub.feature.mypage.participant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.*
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.CallBackListener
import kr.co.gamja.study_hub.data.repository.RetrofitManager
class ParticipantViewModel : ViewModel() {

    //대기 목록
    private val _participantWaitingList = MutableLiveData<List<RegisterListContent>>()
    val participantWaitingList: LiveData<List<RegisterListContent>>
        get() = _participantWaitingList

    //참가자
    private val _acceptList = MutableLiveData<List<RegisterListContent>>()
    val acceptList: LiveData<List<RegisterListContent>>
        get() = _acceptList

    //거절 목록
    private val _refuseList = MutableLiveData<List<RegisterListContent>>()
    val refuseList: LiveData<List<RegisterListContent>>
        get() = _refuseList

    private val _errMsg = MutableLiveData<String>()
    val errMsg: LiveData<String>
        get() = _errMsg

    //waitingList 갱신
    fun fetchData(
        inspection: String,
        studyId: Int,
        page: Int,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            //신청 리스트 받아오기
            try {
                val response =
                    if (inspection == "REJECT") AuthRetrofitManager.api.getRegisterListReject(
                        page = page,
                        size = 10,
                        studyId = studyId
                    )
                    else RetrofitManager.api.getRegisterList(inspection, page, 10, studyId)

                if (response.isSuccessful) {
                    val result = response.body() ?: throw NullPointerException("Result is NULL")
                    val datas = result.applyUserData.content

                    val tmpData = mutableListOf<RegisterListContent>()

                    datas.forEach { data ->
                        tmpData.add(data)
                    }
                    when (inspection) {
                        "STANDBY" -> {
                            _participantWaitingList.postValue(tmpData)
                        }
                        "ACCEPT" -> {
                            _acceptList.postValue(tmpData)
                        }
                        "REJECT" -> {
                            _refuseList.postValue(tmpData)
                        }
                    }
                } else {
                    /** fetch data 실패 로직 */
                }
            } catch (e: Exception) {
                throw IllegalArgumentException(e)
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

    fun reject(
        rejectReason: String,
        studyId: Int,
        userId: Int,
        params:CallBackListener
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
}