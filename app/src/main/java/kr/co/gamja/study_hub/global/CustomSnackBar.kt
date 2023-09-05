package kr.co.gamja.study_hub.global

import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kr.co.gamja.study_hub.databinding.CustomSnackbarBinding

class CustomSnackBar(view: View, private val message: String, private val anchorview: View) {
    companion object {
        fun make(view: View, message: String, anchorview: View) =
            CustomSnackBar(view, message, anchorview)
    }

    private val context = view.context
    private val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT)
    private val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout

    private val snackbarBinding: CustomSnackbarBinding =
        CustomSnackbarBinding.inflate(LayoutInflater.from(context))

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
    }

    fun show() {
        snackbar.apply {
            anchorView =anchorview
        }.show()
    }

}