package kr.co.gamja.study_hub.feature.mypage.participant

import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.util.Log
import androidx.datastore.dataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kr.co.gamja.study_hub.data.model.ApplyAcceptRequest
import kr.co.gamja.study_hub.data.model.ApplyRejectDto
import kr.co.gamja.study_hub.data.model.RegisterListContent
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.RetrofitManager
import okhttp3.Authenticator
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
        viewModelScope.launch(Dispatchers.IO){
            //신청 리스트 받아오기
            try {
                Log.d("Participant", "fetch start")
                val response =
                    if (inspection == "REJECT") AuthRetrofitManager.api.getRegisterListReject(page = page, size = 10, studyId = studyId)
                    else RetrofitManager.api.getRegisterList(inspection, page, 10, studyId)

                if (response.isSuccessful){
                    val result = response.body() ?: throw NullPointerException("Result is NULL")
                    val datas = result.applyUserData.content

                    val tmpData = mutableListOf<RegisterListContent>()

                    datas.forEach{ data ->
                        tmpData.add(data)
                    }
                    when(inspection){
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
                    Log.d("ParticipantViewModel", "fetchWaitingList is Failed")
                }
                Log.d("Participant", "fetch done")
            } catch (e : Exception) {
                throw IllegalArgumentException(e)
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
                val requestDto = ApplyAcceptRequest(
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
    ) : Job {
        return viewModelScope.launch{
            try {
                Log.d("Participant", "viewModelScope Launch")
                val requestDto = ApplyRejectDto(
                    rejectReason = rejectReason,
                    rejectedUserId = userId,
                    studyId = studyId
                )
                val response = AuthRetrofitManager.api.applyReject(requestDto)
                Log.d("Participant", "${response.body()}")
                if (response.isSuccessful){
                    if (response.code() != 200) {
                        when (response.code()) {
                            401 -> _errMsg.postValue("Unauthorized")
                            403 -> _errMsg.postValue("Forbidden")
                            404 -> _errMsg.postValue("Not Found")
                            else -> Log.e("Error", "${response.body()}")
                        }
                    } else {
                        Log.d("Participant", "${response.errorBody()}")
                    }
                } else {
                    Log.d("ParticipantViewModel", "Refusal is Failed")
                }
                Log.d("Participant", "Done")
            } catch (e : Exception) {
                throw IllegalArgumentException(e.message)
            }
        }
    }
}