package kr.co.gamja.study_hub.feature.studypage.allcomments.bottomsheet

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.ErrorResponse
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.CallBackListener

class CommentBottomSheetViewModel : ViewModel() {
    val tag = javaClass.simpleName

    fun deleteComment(postId: Int, params:CallBackListener) {
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

}