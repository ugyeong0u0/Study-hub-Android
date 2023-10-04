package kr.co.gamja.study_hub.feature.studypage.createStudy


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentCalendarBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.util.*
import kotlin.collections.ArrayList

class CalendarFragment : BottomSheetDialogFragment() {
    private val tag =this.javaClass.simpleName

    private lateinit var binding: FragmentCalendarBinding
    private val viewModel: CreateStudyViewModel by activityViewModels()
    private lateinit var selectedDate: LocalDate// 년 월
    private lateinit var today: String // 오늘
    private lateinit var currentMonth: String // 오늘이 속한달
    private lateinit var changedYear: String // 바뀐 년
    private lateinit var changedMonth:String // 바뀐 달
    private lateinit var formattedDate: String // 포멧한 값
    private lateinit var whatDay: String // 시작인지 끝날짜인지 구분 tag(스터디 생성에서 가져옴)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false)
        return binding.root
    }

    // TODO("선택한 날짜 뷰모델로 보내기")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val receiveBundle = arguments
        if (receiveBundle != null) {
            val value = receiveBundle.getString("whatDayKey")
            if (value != null) whatDay = value
            else Log.e(tag, "시작날짜인지 끝인지 못받아옴")
        }
        selectedDate = LocalDate.now()
        today = LocalDate.now().dayOfMonth.toString()
        currentMonth = toMonth(LocalDate.now())
        setMonthView() // 상단 월
        // 다음 달 보기
        binding.btnLeft.setOnClickListener {
            selectedDate = selectedDate.minusMonths(1)
            setMonthView() // 월 업데이트
        }
        // 다음 달 보기
        binding.btnRight.setOnClickListener {
            selectedDate = selectedDate.plusMonths(1)
            setMonthView() // 월 업데이트
        }
        //
        binding.btnOk.setOnClickListener {
            if (whatDay == "0") {
                viewModel.setStartDay(formattedDate) // 2023년 11월 1일 표시 및 api통신 날짜(2023-11-01) 설정
            } else {
                viewModel.setEndDay(formattedDate)
            }
            dismiss()
        }
    }


    // month 포멧 변경
    fun monthYearFromDate(date: LocalDate): String {
        val dateAsDate: Date = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant())
        val outputDateFormat = SimpleDateFormat("yyyy-MM", Locale.KOREAN)
        return outputDateFormat.format(dateAsDate)
    }

    fun toMonth(date: LocalDate): String {
        val dateAsDate: Date = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant())
        val outputDateFormat = SimpleDateFormat("yyyyMM", Locale.KOREAN)
        return outputDateFormat.format(dateAsDate)
    }
    fun getOnlyYear(date: LocalDate): String {
        val dateAsDate: Date = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant())
        val outputDateFormat = SimpleDateFormat("yyyy", Locale.KOREAN)
        return outputDateFormat.format(dateAsDate)
    }
    fun getOnlyMonth(date: LocalDate): String {
        val dateAsDate: Date = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant())
        val outputDateFormat = SimpleDateFormat("MM", Locale.KOREAN)
        return outputDateFormat.format(dateAsDate)
    }

    fun daysInMonthArray(newDate: LocalDate): ArrayList<String> {
        val newDayList = ArrayList<String>()
        val onlyMonth = toMonth(newDate)
        changedYear=getOnlyYear(newDate)
        changedMonth=getOnlyMonth(newDate)
        val newMonth = YearMonth.from(newDate) // 2023-10
        val firstDayOfMonth = selectedDate.withDayOfMonth(1)
        val lastDayOfMonth = newMonth.lengthOfMonth()
        val dayOfWeek = firstDayOfMonth.dayOfWeek.value
        // 202310>202409
        if (onlyMonth.toInt() > currentMonth.toInt()) {
            binding.btnLeft.isEnabled = true
            binding.btnLeft.setBackgroundResource(R.drawable.icon_arrow_left_l_black)
        } else {
            binding.btnLeft.isEnabled = false
            binding.btnLeft.setBackgroundResource(R.drawable.icon_arrow_left_l_gray)
        }


        // 7*6
        for (i in 1 until 42) {
            if (i <= dayOfWeek || i > lastDayOfMonth + dayOfWeek)
                newDayList.add("")
            else newDayList.add((i - dayOfWeek).toString())
        }
        return newDayList
    }

    fun setMonthView() {
        binding.txtMonth.setText(monthYearFromDate(selectedDate))
        val newDayList = daysInMonthArray(selectedDate)
        val adapter = CalendarAdapter(requireContext()).apply {
            setOnCalendarItemClickListener(object:OnCalendarItemClickListener{
                override fun onItemClick(item: InfoOfDays, position: Int) {
                    formattedDate = "${changedYear}년 ${changedMonth}월 ${item.day}일"
                    binding.btnOk.isEnabled = true
                    binding.btnOk.setTextColor(ContextCompat.getColor(requireContext(), R.color.O_50))
                    Toast.makeText(requireContext(), "년 월 일 ${item.day}", Toast.LENGTH_SHORT).show()
                }
            })
        }
        val infoOfDaysList:ArrayList<InfoOfDays> =ArrayList()
        for(day in newDayList){
            val info = InfoOfDays(day=day, isSelected = false)
            infoOfDaysList.add(info)
        }
        adapter.daysInfo=infoOfDaysList
        adapter.currentDay = today
        binding.recyclerDay.adapter = adapter
        binding.recyclerDay.layoutManager = GridLayoutManager(requireContext(), 7)
    }

}