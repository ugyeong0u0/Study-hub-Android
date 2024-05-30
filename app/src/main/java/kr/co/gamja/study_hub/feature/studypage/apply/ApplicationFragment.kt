package kr.co.gamja.study_hub.feature.studypage.apply

import android.os.Bundle
import android.util.Log
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
    val msg = this.javaClass.simpleName
    private lateinit var binding: FragmentApplicationBinding
    private val viewModel: ApplicationViewModel by viewModels()
    lateinit var navOptions: NavOptions
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
        val page = arguments?.getString("whatPage") // 신청후 백스택때문
        val studyId = arguments?.getInt("studyId") // 스터디 조회에서 신청으로 넘어왔을 때 받은 studyId
        val postId = arguments?.getInt("postId")
        Log.d("postId,studyId", postId.toString() + " : " + studyId.toString())

        // 툴바 설정
        val toolbar = binding.applicationToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        binding.iconBack.setOnClickListener {
            findNavController().navigateUp() // 뒤로 가기
        }
        binding.btnComplete.setOnClickListener {

            if (viewModel.applyEditTextLength.value!! < 11) {
                CustomSnackBar.make(
                    binding.layoutRelative,
                    getString(R.string.apply_error),
                    binding.btnComplete,
                    true,
                    R.drawable.icon_warning_m_orange_8_12
                ).show(1000)
            } else {
                Log.e(msg, "신청하기 api 전송")
                viewModel.applyStudy(studyId!!, object : CallBackListener {
                    override fun isSuccess(result: Boolean) {
                        if (result) {
                            CustomSnackBar.make(
                                binding.layoutRelative,
                                getString(R.string.apply_ok),
                                binding.btnComplete
                            ).show()

                            when (page) {
                                "content" -> {
                                    Log.i(tag, "신청 page로 들어온 페이지 : content")
                                    navOptions = NavOptions.Builder() // 백스택에서 제거
                                        .setPopUpTo(R.id.studyContentFragment, true)
                                        .build()
                                }
                                "bookmark" -> {
                                    Log.i(tag, "신청 page로 들어온 페이지 : bookmark")
                                    navOptions = NavOptions.Builder() // 백스택에서 제거
                                        .setPopUpTo(R.id.bookmarkFragment, false)
                                        .build()
                                }
                            }
                            val action =
                                ApplicationFragmentDirections.actionGlobalStudyContentFragment(
                                    true,
                                    postId!!
                                ) // 컨텐츠로 가니까 단건조회에 쓸 postId필요
                            findNavController().navigate(action, navOptions) // 백스택에서 생성 페이지 제거
                        } else {
                            CustomSnackBar.make(
                                binding.layoutRelative,
                                getString(R.string.already_existStudy),
                                binding.btnComplete,
                                false
                            ).show()
                        }
                    }
                })
            }


        }
    }
}