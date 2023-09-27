package kr.co.gamja.study_hub.feature.studypage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentCalendarBinding

class CalendarFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCalendarBinding
    private val viewModel: CreateStudyViewModel by activityViewModels()
    private lateinit var formattedDate: String
    private lateinit var whatDay: String
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

        // TODO("캘린더 커스텀 및 시작날짜<종료날짜")
        binding.calendar.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            formattedDate = "${year}년 ${month + 1}월 ${dayOfMonth}일"
            binding.btnOk.isEnabled = true
            binding.btnOk.setTextColor(ContextCompat.getColor(requireContext(), R.color.O_50))
        }

        binding.btnOk.setOnClickListener {
            if (whatDay == "0") {
                viewModel.setStartDay(formattedDate)
            } else {
                viewModel.setEndDay(formattedDate)
            }
            dismiss()
        }
    }
}