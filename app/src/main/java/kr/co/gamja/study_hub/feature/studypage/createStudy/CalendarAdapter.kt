package kr.co.gamja.study_hub.feature.studypage.createStudy

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kr.co.gamja.study_hub.R

import kr.co.gamja.study_hub.databinding.CalendarCellItemBinding


class CalendarAdapter(private val context: Context) :
    RecyclerView.Adapter<CalendarAdapter.CalendarHolder>() {
    lateinit var currentDay: String // 현재 날짜
    var daysInfo = ArrayList<InfoOfDays>() // 날짜 선택 여부
    private var onCalendarItemClickListener: OnCalendarItemClickListener? = null
    private var selectedPosition = -1

    fun setOnCalendarItemClickListener(listener: OnCalendarItemClickListener) {
        onCalendarItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarHolder {
        val binding =
            CalendarCellItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarHolder, position: Int) {
        val day = daysInfo.get(position)
        holder.setDays(day)
    }

    override fun getItemCount(): Int {
        return daysInfo.size
    }

    inner class CalendarHolder(val binding: CalendarCellItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setDays(item: InfoOfDays) {
            binding.model = item

            if (selectedPosition == absoluteAdapterPosition) {
                daysInfo[absoluteAdapterPosition].isSelected = true
                binding.setChecked()
            } else {
                daysInfo[absoluteAdapterPosition].isSelected = false
                binding.setUncheked()
            }
            if (onCalendarItemClickListener != null) {
                binding.txtDay.setOnClickListener {
                    onCalendarItemClickListener?.onItemClick(item, absoluteAdapterPosition)
                    if (selectedPosition != absoluteAdapterPosition) {
                        binding.setChecked()
                        notifyItemChanged(selectedPosition)
                        selectedPosition = absoluteAdapterPosition
                    }
                }
            }

            // 오늘 이전 날짜들은 회색처리
            if (!item.day.isEmpty())
                if (currentDay.toInt() <= item.day.toInt()) {
                    binding.txtDay.setTextColor(ContextCompat.getColor(context, R.color.sysblack1))
                    binding.txtDay.isEnabled = true
                } else {
                    binding.txtDay.setTextColor(ContextCompat.getColor(context, R.color.BG_40))
                    binding.txtDay.isEnabled = false
                }
        }
    }

    private fun CalendarCellItemBinding.setChecked() {
        txtDay.setTextColor(ContextCompat.getColor(context, R.color.syswhite))
        txtDay.setBackgroundResource(R.drawable.solid_o_50_corner_99)
    }


    private fun CalendarCellItemBinding.setUncheked(){
        txtDay.setTextColor(ContextCompat.getColor(context, R.color.sysblack1))
        txtDay.setBackgroundResource(R.drawable.solid_syswhite)
    }

}

interface OnCalendarItemClickListener {
    fun onItemClick(item: InfoOfDays, position: Int)
}