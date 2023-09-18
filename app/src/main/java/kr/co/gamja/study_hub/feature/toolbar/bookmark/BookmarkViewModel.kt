package kr.co.gamja.study_hub.feature.toolbar.bookmark

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.data.model.BookmarkResponse
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager

class BookmarkViewModel: ViewModel() {
    private val tag = this.javaClass.simpleName
    // 리스트 개수
    private val _listSize = MutableLiveData<Int>()
    val listSize: LiveData<Int> get() = _listSize

    fun getBookmarkList(adapter: BookmarkAdapter){
        viewModelScope.launch {
            try {
                val response=AuthRetrofitManager.api.getBookmark()
                if(response.isSuccessful){
                    val result= response.body() as BookmarkResponse
                    Log.d(tag, "북마크 성공 code" + response.code().toString())
                    adapter.bookmarkList=result
                    adapter.notifyDataSetChanged()
                    Log.d(tag, "북마크 성공 result.size" + result.size)
                    _listSize.value=result.size
                }
            }catch (e:Exception){
                Log.e(tag, "북마크 Exception: ${e.message}")
            }
        }
    }
}