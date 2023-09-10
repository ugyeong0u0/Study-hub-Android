package kr.co.gamja.study_hub.global

import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import kr.co.gamja.study_hub.databinding.SnackbarBinding

class CustomSnackBar(view: View, private val message: String, private val anchorview: View, private val checkImg: Boolean) {
    companion object {
        fun make(view: View, message: String, anchorview: View,checkImg: Boolean=true) =
            CustomSnackBar(view, message, anchorview,checkImg)
    }

    private val context = view.context
    private val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT)
    private val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout

    private val snackbarBinding: SnackbarBinding=
        SnackbarBinding.inflate(LayoutInflater.from(context))

    init {
        initView()
        initData()
    }

    private fun initView() {
        with(snackbarLayout) {
            removeAllViews()
            setPadding(0, 0, 0, 0)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            addView(snackbarBinding.root, 0)
        }
    }

    private fun initData() {
        snackbarBinding.txtSnackBar.text = message
        if(!checkImg) snackbarBinding.iconCheak.isVisible=false
    }

    fun show() {
        snackbar.apply {
            anchorView =anchorview
        }.show()
    }

}