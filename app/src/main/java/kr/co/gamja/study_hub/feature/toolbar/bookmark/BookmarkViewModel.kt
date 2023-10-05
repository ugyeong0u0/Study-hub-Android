package kr.co.gamja.study_hub.feature.toolbar.bookmark

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.BookmarkSaveDeleteResponse
import kr.co.gamja.study_hub.data.model.GetBookmarkResponse
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager

class BookmarkViewModel : ViewModel() {
    private val tag = this.javaClass.simpleName

    // 리스트 개수
    private val _listSize = MutableLiveData<Int>()
    val listSize: LiveData<Int> get() = _listSize

    fun getBookmarkList(adapter: BookmarkAdapter, page: Int, params: BookmarkCallback) {
        viewModelScope.launch {
            try {
                // @Query("page") page: Int?, @Query("size") size: Int?
                val response = AuthRetrofitManager.api.getBookmark(page, 10)
                if (response.isSuccessful) {
                    val result = response.body() as GetBookmarkResponse
                    Log.d(tag, "북마크조회 성공 code" + response.code().toString())
                    params.isLastPage(result.getBookmarkedPostsData.last) // 마지막 페이지 여부
                    adapter.bookmarkList = result
                    adapter.notifyDataSetChanged()
                    Log.d(tag, "북마크조회 성공 result.size" + result.getBookmarkedPostsData.content.size)
                    _listSize.value = result.totalCount
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

interface BookmarkCallback {
    fun isLastPage(lastPage: Boolean)
}