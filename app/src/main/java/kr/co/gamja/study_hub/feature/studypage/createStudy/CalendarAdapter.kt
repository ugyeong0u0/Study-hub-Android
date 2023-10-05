package kr.co.gamja.study_hub.feature.studypage.createStudy

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kr.co.gamja.study_hub.R

import kr.co.gamja.study_hub.databinding.CalendarCellItemBinding


class CalendarAdapter(private val context: Context) :
    RecyclerView.Adapter<CalendarAdapter.CalendarHolder>() {
    var daysInfo = ArrayList<InfoOfDays>() // 날짜
    private var onCalendarItemClickListener: OnCalendarItemClickListener? = null
    private var selectedPosition = -1
    lateinit var currentYearMonth: String
    lateinit var currentDay: String
    lateinit var selectedYearMonthDay: InfoOfSelectedDay // 선택된 날짜

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
            binding.model = item // 1일... day

            // 날짜가 없는 경우 선택 불가
            if (daysInfo[absoluteAdapterPosition].infoDay.isNotEmpty()) {
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
                            // 날자 정한거 유지 풀기
                            selectedYearMonthDay.selectedYearMonth=null
                            selectedYearMonthDay.selectedDay=null
                        }

                    }
                }
            }


            // 오늘 이전 날짜들은 회색처리
            if (!item.infoDay.isEmpty())
                if ((currentDay.toInt() > item.infoDay.toInt()) && (currentYearMonth.toInt() == item.yearMonthDay.toInt())) {
                    binding.txtDay.setTextColor(ContextCompat.getColor(context, R.color.BG_40))
                    binding.txtDay.isEnabled = false
                } else {
                    binding.txtDay.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.sysblack1
                        )
                    )
                    binding.txtDay.isEnabled = true
                }
            // 전에 선택한 날짜 유지
            if (!selectedYearMonthDay.selectedDay.isNullOrEmpty() && !selectedYearMonthDay.selectedYearMonth.isNullOrEmpty() && item.yearMonthDay == selectedYearMonthDay.selectedYearMonth.toString() && item.infoDay == selectedYearMonthDay.selectedDay.toString()) {
                Log.e("전에 선택한 날짜 유지", true.toString())
                binding.setChecked()
            }
        }
    }

    private fun CalendarCellItemBinding.setChecked() {
        txtDay.setTextColor(ContextCompat.getColor(context, R.color.syswhite))
        txtDay.setBackgroundResource(R.drawable.solid_o_50_corner_99)
    }


    private fun CalendarCellItemBinding.setUncheked() {
        txtDay.setTextColor(ContextCompat.getColor(context, R.color.sysblack1))
        txtDay.setBackgroundResource(R.drawable.solid_syswhite)
    }

}

interface OnCalendarItemClickListener {
    fun onItemClick(item: InfoOfDays, position: Int)
}