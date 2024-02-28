package kr.co.gamja.study_hub.feature.mypage.participant

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import kr.co.gamja.study_hub.databinding.ToastLayoutBinding

class CustomToast() : DialogFragment(){

    private lateinit var binding : ToastLayoutBinding

    private val viewModel : ParticipantViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ToastLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        // 다이얼로그의 위치를 지정
        val window = dialog?.window
        window?.setDimAmount(0f)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val params = window?.attributes
        params?.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL // 원하는 위치로 변경 가능
        window?.attributes = params
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val studyId = arguments?.getInt("studyId") ?: -1
        val page = arguments?.getInt("page") ?: -1

        if (studyId != -1 && page == -1){
            viewModel.fetchData("STANDBY", studyId, page)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            dismiss()
        }, 750)
    }
}