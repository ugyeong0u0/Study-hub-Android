package kr.co.gamja.study_hub.feature.mypage.aboutstudyhub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.repository.CallBackListener
import kr.co.gamja.study_hub.databinding.FragmentComplaintBinding
import kr.co.gamja.study_hub.global.CustomSnackBar

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
                viewModel.initComplaint() // 작성하다가 뒤로 갈시 포함해서 문의하기 초기화
                findNavController().navigateUp() // 뒤로 가기
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, pressedCallBack)


        binding.iconBack.setOnClickListener {
            viewModel.initComplaint() // 문의하기 초기화
            findNavController().navigateUp() // 뒤로 가기
        }
// 제목, 이메일, 내용 길이에 따라 버튼 활성화 여부 결정
        viewModel.title.observe(viewLifecycleOwner) {
            if (it.length > 2 && viewModel.content.value?.isNotEmpty() == true && viewModel.content.value?.toString()?.length!! > 4 && viewModel.email.value?.isNotEmpty() == true && viewModel.email.value?.toString()?.length!! > 5) {
                viewModel.updateBtn(true)
            } else viewModel.updateBtn(false)
        }
        viewModel.content.observe(viewLifecycleOwner) {
            if (it.length > 4 && viewModel.title.value?.isNotEmpty() == true && viewModel.title.value?.toString()?.length!! > 2 && viewModel.email.value?.isNotEmpty() == true && viewModel.email.value?.toString()?.length!! > 5) {
                viewModel.updateBtn(true)
            } else viewModel.updateBtn(false)
        }
        viewModel.email.observe(viewLifecycleOwner) {
            if (it.length > 5 && viewModel.title.value?.isNotEmpty() == true && viewModel.title.value?.toString()?.length!! > 2 && viewModel.content.value?.isNotEmpty() == true && viewModel.content.value?.toString()?.length!! > 4) {
                viewModel.updateBtn(true)
            } else viewModel.updateBtn(false)
        }

        // 문의 완료하기 누를 시
        binding.btnComplete.setOnClickListener {
            viewModel.sendQuestion(object : CallBackListener {
                override fun isSuccess(result: Boolean) {
                    val activity = requireActivity() as AppCompatActivity
                    val bottomView = activity.findViewById<View>(R.id.bottom_nav)
                    CustomSnackBar.make(
                        binding.layoutLinear,
                        getString(R.string.complete_complaint),
                        bottomView, true, R.drawable.icon_check_green
                    ).show()
                    findNavController().navigate(
                        R.id.action_global_mypageMainFragment,
                        null
                    )
                    viewModel.initComplaint() // 성공시 초기화
                }
            })
        }
    }

}