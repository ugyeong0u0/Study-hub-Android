package kr.co.gamja.study_hub.feature.home.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kr.co.gamja.study_hub.data.model.SearchRecommendResponseDto
import kr.co.gamja.study_hub.data.repository.RetrofitManager

class RecommendViewModel : ViewModel() {
    
    /** recommned api를 사용해서 recommendList 생성 */
    private val _recommendList = MutableLiveData<List<String>>()
    val recommendList : LiveData<List<String>>
        get() = _recommendList

    //키워드 기반 recommendList 업데이트
    fun searchRecommend(keyword : String) {
        runBlocking{
            viewModelScope.launch{
                val response = RetrofitManager.api.getSearchRecommends(keyword)
                try {
                    if (response.isSuccessful){
                        val result = response.body() ?: SearchRecommendResponseDto(emptyList())

                        if (result.recommendList.isNotEmpty()){
                            _recommendList.postValue(result.recommendList)
                        }

                    } else {
                        Log.e("Error", "Response is Failed\n${response.errorBody()}")
                    }
                } catch (e : Exception) {
                    throw IllegalArgumentException(e.message)
                }
            }

        }
    }

    fun resetList() {
        _recommendList.postValue(emptyList())
    }
}