package kr.co.gamja.study_hub.feature.mypage.participant

import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kr.co.gamja.study_hub.data.model.ApplyAccpetRequest
import kr.co.gamja.study_hub.data.model.ApplyRejectDto
import kr.co.gamja.study_hub.data.model.RegisterListContent
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.RetrofitManager
import okhttp3.internal.notify
import kotlin.reflect.typeOf

class ParticipantViewModel : ViewModel() {

    //대기 목록
    private val _participantWaitingList = MutableLiveData<List<RegisterListContent>>()
    val participantWaitingList : LiveData<List<RegisterListContent>>
        get() = _participantWaitingList

    //참가자
    private val _acceptList = MutableLiveData<List<RegisterListContent>>()
    val acceptList : LiveData<List<RegisterListContent>>
        get() = _acceptList

    //거절 목록
    private val _refuseList = MutableLiveData<List<RegisterListContent>>()
    val refuseList : LiveData<List<RegisterListContent>>
        get() = _refuseList

    private val _errMsg = MutableLiveData<String>()
    val errMsg : LiveData<String>
        get() = _errMsg

    //waitingList 갱신
    fun fetchData(
        inspection : String,
        studyId : Int,
        page : Int,
    ) {
        Log.d("Participant", "study id : ${studyId}")
        runBlocking{
            viewModelScope.launch(Dispatchers.IO){
                //신청 리스트 받아오기
                try {
                    val response = RetrofitManager.api.getRegisterList(inspection, page, 8, studyId)
                    Log.d("Participant", "${ response.body() }")
                    if (response.isSuccessful){
                        val result = response.body() ?: throw NullPointerException("Result is NULL")
                        val datas = result.applyUserData.content

                        val tmpData = mutableListOf<RegisterListContent>()

                        datas.forEach{ data ->
                            tmpData.add(data)
                        }
                        Log.d("ParticipantViewModel", "fetch data ~ing")
                        Log.d("ParticipantViewModel", "new data : ${tmpData}")
                        when(inspection){
                            "STANDBY" -> {
                                Log.d("ParticipantViewModel", "waiting start ${_participantWaitingList}")
                                Log.d("ParticipantViewModel", "${ participantWaitingList.value }")
                                _participantWaitingList.postValue(tmpData)
                                Log.d("ParticipantViewModel", "waiting fetch ${_participantWaitingList}")
                                Log.d("ParticipantViewModel", "${ participantWaitingList.value }")
                            }
                            "ACCEPT" -> {
                                Log.d("ParticipantViewModel", "accept start ${_participantWaitingList.value}")
                                _acceptList.postValue(tmpData)
                                Log.d("ParticipantViewModel", "accept fetch ${_participantWaitingList.value}")
                            }
                            "REJECT" -> {
                                Log.d("ParticipantViewModel", "reject start ${_participantWaitingList.value}")
                                _refuseList.postValue(tmpData)
                                Log.d("ParticipantViewModel", "reject fetch ${_participantWaitingList.value}")
                            }
                        }

                        Log.d("ParticipantViewModel", "fetchWaitingList is Success")
                    } else {
                        Log.d("ParticipantViewModel", "fetchWaitingList is Failed")
                    }
                } catch (e : Exception) {
                    throw IllegalArgumentException(e)
                }
            }
        }
    }

    //수락
    fun accept(
        studyId : Int,
        userId : Int
    ){
        viewModelScope.launch(Dispatchers.IO){
            try{
                val requestDto = ApplyAccpetRequest(
                    rejectedUserId = userId,
                    studyId = studyId
                )
                val response = AuthRetrofitManager.api.applyAccept(requestDto)
                if (response.isSuccessful){
                    if (response.code() != 200) {
                        when (response.code()) {
                            401 -> _errMsg.postValue("Unauthorized")
                            403 -> _errMsg.postValue("Forbidden")
                            404 -> _errMsg.postValue("Not Found")
                        }
                    }
                } else {
                    Log.d("ParticipantViewModel", "response is ${response}")
                    Log.d("ParticipantViewModel", "Accept is Failed")
                }
            } catch (e: Exception){
                throw IllegalArgumentException(e)
            }
        }
    }

    //거절
    fun reject(
        rejectReason : String,
        studyId : Int,
        userId : Int
    ){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val requestDto = ApplyRejectDto(
                    rejectReason = rejectReason,
                    rejectedUserId = userId,
                    studyId = studyId
                )
                val response = AuthRetrofitManager.api.applyReject(requestDto)
                if (response.isSuccessful){
                    if (response.code() != 200) {
                        when (response.code()) {
                            401 -> _errMsg.postValue("Unauthorized")
                            403 -> _errMsg.postValue("Forbidden")
                            404 -> _errMsg.postValue("Not Found")
                        }
                    }
                } else {
                    Log.d("ParticipantViewModel", "Refusal is Failed")
                }
            } catch (e : Exception) {
                throw IllegalArgumentException(e)
            }
        }
    }
}