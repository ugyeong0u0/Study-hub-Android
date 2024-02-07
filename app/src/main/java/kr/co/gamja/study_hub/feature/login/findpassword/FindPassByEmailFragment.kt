package kr.co.gamja.study_hub.feature.login.findpassword

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.repository.CallBackListener
import kr.co.gamja.study_hub.databinding.FragmentFindPassByEmailBinding
import kr.co.gamja.study_hub.global.CustomSnackBar

// 비번 찾기(이메일) 1페이지
class FindPassByEmailFragment : Fragment() {
    val tagMsg = this.javaClass.simpleName
    private lateinit var binding: FragmentFindPassByEmailBinding
    private val viewModel: FindPasswordViewModel by activityViewModels()
    var fromPage: String = "" // 비밀번호 찾기 누른 페이지 bundle로 받음
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_find_pass_by_email,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.initEmailVerification() // 이메일 보낼때 비동기함수 끝났는지 여부 변수 초기화
        viewModel.initEmailVerificationTwo(false) // emailVerificationResult.value false시 observe때문에 놓음

        // 툴바 설정
        val toolbar = binding.findPasswordToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""


        // 비밀번호 찾기 누른 페이지 bundle로 받음
        val receiveBundle = arguments
        if (receiveBundle != null) {
            fromPage = receiveBundle.getString("page").toString()
//            Log.e(tagMsg, "어떤 페이지에서 비밀번호 찾기로 왔는지 : {$fromPage}")
        } else Log.e(
            tagMsg,
            "FindPassByEmailFragment's receiveBundle is Null in goToCorrectStudy()"
        )

        binding.iconBack.setOnClickListener {
            viewModel.initUserEmail() // 이메일 초기화
            when (fromPage) {
                // 마이페이지에서 비번변경할 시 - currentPassword.kt에서옴
                "changePassword" -> {
//                  Log.e(tagMsg,"changePassword했고 mypageinfo로")
                    findNavController().navigateUp() // 뒤로 가기
                }
                // 마이페이지에서 비번찾기 누를시 - currentPassword.kt에서 비번찾기 눌러서 옴
                "myPage" -> {
//                  Log.e(tagMsg,"mypageinfo로")
                    findNavController().navigateUp() // 뒤로 가기
                }
                // 로그인에서 비번 찾기 누를 시 - login.kt에서 옴
                // todo(로그인 시에는 jwt토큰 없어서 서버 답변 필요)
                "login" -> {
//                   Log.e(tagMsg,"login으로")
                    // 로그인 프래그먼트로 이동
                    findNavController().navigate(R.id.action_global_loginFragment)
                }
            }

        }

        // 안드로이드 하단바에서 뒤로가기 할 시 -> 로그인에서 넘어가는 경우 그래프 변경해서 변겨된 그래프의 홈으로 가버림
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (fromPage.equals("login")) {
                        // 로그인 프래그먼트로 이동
                        findNavController().navigate(R.id.action_global_loginFragment)
                    }
                }
            })

        // 유저 이메일 확인 버튼 활성화
        viewModel.userEmail.observe(viewLifecycleOwner) {
            viewModel.updateEnableEmailBtn()
        }

        // 이메일 인증 통신 완료 된 후 화면 전환 혹은 스낵바
        viewModel.emailVerificationResult.observe(viewLifecycleOwner) { result ->
            if (result) {
                val bundle = Bundle()
                bundle.putString("page", fromPage)
                Log.i(tagMsg, "3. 이메일 콜백 isAdd : $isAdded")
                viewModel.initEmailVerificationTwo(false) // emailVerificationResult.value false시 observe때문에 놓음
                findNavController().navigate(
                    R.id.action_findPassByEmailFragment_to_findPassByAuthFragment,
                    bundle
                )
            } else {
                if (viewModel.emailVerificationResultTwo.value == true) {
                    // 존재하지 않는 이메일 오류 스낵바
                    CustomSnackBar.make(
                        binding.layoutRelative,
                        requireContext().getString(R.string.error_noRegisteredEmail),
                        null, true, R.drawable.icon_warning_m_orange_8_12
                    ).show()
                }
            }
        }

        // 이메일 확인 버튼 클릭 시
        binding.btnComplete.setOnClickListener {
            hideKeyboardForResend() // 자판내리기
            viewModel.emailForFindPassword() // 이메일 인증
        }
    }

    private fun hideKeyboardForResend() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusView = requireActivity().currentFocus
        if (currentFocusView != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocusView.windowToken, 0)
        }
    }
}