package kr.co.gamja.study_hub.feature.mypage

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import kr.co.gamja.study_hub.global.CustomDialog
import kr.co.gamja.study_hub.global.CustomSnackBar
import kr.co.gamja.study_hub.global.OnDialogClickListener


class MypageMainFragment : Fragment() {
    private lateinit var binding: FragmentMypageMainBinding
    private val viewModel: MyInfoViewModel by activityViewModels()
    private var doubleBackPressed = false
    val tagMsg = this.javaClass.simpleName

    var isUser: Boolean = true // 로그인 유저(true)인지 아니면 둘러보기 유저인지(false)

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
        viewModel.mSetOnClickListener(object : MyInfoCallbackListener {
            override fun myInfoCallbackResult(isSuccess: Boolean) {
                if (!isSuccess) {
                    viewModel.init() // 비회원시 결과값 초기화
                }
                viewModel.isUserLogin.value = isUser
            }
        })
        // 회원 정보 보기 누를시
        binding.layoutUserInfo.setOnClickListener {
            Log.e(tagMsg, "MyInfo로 넘어가는 레이아웃 클릭됨")
            if (viewModel.isUserLogin.value==true) {
                findNavController().navigate(
                    R.id.action_global_myInfoFragment,
                    null
                )
            } else {
                needLogin() // 비회원인경우 로그인 하러가기 다이어로그
            }
        }
        // 작성한 글 누를 시
        binding.btnUserContent.setOnClickListener {
            Log.e(tagMsg, "작성한 글로 넘어가는 버튼 클릭됨")
            if (viewModel.isUserLogin.value==true) {
                findNavController().navigate(
                    R.id.action_mypageMainFragment_to_writtenStudyFragment,
                    null
                )
            } else {
                needLogin() // 비회원인경우 로그인 하러가기 다이어로그
            }
        }
        // 신청 내역 페이지로 이동
        binding.btnUserBookmark.setOnClickListener {
            Log.e(tagMsg, "신청내역으로 넘어가는 버튼 클릭됨")
            if (viewModel.isUserLogin.value==true) {
                findNavController().navigate(
                    R.id.action_global_applicationHistoryFragment,
                    null
                )
            } else {
                needLogin() // 비회원인경우 로그인 하러가기 다이어로그
            }
        }
        // 참여한 스터디 페이지로 이동
        binding.btnUserStudy.setOnClickListener {
            Log.e(tagMsg, "참여한 스터디로 넘어가는 버튼 클릭됨")
           if(viewModel.isUserLogin.value==true){
               findNavController().navigate(
                   R.id.action_mypageMainFragment_to_engagedStudyFragment,
                   null
               )
           }else{
               needLogin() // 비회원인경우 로그인 하러가기 다이어로그
           }
        }
        // 문의하기
        binding.btnAsking.setOnClickListener {
            findNavController().navigate(
                R.id.action_mypageMainFragment_to_complaintFragment,
                null
            )
        }
        //공지사항
        binding.btnNotice.setOnClickListener {
            findNavController().navigate(
                R.id.action_mypageMainFragment_to_announcementFragment,
                null
            )
        }

        // 이용방법으로 가기
        binding.btnUsingGuide.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean("isUser", viewModel.isUserLogin.value!!)
            findNavController().navigate(
                R.id.action_mypageMainFragment_to_manualFragment, bundle
            )
        }
        // 개인정보이용약관
        binding.btnUerTerms.setOnClickListener {
            findNavController().navigate(R.id.action_global_personalInfoTermFragment, null)
        }
        // 이용약관
        binding.btnTerms.setOnClickListener {
            findNavController().navigate(R.id.action_global_serviceUseTermFragment, null)
        }
    }

    fun needLogin() {
        Log.d(tagMsg, "비회원 누름")
        val head =
            requireContext().resources.getString(R.string.head_goLogin)
        val sub =
            requireContext().resources.getString(R.string.sub_goLogin)
        val yes =
            requireContext().resources.getString(R.string.txt_login)
        val no = requireContext().resources.getString(R.string.btn_cancel)
        val dialog =
            CustomDialog(requireContext(), head, sub, no, yes)
        dialog.showDialog()
        dialog.setOnClickListener(object : OnDialogClickListener {
            override fun onclickResult() { // 로그인하러가기 누를시
                findNavController().navigate(R.id.action_global_loginFragment, null)
            }
        })
    }
}