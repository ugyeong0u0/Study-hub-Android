package kr.co.gamja.study_hub.global

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment


class ExtensionFragment {
    // 키보드 외 터치시 키보드 숨기기
    companion object {
        fun Fragment.hideKeyboard() {
            activity?.let { fragmentActivity ->
                fragmentActivity.currentFocus?.let { currentFocusView ->
                    val inputManager =
                        fragmentActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(
                        currentFocusView.windowToken,
                        InputMethodManager.HIDE_NOT_ALWAYS
                    )
                }
            }
        }
    }

}