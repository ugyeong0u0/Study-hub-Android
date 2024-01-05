package kr.co.gamja.study_hub.feature.home.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kr.co.gamja.study_hub.data.model.ContentXXX
import kr.co.gamja.study_hub.data.model.ContentXXXX
import kr.co.gamja.study_hub.data.model.FindStudyResponseM
import kr.co.gamja.study_hub.data.repository.RetrofitManager

class SearchViewModel: ViewModel() {

    val tag = this.javaClass.simpleName
    // editText 입력 단어 - 통신 보낼 변수
    val searchWord = MutableLiveData<String>()
    private val _searchImg =MutableLiveData<Boolean>(true)
    val searchImg:LiveData<Boolean> get() =_searchImg

    //study 리스트
    private val _studys = MutableLiveData<List<ContentXXXX>>()
    val studys : LiveData<List<ContentXXXX>>
        get() = _studys

    // 텍스트 지우기 검색 이미지 활성화
    fun updateSearchImg(){
        _searchImg.value = searchWord.value.toString().isEmpty()
    }

    // 단어를 가지고 검색
    fun fetchStudys(searchContent: String, isHot : Boolean, isDepartment: Boolean){
        runBlocking{
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val response = RetrofitManager.api.getStudyPostAll(
                        hot = isHot,
                        page = 1,
                        size = 10,
                        inquiryText = searchContent,
                        titleAndMajor = !isDepartment
                    )
                    if (response.isSuccessful){
                        val result = response.body() ?: throw NullPointerException("Error : ${response.body()}")
                        Log.d(tag, "data : ${result}")
                        _studys.postValue(result.postDataByInquiries.content)
                        Log.d(tag, "value : ${studys.value}")
                    } else {
                        Log.d(tag, "error : ${response.errorBody()}")
                    }
                } catch (e: Exception) {
                    Log.d(tag, "${e.message}")
                }
            }
        }
    }
}