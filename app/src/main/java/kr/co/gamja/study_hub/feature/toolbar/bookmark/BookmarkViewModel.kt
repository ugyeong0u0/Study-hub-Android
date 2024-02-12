package kr.co.gamja.study_hub.feature.toolbar.bookmark

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.BookmarkSaveDeleteResponse
import kr.co.gamja.study_hub.data.model.ContentXX
import kr.co.gamja.study_hub.data.model.GetBookmarkResponse
import kr.co.gamja.study_hub.data.model.GetBookmarkedPostsData
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.StudyHubApi
import retrofit2.Response
import java.util.concurrent.Flow

class BookmarkViewModel(private val authApi: StudyHubApi) : ViewModel() {
    private val tag = this.javaClass.simpleName

    // 둘러보기인지
    var isUserLogin = MutableLiveData<Boolean>(true)

    // 리스트 개수
    private val _listSize = MutableLiveData<Int>()
    val listSize: LiveData<Int> get() = _listSize

    val myBookmarkFlow : kotlinx.coroutines.flow.Flow<PagingData<ContentXX>>
        get() = if (isUserLogin.value == true) {
            Log.e(tag, "회원 북마크 ")
            Pager(
                PagingConfig(
                    pageSize = 10,
                    enablePlaceholders = false,
                    initialLoadSize = 10
                )
            ) {
                BookmarkPagingSource(authApi)
            }.flow.cachedIn(viewModelScope)
        } else {
            Log.e(tag, "비회원 북마크 ")
            flowOf(PagingData.empty<ContentXX>())
        }


    // 내가 쓴 스터디 글 총 개수
    fun getBookmarkList() {
        viewModelScope.launch {
            try { // 회원의 경우
                val response: Response<GetBookmarkResponse>
                if (isUserLogin.value == true) {
                    response = AuthRetrofitManager.api.getBookmark(0, 10)
                    if (response.isSuccessful) {
                        val result = response.body() as GetBookmarkResponse
                        Log.d(tag, "북마크조회 성공 code" + response.code().toString())
                        _listSize.value = result.totalCount
                        Log.d(tag, "북마크 개수 조회 : " + result.totalCount)
                    }
                } else { // 비회원인 경우
                    _listSize.value = 0
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