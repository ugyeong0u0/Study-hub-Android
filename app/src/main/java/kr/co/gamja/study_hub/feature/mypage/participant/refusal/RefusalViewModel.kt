package kr.co.gamja.study_hub.feature.mypage.participant.refusal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.ApplyRejectDto
import kr.co.gamja.study_hub.data.model.DuplicationNicknameErrorResponse
import kr.co.gamja.study_hub.data.model.RegisterListContent
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.CallBackListener
import kr.co.gamja.study_hub.data.repository.RetrofitManager

class RefusalViewModel : ViewModel() {

    //거절 목록
    private val _refuseList = MutableLiveData<List<RegisterListContent>>()
    val refuseList: LiveData<List<RegisterListContent>>
        get() = _refuseList

    //list 갱신
    fun fetchData(
        isAdd : Boolean,
        studyId: Int,
        page: Int,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            //신청 리스트 받아오기
            try {
                val response = AuthRetrofitManager.api.getRegisterListReject(
                        page = page,
                        size = 10,
                        studyId = studyId
                    )
                if (response.isSuccessful) {
                    val result = response.body() ?: throw NullPointerException("Result is NULL")

                    if (isAdd) {
                        _refuseList.postValue(
                            refuseList.value ?: emptyList<RegisterListContent>()
                                .plus(result.applyUserData.content)
                        )
                    }
                    else {
                        _refuseList.postValue(result.applyUserData.content)
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