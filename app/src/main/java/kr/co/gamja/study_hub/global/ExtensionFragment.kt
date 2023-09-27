package kr.co.gamja.study_hub.global

import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment


class ExtensionFragment {
     val tag = this.javaClass.simpleName
    // 키보드 외 터치시 키보드 숨기기
    companion object {
        fun Fragment.hideKeyboard() {
            Log.d(tag, " 하이드키바 호출")
            activity?.let { fragmentActivity ->
                fragmentActivity.currentFocus?.let { currentFocusView ->
                    val inputManager =
                        fragmentActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    try {
                        inputManager.hideSoftInputFromWindow(
                            currentFocusView.windowToken,
                            InputMethodManager.HIDE_NOT_ALWAYS
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }
        }
    }

}