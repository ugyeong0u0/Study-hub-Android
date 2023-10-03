package kr.co.gamja.study_hub.feature.studypage.studyHome

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.FindStudyResponse
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager

class StudyMainViewModel : ViewModel() {
    val tag = this.javaClass.simpleName

    // 리스트 개수
    private val _listSize = MutableLiveData<Int>()
    val listSize: LiveData<Int> get() = _listSize

    fun getStudyPosts(adapter: StudyMainAdapter, page: Int, params: getStudyCallback) {
        viewModelScope.launch {
            try {
                val response = AuthRetrofitManager.api.getStudyPostAll(page, 10)
                if (response.isSuccessful) {
                    val result = response.body() as FindStudyResponse
                    params.isLastPage(result.last)
                    adapter.studyPosts = result
                    adapter.notifyDataSetChanged()
                    // TODO("_listSize 업데이트 ")
                }
            } catch (e: Exception) {
                Log.e(tag, "스터디 글 조회 Exception: ${e.message}")
            }
        }
    }

}

interface getStudyCallback {
    fun isLastPage(lastPage: Boolean)
}