package kr.co.gamja.study_hub.feature.login.findpassword

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.DataBinderMapperImpl
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.repository.CallBackListener
import kr.co.gamja.study_hub.databinding.FragmentFindPassByAuthBinding
import kr.co.gamja.study_hub.global.CustomSnackBar

// 로그인이나 마이페이지 비번변경 페이지에서 비번찾기 눌렀을 때
class FindPassByAuthFragment : Fragment() {
    private val msgTag = this.javaClass.simpleName
    private lateinit var binding: FragmentFindPassByAuthBinding
    private val viewModel: FindPasswordViewModel by activityViewModels()
    var fromPage: String = "" // 이메일로 비번찾기 페이지 bundle로 받음
    private lateinit var userEmailForPass :String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_find_pass_by_auth, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        Log.e(msgTag,"이메일 인증코드 페이지")
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.initEmailVerificationTwo(false) // emailVerificationResult.value false시 observe때문에 놓음
        viewModel.initEmailVerification() // 이메일 보낼때 비동기함수 끝났는지 여부 변수 초기화

        // 툴바 설정
        val toolbar = binding.findPassByAuthToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        binding.iconBack.setOnClickListener {
            viewModel.initUserAuth() // 인증코드 지우기
            findNavController().navigateUp() // 뒤로 가기
        }

        // 비밀번호 찾기 누른 페이지 bundle로 받음
        val receiveBundle = arguments
        if (receiveBundle != null) {
            fromPage = receiveBundle.getString("page").toString()
            userEmailForPass = receiveBundle.getString("userEmail").toString()
            Log.i(msgTag, "어떤 페이지에서 비밀번호 찾기로 왔는지 : {$fromPage}")
        } else Log.e(
            msgTag,
            "FindPassByAuthFragment's receiveBundle is Null in goToCorrectStudy()"
        )

        // 유저 이메일 확인 버튼 활성화
        viewModel.userEmail.observe(viewLifecycleOwner) {
            viewModel.updateEnableEmailBtn()
        }

        // 이메일 인증 통신 완료 된 후 화면 전환 혹은 스낵바
        viewModel.emailVerificationResult.observe(viewLifecycleOwner) { result ->
            if (result) {
                Log.i(msgTag,"이메일 코드 재전송")
                CustomSnackBar.make(
                    binding.layoutRelative,
                    getString(R.string.txt_resendAlarm),
                    null, true, R.drawable.icon_check_green
                ).show()
                viewModel.initEmailVerificationTwo(false) // emailVerificationResult.value false시 observe때문에 놓음
            } else {
                if (viewModel.emailVerificationResultTwo.value == true) {
                    CustomSnackBar.make(
                        binding.layoutRelative,
                        getString(R.string.error_resendPlease),
                        null, true, R.drawable.icon_warning_m_orange_8_12
                    ).show()
                }
            }
        }

        // 재전송 버튼 누를 시
        binding.btnResend.setOnClickListener {
            hideKeyboardForResend() // 자판내리기
            viewModel.emailForFindPassword() // 이메일 인증
        }

        viewModel.userAuth.observe(viewLifecycleOwner) {
            viewModel.updateEnableAuthBtn() // 뷰모델에서 입력값 확인 후 처리
        }

        binding.btnComplete.setOnClickListener {
            hideKeyboardForResend() // 자판 내리기
            viewModel.authForFindPassword(object : CallBackListener {
                override fun isSuccess(result: Boolean) {
                    if (result) {

                        viewModel.initUserAuth() // 인증코드 지우기
                        viewModel.initUserEmail() // 인증 이메일 지우기
                        val bundle = Bundle()
                        bundle.putString("page", fromPage)
                        bundle.putBoolean("auth", true)
                        bundle.putString("userEmail",userEmailForPass)
                        findNavController().navigate(
                            R.id.action_findPassByAuthFragment_to_newPasswordFragment,
                            bundle
                        )
                    } else {
                        CustomSnackBar.make(
                            binding.layoutRelative,
                            getString(R.string.error_notSuitAuth),
                            null, true, R.drawable.icon_warning_m_orange_8_12
                        ).show()
                    }
                }
            })
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