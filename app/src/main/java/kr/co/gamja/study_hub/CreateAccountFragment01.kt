package kr.co.gamja.study_hub

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.custom.FunctionLogin
import kr.co.gamja.study_hub.databinding.FragmentCreateAccount01Binding


class CreateAccountFragment01 : Fragment() {
    private var _binding: FragmentCreateAccount01Binding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateAccount01Binding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // editText
        val functionLogin: FunctionLogin = FunctionLogin(requireContext())

        binding.fcaTxtPagenumber.text = getString(R.string.txt_pagenumber, 1)
        binding.fcaBtnNext.setOnClickListener {
            findNavController().navigate(
                R.id.action_createAccountFragment01_to_createAccountFragment02,
                null
            )
        }

        // 이메일 확인
        val fcaLayout_email = binding.fcaEditlayoutEmail
        val caf01_flag_email: Boolean = functionLogin.loginTextWatcher(fcaLayout_email, null, 1)
        // 인증코드확인
        val fcaLayout_authcode = binding.fcaEditlayoutAuthcode
        val caf01_flag_authcode: Boolean = functionLogin.authTextWatcher(fcaLayout_authcode)
        // 1. 이메일 확인이 되면, 인증버튼 활성화 and 인증코드 editText색변경
        // 2. 재전송버튼 활성화 and 인증코드 editText 빨간색으로변경
        // 3. 인증코드가 맞다면 editText 녹색으로 전환 and '다음'버튼 활성화

        // ...1
        Log.d("이메일불", "$caf01_flag_email")
        if (caf01_flag_email == true) {
            binding.btnAuth.isVisible = true
            binding.btnAuth.setOnClickListener {
                binding.btnAuth.isVisible = false
                binding.btnResend.isVisible = true
                binding.btnResend.setOnClickListener {
                    binding.fcaTxtWordauthcode.isVisible = true
                    binding.fcaEditauthcode.isVisible = true

                }
//                TODO("에러 밑줄색변경")
            }
            // 인증코드 확인
            if (caf01_flag_authcode == true) {
                binding.fcaBtnNext.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}