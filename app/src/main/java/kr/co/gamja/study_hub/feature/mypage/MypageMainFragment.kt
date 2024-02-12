package kr.co.gamja.study_hub.feature.mypage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentMypageMainBinding
import kr.co.gamja.study_hub.global.CustomSnackBar


class MypageMainFragment : Fragment() {
    private lateinit var binding: FragmentMypageMainBinding
    private val viewModel: MyInfoViewModel by activityViewModels()
    private var doubleBackPressed = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage_main, container, false)
        return binding.root

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (doubleBackPressed) {
                        requireActivity().finish()
                    } else {
                        doubleBackPressed = true
                        val activity = requireActivity() as AppCompatActivity
                        val bottomView = activity.findViewById<View>(R.id.bottom_nav)
                        CustomSnackBar.make(
                            binding.layoutUserInfo,
                            getString(R.string.btnBack_login), bottomView, false
                        ).show()
                        view?.postDelayed({ doubleBackPressed = false }, 2000)
                    }
                }
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        // 툴바 설정
        val toolbar = binding.myPageMainToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        viewModel.getUsers()

        viewModel.imgData.observe(viewLifecycleOwner, Observer { img ->
            Glide.with(this).load(viewModel.imgData.value)
                .apply(
                    RequestOptions().override(
                        binding.iconProfile.width,
                        binding.iconProfile.height
                    ).circleCrop()
                )
                .into(binding.iconProfile)

        })
        viewModel.setOnClickListener(object : MyInfoCallbackListener {
            override fun myInfoCallbackResult(isSuccess: Boolean) {
                if (!isSuccess) {
                    viewModel.init() // 비회원시 결과값 초기화
                }
            }
        })
        // 회원 정보 보기 누를시
        binding.layoutUserInfo.setOnClickListener {
            findNavController().navigate(
                R.id.action_global_myInfoFragment,
                null
            )
        }
        // 작성한 글 누를 시
        binding.btnUserContent.setOnClickListener {
            findNavController().navigate(
                R.id.action_mypageMainFragment_to_writtenStudyFragment,
                null
            )
        }
        // 신청 내역 페이지로 이동
        binding.btnUserBookmark.setOnClickListener {
            findNavController().navigate(
                R.id.action_global_applicationHistoryFragment,
                null
            )
        }
        // 참여한 스터디 페이지로 이동
        binding.btnUserStudy.setOnClickListener {
            findNavController().navigate(
                R.id.action_mypageMainFragment_to_engagedStudyFragment,
                null
            )
        }
        // 문의하기
        binding.btnAsking.setOnClickListener {
            findNavController().navigate(
                R.id.action_mypageMainFragment_to_complaintFragment,
                null
            )
        }
    }
}