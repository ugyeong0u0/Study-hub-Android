package kr.co.gamja.study_hub.feature.mypage.aboutstudyhub

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentComplaintBinding

class ComplaintFragment : Fragment() {
    private lateinit var binding: FragmentComplaintBinding
    private val viewModel: AboutStudyHubViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_complaint, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        // 툴바 설정
        val toolbar = binding.complaintToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        // 안드로이드 폰 바텀네비에서 뒤로가기 할 시 다이얼로그
        val pressedCallBack = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
               viewModel.initComplaint() // 문의하기 초기화
                findNavController().navigateUp() // 뒤로 가기
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, pressedCallBack)

        binding.iconBack.setOnClickListener {
            viewModel.initComplaint() // 문의하기 초기화
            findNavController().navigateUp() // 뒤로 가기
        }
    }

}