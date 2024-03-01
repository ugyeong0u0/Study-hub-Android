package kr.co.gamja.study_hub.feature.mypage.useterm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.FindStudyResponseM
import kr.co.gamja.study_hub.data.model.UseTermResponse
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.RetrofitManager

class UseTermViewModel : ViewModel() {

    private val tag = this.javaClass.simpleName
    fun getUseTerm(adapter : UseTermAdapter) {
        viewModelScope.launch {
            try {
                val response = RetrofitManager.api.getUserTerm()
                if (response.isSuccessful) {
                    val result = response.body() as UseTermResponse
                    adapter.useTerms= result
                    adapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.e(tag, "유저 사진 삭제 Excep: ${e.message}")
            }
        }
    }
}