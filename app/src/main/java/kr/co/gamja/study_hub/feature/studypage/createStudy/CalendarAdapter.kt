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
    var daysInfo = ArrayList<InfoOfDays>() // 날짜
    private var onCalendarItemClickListener: OnCalendarItemClickListener? = null
    private var selectedPosition = -1
    lateinit var currentYearMonth: String
    lateinit var currentDay: String
    lateinit var selectedYearMonthDay: InfoOfSelectedDay // 선택된 날짜
    lateinit var startDate: StartDate // 스터디 시작 날짜

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
                    binding.setChecked(false)
                } else {
                    daysInfo[absoluteAdapterPosition].isSelected = false
                    binding.setUncheked()
                }
                if (onCalendarItemClickListener != null) {
                    binding.txtDay.setOnClickListener {
                        onCalendarItemClickListener?.onItemClick(item, absoluteAdapterPosition)
                        if (selectedPosition != absoluteAdapterPosition) {
                            binding.setChecked(false)
                            notifyItemChanged(selectedPosition)
                            selectedPosition = absoluteAdapterPosition
                            // 날자 정한거 유지 풀기
                            selectedYearMonthDay.selectedYearMonth = null
                            selectedYearMonthDay.selectedDay = null
                        }

                    }
                }
            }
            // 날짜 선택 x 경우 - 현재날짜보다 전날인경우 회색
            if (startDate.selectedYearMonth.isNullOrEmpty() && startDate.selectedDay.isNullOrEmpty()) {
                if (item.infoDay.isNotEmpty()) {
                    val itemDate = item.yearMonthDay + String.format("%02d", item.infoDay.toInt())
                    val currentDate = currentYearMonth + String.format("%02d", currentDay.toInt())
                    if (itemDate < currentDate) {
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
                }
            } else {
                // 날짜 선택 o 경우 - 시작날짜 포함 전 날짜들은 회색 처리
                if (item.infoDay.isNotEmpty()) {
                    if (item.yearMonthDay == startDate.selectedYearMonth.toString()) {
                        val itemDay = String.format("%02d", item.infoDay.toInt())
                        val startDay = String.format("%02d", startDate.selectedDay?.toInt())
                        if (itemDay < startDay) {
                            binding.txtDay.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.BG_40
                                )
                            )
                            binding.txtDay.isEnabled = false
                        }
                        // 시작날짜는 R.color.O_50 표시
                        if (itemDay == startDay) {
                            binding.setChecked(true)
                            binding.txtDay.isEnabled = false
                        }
                    }
                    if (item.yearMonthDay < startDate.selectedYearMonth.toString()) {
                        binding.txtDay.setTextColor(ContextCompat.getColor(context, R.color.BG_40))
                        binding.txtDay.isEnabled = false
                    }
                }
            }
            // 전에 선택한 날짜 유지
            if (!selectedYearMonthDay.selectedDay.isNullOrEmpty() && !selectedYearMonthDay.selectedYearMonth.isNullOrEmpty()
                && item.yearMonthDay == selectedYearMonthDay.selectedYearMonth.toString() && item.infoDay == selectedYearMonthDay.selectedDay.toString()
            ) {
                binding.setChecked(false)
            }
        }
    }

    private fun CalendarCellItemBinding.setChecked(selected: Boolean) {
        txtDay.setTextColor(ContextCompat.getColor(context, R.color.syswhite))
        txtDay.setBackgroundResource(R.drawable.solid_o_50_corner_99)
        // 시작 날짜 색
        if (selected) {
            txtDay.setTextColor(ContextCompat.getColor(context, R.color.O_50))
            txtDay.setBackgroundResource(R.drawable.solid_o_10_corner_99)
        }
    }


    private fun CalendarCellItemBinding.setUncheked() {
        txtDay.setTextColor(ContextCompat.getColor(context, R.color.sysblack1))
        txtDay.setBackgroundResource(R.drawable.solid_syswhite)
    }

}

interface OnCalendarItemClickListener {
    fun onItemClick(item: InfoOfDays, position: Int)
}