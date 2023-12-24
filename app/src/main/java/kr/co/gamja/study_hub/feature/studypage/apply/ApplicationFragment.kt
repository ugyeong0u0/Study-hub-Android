package kr.co.gamja.study_hub.feature.studypage.apply

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.repository.CallBackListener
import kr.co.gamja.study_hub.databinding.FragmentApplicationBinding
import kr.co.gamja.study_hub.global.CustomSnackBar

class ApplicationFragment : Fragment() {
    private lateinit var binding: FragmentApplicationBinding
    private val viewModel: ApplicationViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_application, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // 툴바 설정
        val toolbar = binding.applicationToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        binding.iconBack.setOnClickListener {
            findNavController().navigateUp() // 뒤로 가기
        }
        binding.btnComplete.setOnClickListener {
            viewModel.applyEditTextLength.observe(viewLifecycleOwner) {
                if (it < 11) {
                    CustomSnackBar.make(
                        binding.layoutRelative,
                        getString(R.string.apply_error),
                        binding.btnComplete,
                        true,
                        R.drawable.icon_warning_m_orange_8_12
                    ).show()
                } else {
                    viewModel.applyStudy(object : CallBackListener {
                        override fun isSuccess(result: Boolean) {
                            val navOptions = NavOptions.Builder()
                                .setPopUpTo(R.id.applicationFragment, true)
                                .build()
                            if (result) {
                                CustomSnackBar.make(
                                    binding.layoutRelative,
                                    getString(R.string.apply_ok)
                                ).show()
                                // todo(" 스터디 켄텐츠 bundle값 3종류 필요한듯(스터디 생성, 수정 , 신청후) 수정하기")
                                /*val action =
                                    ApplicationFragmentDirections.actionGlobalStudyContentFragment(result)
                                findNavController().navigate(action, navOptions) // 백스택에서 생성 페이지 제거*/
                            }
                        }
                    })
                }
            }

        }
    }
}