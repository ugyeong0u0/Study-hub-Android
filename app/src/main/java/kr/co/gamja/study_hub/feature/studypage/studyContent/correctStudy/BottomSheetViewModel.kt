package kr.co.gamja.study_hub.feature.studypage.studyContent.correctStudy

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager

class BottomSheetViewModel:ViewModel() {
    val tag = javaClass.simpleName

    // 스터디 삭제
    fun deleteStudy(postId: Int) {
    viewModelScope.launch {
        try {
            val response =AuthRetrofitManager.api.deleteMyStudy(postId = postId)
            if(response.isSuccessful){
               Log.d(tag, "스터디삭제 성공")
            }
        }catch (e: Exception){
            e.stackTrace
            Log.e(tag, "스터디 content조회 Exception: ${e.message}")
        }
    }
    }
}