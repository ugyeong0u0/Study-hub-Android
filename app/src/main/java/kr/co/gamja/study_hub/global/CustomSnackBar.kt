package kr.co.gamja.study_hub.global

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.SnackbarBinding

class CustomSnackBar(
    view: View,
    private val message: String,
    private val anchorview: View?,
    private val checkImg: Boolean,
    private val imgRes: Int
) {
    companion object {
        val okImg = R.drawable.icon_check_green
        fun make(
            view: View,
            message: String,
            anchorview: View? = null,
            checkImg: Boolean = true,
            imgRes: Int = okImg
        ) =
            CustomSnackBar(view, message, anchorview, checkImg, imgRes)
    }

    private val context = view.context
    private val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT)
    private val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout

    private val snackbarBinding: SnackbarBinding =
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
        if (!checkImg) snackbarBinding.iconCheak.isVisible = false
        snackbarBinding.iconCheak.setImageDrawable(AppCompatResources.getDrawable(context, imgRes))
    }

    fun show() {
        if (anchorview != null) {
            snackbar.apply {
                anchorView = anchorview
            }.show()
        } else {
            snackbar.show()
        }
    }

}