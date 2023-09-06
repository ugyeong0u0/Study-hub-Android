package kr.co.gamja.study_hub.global

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import kr.co.gamja.study_hub.R

class CustomDialog(
    context: Context,
    private var head: String?,
    private var sub: String?,
    private var btnNo: String?,
    private var btnYes: String?
) {
    private val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener

    fun setOnClickListener(listener: OnDialogClickListener) {
        onClickListener = listener
    }

    fun showDialog() {
        dialog.setContentView(R.layout.dialog)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.setCanceledOnTouchOutside(true) // 외부터치
        dialog.setCancelable(true) // 뒤로가기
        dialog.show()

        val txtHead = dialog.findViewById<TextView>(R.id.txt_head)
        txtHead.text = head

        val txtSub = dialog.findViewById<TextView>(R.id.txt_sub)
        if (sub == null) {
            txtSub.visibility= View.GONE
        }
        else txtSub.text = sub
        val noButton = dialog.findViewById<Button>(R.id.btn_cancel)
        noButton.text = btnNo
        noButton.setOnClickListener { dialog.dismiss() }
        val yesButton = dialog.findViewById<Button>(R.id.btn_ok)
        yesButton.text = btnYes

        yesButton.setOnClickListener {
            onClickListener.onclickResult()
            dialog.dismiss()
        }

    }
}

interface OnDialogClickListener {
    fun onclickResult()
}