package kr.co.gamja.study_hub.feature.mypage.participant.participation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.RegisterListContent
import kr.co.gamja.study_hub.data.repository.RetrofitManager

class ParticipationViewModel : ViewModel() {

    //참가자
    private val _acceptList = MutableLiveData<List<RegisterListContent>>()
    val acceptList: LiveData<List<RegisterListContent>>
        get() = _acceptList

    //수락 list 갱신
    fun fetchData(
        isAdd: Boolean,
        studyId: Int,
        page: Int,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            //신청 리스트 받아오기
            try {
                val response = RetrofitManager.api.getRegisterList("ACCEPT", page, 10, studyId)

                if (response.isSuccessful) {
                    val result = response.body() ?: throw NullPointerException("Result is NULL")

                    if (isAdd) {
                        _acceptList.postValue(
                            acceptList.value ?: emptyList<RegisterListContent>()
                                .plus(result.applyUserData.content)
                        )
                    }
                    else {
                        _acceptList.postValue(result.applyUserData.content)
                    }
                } else {
                    /** fetch data 실패 로직 */
                }
            } catch (e: Exception) {
                throw IllegalArgumentException(e)
            }
        }
    }
}