package kr.co.gamja.study_hub.feature.studypage.allcomments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.CommentRequest
import kr.co.gamja.study_hub.data.model.CommentsListResponse
import kr.co.gamja.study_hub.data.model.ErrorResponse
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.CallBackListener
import kr.co.gamja.study_hub.data.repository.StudyHubApi

class AllCommentsViewModel(studyHubApi: StudyHubApi) : ViewModel() {

    private val tag = this.javaClass.simpleName

    // 댓글 개수
    var totalComment = MutableLiveData<Int>(0)

    // 댓글 양방향 데이터
    var comment = MutableLiveData<String>()

    // postId
    private var _postId = MutableLiveData<Int>(0)
    val postId: LiveData<Int> get() = _postId

    fun setPostId(result: Int) {
        _postId.value = result
    }

    val allContentsFlow = Pager(
        PagingConfig(
            pageSize = 10,
            enablePlaceholders = false,
            initialLoadSize = 10
        )
    ) {
        AllCommentsPagingSource(studyHubApi, postId.value!!)
    }.flow.cachedIn(viewModelScope)

    // 댓글 개수 조회
    fun getCommentsList(postId: Int) {
        viewModelScope.launch {
            try {
                val response =
                    AuthRetrofitManager.api.getComments(postId, 0, 8) // 댓글 전체 조회라 page size 신경쓸 필요x

                if (response.isSuccessful) {
                    Log.d(tag, "conmmentsList 코드 code" + response.code().toString())
                    val result = response.body() as CommentsListResponse
                    totalComment.value = result.numberOfElements
                }
            } catch (e: Exception) {
                Log.e(tag, "conmmentsList 코드 Exception: ${e.message}")
            }
        }
    }

    // 댓글 등록
    fun setUserComment(params: CallBackListener) {
        val req = CommentRequest(comment.value!!, postId.value!!)
        viewModelScope.launch {
            val response = AuthRetrofitManager.api.setComment(req)
            if (response.isSuccessful) {
                params.isSuccess(true)
            } else {
                val errorResponse: ErrorResponse? = response.errorBody()?.let {
                    val gson = Gson()
                    gson.fromJson(it.charStream(), ErrorResponse::class.java)
                }
                if (errorResponse != null) {
                    Log.e(tag, errorResponse.message)
                    params.isSuccess(false)
                }
            }
        }
    }
}