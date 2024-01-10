package kr.co.gamja.study_hub.feature.studypage.allcomments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.*
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.CallBackListener
import kr.co.gamja.study_hub.data.repository.StudyHubApi

class AllCommentsViewModel(studyHubApi: StudyHubApi) : ViewModel() {

    private val tag = this.javaClass.simpleName

    // paging 초기화(기존 댓글 잔상 지우기)
    private val _reloadTrigger = MutableStateFlow(Unit)

    fun setReloadTrigger(){
        _reloadTrigger.value=Unit
    }


    // 댓글 개수
    var totalComment = MutableLiveData<Int>(0)

    // 댓글 양방향 데이터
    var comment = MutableLiveData<String>()

    // 게시글 postId
    private var _postId = MutableLiveData<Int>(0)
    val postId: LiveData<Int> get() = _postId

    fun setPostId(result: Int) {
        _postId.value = result
    }

    // 삭제인지 확인
    private var _isDelete = MutableLiveData<Boolean>()
    val isDelete: LiveData<Boolean> get() = _isDelete

    // CommentBottomsheetFragment에서 사용
    fun setDelete(result: Boolean) {
        _isDelete.value = result
    }

    // 수정인지 확인
    private var _isModify = MutableLiveData<Boolean>()
    val isModify: LiveData<Boolean> get() = _isModify

    // CommentBottomsheetFragment에서 사용
    fun setModify(result: Boolean) {
        _isModify.value = result
    }

    fun initComment() {
        comment.value = ""
    }

    val allContentsFlow : Flow<PagingData<Content>> = _reloadTrigger.flatMapLatest {
        Pager(
            PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            )
        ) {
            AllCommentsPagingSource(studyHubApi, postId.value!!)
        }.flow.cachedIn(viewModelScope)
    }

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

    // 삭제 하기
    fun deleteComment(postId: Int, params: CallBackListener) {
        viewModelScope.launch {
            try {
                val response = AuthRetrofitManager.api.deleteComment(postId)
                Log.d(tag, "포스트id : $postId")
                if (response.isSuccessful) {
                    Log.d(tag, "댓글 삭제 성공")
                    params.isSuccess(true)
                } else {
                    params.isSuccess(false)
                    val errorResponse: ErrorResponse? = response.errorBody()?.let {
                        val gson = Gson()
                        gson.fromJson(it.charStream(), ErrorResponse::class.java)
                    }
                    if (errorResponse != null) {
                        Log.e(tag, errorResponse.message)
                    }
                }
            } catch (e: Exception) {
                e.stackTrace
                Log.e(tag, "댓글 삭제 Exception: ${e.message}")
            }
        }
    }

    // 댓글 수정하기
    fun modifyComment(nowCommentId: Int, params: CallBackListener) {
        val req = CommentCorrectRequest(nowCommentId, comment.value!!)
        Log.d(tag, "댓글 viewModel commentId: $nowCommentId postId: ${postId.value}")
        viewModelScope.launch {
            try {
                val response = AuthRetrofitManager.api.correctComment(req)
                Log.d(tag, "응답code : ${response.code()}")
                if (response.isSuccessful) {
                    params.isSuccess(true)
                } else {
                    params.isSuccess(false)
                }
            } catch (e: Exception) {
                e.stackTrace
                Log.e(tag, "댓글 삭제 Exception: ${e.message}")
            }
        }
    }
}