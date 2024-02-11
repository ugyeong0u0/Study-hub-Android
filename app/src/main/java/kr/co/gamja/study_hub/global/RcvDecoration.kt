package kr.co.gamja.study_hub.global

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RcvDecoration(
    private val verticalSpace : Int = 0
) : RecyclerView.ItemDecoration(){

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom = verticalSpace
    }

}