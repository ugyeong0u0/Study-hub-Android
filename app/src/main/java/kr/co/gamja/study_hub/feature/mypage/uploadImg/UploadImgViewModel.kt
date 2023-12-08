package kr.co.gamja.study_hub.feature.mypage.uploadImg

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.CallBackListener
import okhttp3.MultipartBody

class UploadImgViewModel : ViewModel() {
    private val tag = this.javaClass.simpleName
    fun uploadImg(requestBody: MultipartBody.Part, params:CallBackListener) {
        viewModelScope.launch {
            try {
                val response = AuthRetrofitManager.api.setUserImg(requestBody)
                if (response.isSuccessful) {
                    Log.d(tag, "사진 업로딩 ok")
                    params.isSuccess(true)
                } else Log.e(tag, "사진 업로딩 실패" + response.code())
            } catch (e: Exception) {
                Log.e(tag, "사진 업로딩 Excep: ${e.message}")
            }
        }
    }
}