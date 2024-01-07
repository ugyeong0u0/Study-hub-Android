package kr.co.gamja.study_hub.feature.studypage.studyContent.correctStudy

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.CallBackListener

class BottomSheetViewModel:ViewModel() {
    val tag = javaClass.simpleName

    // 스터디 삭제
    fun deleteStudy(postId: Int, params: CallBackListener) {
    viewModelScope.launch {
        try {
            val response =AuthRetrofitManager.api.deleteMyStudy(postId = postId)
            if(response.isSuccessful){
               Log.d(tag, "스터디삭제 성공")
                params.isSuccess(true)
            }else{
                params.isSuccess(false)
            }
        }catch (e: Exception){
            e.stackTrace
            Log.e(tag, "스터디 삭제 Exception: ${e.message}")
        }
    }
    }
}