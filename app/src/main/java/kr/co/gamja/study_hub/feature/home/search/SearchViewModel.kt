package kr.co.gamja.study_hub.feature.home.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kr.co.gamja.study_hub.data.model.ContentXXXX
import kr.co.gamja.study_hub.data.repository.RetrofitManager

class SearchViewModel: ViewModel() {

    val tag = this.javaClass.simpleName

    //검색된 내용
    private var keyword = ""

    // 둘러보기인지
    var isUserLogin = MutableLiveData<Boolean>(true)

    private val _searchImg =MutableLiveData<Boolean>(true)
    val searchImg:LiveData<Boolean> get() =_searchImg

    //study 리스트
    private val _studys = MutableLiveData<List<ContentXXXX>>()
    val studys : LiveData<List<ContentXXXX>>
        get() = _studys

    // 텍스트 지우기 검색 이미지 활성화
    fun updateSearchImg(){
        _searchImg.value = keyword == ""
    }

    // 단어를 가지고 검색
    fun fetchStudys(isHot : Boolean, isDepartment: Boolean){
        runBlocking{
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val response = RetrofitManager.api.getStudyPostAll(
                        hot = isHot,
                        page = 0,
                        size = 10,
                        inquiryText = keyword,
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

    //데이터 추가
    suspend fun addSearchData(isHot : Boolean, isDepartment: Boolean, page : Int) =
        viewModelScope.async(Dispatchers.IO) {

            Log.d(tag,"addSearchData : ${_studys.value}")

            try {
                val response = RetrofitManager.api.getStudyPostAll(
                    hot = isHot,
                    page = page,
                    size = 10,
                    inquiryText = keyword,
                    titleAndMajor = !isDepartment
                )

                if (response.isSuccessful) {
                    val result = response.body()
                    val data = result?.postDataByInquiries?.content ?: emptyList()
                    _studys.postValue(_studys.value?.plus(data))

                    Log.d(tag, "response is done : ${_studys.value}")

                    return@async ""
                } else {
                    return@async response.errorBody()
                }
            } catch (e: Exception) {
                return@async e.message
            }
        }.await()

    //검색어 저장
    fun saveKeyword(keyword : String) {
        this.keyword = keyword
    }

    //검색어 불러오기
    fun getKeyword() = keyword
}