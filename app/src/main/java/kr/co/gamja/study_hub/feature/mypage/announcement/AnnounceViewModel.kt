package kr.co.gamja.study_hub.feature.mypage.announcement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.AnnouncementContentDto
import kr.co.gamja.study_hub.data.repository.RetrofitManager

class AnnounceViewModel : ViewModel() {

    private val _announcementList = MutableLiveData<List<AnnouncementContentDto>>()
    val announcementList : LiveData<List<AnnouncementContentDto>>
        get() = _announcementList

    private val _errMsg = MutableLiveData("")
    val errMsg : LiveData<String>
        get() = _errMsg

    fun initList(){
        viewModelScope.launch{
            try {
                val response = RetrofitManager.api.getAnnounce(0, 8)
                if(response.isSuccessful) {
                    val result = response.body() ?: throw NullPointerException("Data is NULL")
                    _announcementList.postValue(result.content)
                } else {
                    _errMsg.postValue("오류 발생\n다시 시도 해주세요")
                }
            } catch(e : Exception) {
                _errMsg.postValue("오류 발생\n다시 시도 해주세요")
            }
        }
    }

    fun resetErr(){
        _errMsg.postValue("")
    }
}