package kr.co.gamja.study_hub.feature.toolbar.bookmark

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.BookmarkSaveDeleteResponse
import kr.co.gamja.study_hub.data.model.GetBookmarkResponse
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.StudyHubApi

class BookmarkViewModel(studyHubApi: StudyHubApi) : ViewModel() {
    private val tag = this.javaClass.simpleName

    // 리스트 개수
    private val _listSize = MutableLiveData<Int>()
    val listSize: LiveData<Int> get() = _listSize

    val myBookmarkFlow= Pager(
        PagingConfig(
            pageSize = 10,
            enablePlaceholders = false,
            initialLoadSize = 10
        )
    ){
        BookmarkPagingSource(studyHubApi)
    }.flow.cachedIn(viewModelScope)

    // 내가 쓴 스터디 글 총 개수
    fun getBookmarkList() {
        viewModelScope.launch {
            try {
                val response = AuthRetrofitManager.api.getBookmark(0, 10)
                if (response.isSuccessful) {
                    val result = response.body() as GetBookmarkResponse
                    Log.d(tag, "북마크조회 성공 code" + response.code().toString())
                    _listSize.value = result.totalCount
                    Log.d(tag, "북마크 개수 조회 : " + result.totalCount)
                }
            } catch (e: Exception) {
                Log.e(tag, "북마크조회 Exception: ${e.message}")
            }
        }
    }

    fun saveDeleteBookmarkItem(postId: Int?) {
        val req = postId
        Log.d(tag, req.toString())
        viewModelScope.launch {
            try {
                val response = AuthRetrofitManager.api.saveDeleteBookmark(req)
                if (response.isSuccessful) {
                    Log.d(tag, "북마크 저장 코드 code" + response.code().toString())
                    val result = response.body() as BookmarkSaveDeleteResponse
                    Log.d(tag, "저장인지 삭제인지 :" + result.created.toString())

                }
            } catch (e: Exception) {
                Log.e(tag, "북마크 저장삭제 Exception: ${e.message}")
            }
        }
    }
}