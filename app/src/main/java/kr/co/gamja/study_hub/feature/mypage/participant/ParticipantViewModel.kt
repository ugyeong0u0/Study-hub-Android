package kr.co.gamja.study_hub.feature.mypage.participant

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.RegisterListContent
import kr.co.gamja.study_hub.data.repository.RetrofitManager

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
        studyId : Int,
        page : Int,
    ) {
        viewModelScope.launch(Dispatchers.IO){
            //신청 리스트 받아오기
            try {
                val response = RetrofitManager.api.getRegisterList(page, 5, studyId)
                if (response.isSuccessful){
                    val result = response.body() ?: throw NullPointerException("Result is NULL")
                    val datas = result.applyUserData.content

                    val waitingData = mutableListOf<RegisterListContent>()
                    val acceptData = mutableListOf<RegisterListContent>()
                    val rejectData = mutableListOf<RegisterListContent>()

                    datas.forEach{ data ->
                        when (data.inspection) {
                            "STANDBY" -> waitingData.add(data)
                            "ACCEPT" -> acceptData.add(data)
                            "REJECT" -> rejectData.add(data)
                            else -> throw IllegalArgumentException("This type does not exist")
                        }
                    }

                    _participantWaitingList.postValue(waitingData)
                    _acceptList.postValue(acceptData)
                    _refuseList.postValue(rejectData)

                    Log.d("ParticipantViewModel", "fetchWaitingList is Success")
                } else {
                    Log.d("ParticipantViewModel", "fetchWaitingList is Failed")
                }
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
                val response = RetrofitManager.api.editApplyInfo("ACCEPT", studyId, userId)
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
    fun refusal(
        studyId : Int,
        userId : Int
    ){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = RetrofitManager.api.editApplyInfo("REJECT", studyId, userId)
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