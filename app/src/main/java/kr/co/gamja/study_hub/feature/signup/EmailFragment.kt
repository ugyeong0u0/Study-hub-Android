package kr.co.gamja.study_hub.feature.signup

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentEmailBinding

// 회원가입- 이메일 인증
class EmailFragment : Fragment() {
    private var _binding: FragmentEmailBinding? = null
    private val binding get() = _binding!!
    private val viewModel :RegisterViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 툴바 설정
        val toolbar = binding.createEmailToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        binding.iconBack.setOnClickListener {
            val navcontroller = findNavController()
            navcontroller.navigateUp() // 뒤로 가기
        }

        binding.fcaTxtPagenumber.text = getString(R.string.txt_pagenumber, 1)

        setupEmailEditText()

        // 이메일 인증 버튼 누름
        binding.btnAuth.setOnClickListener {
            Log.d("회원가입 btnAuth눌렀을때", "")
            viewModel.emailSend()

            binding.btnAuth.isVisible = false
            binding.btnResend.isVisible = true
            binding.fcaTxtWordauthcode.isVisible = true
            binding.fcaEditauthcode.isVisible = true
        }
        // 인증번호 재전송
        binding.btnResend.setOnClickListener{
            viewModel.emailSend()
        }

        // 인증코드확인
        binding.fcaBtnNext.setOnClickListener{
            val txt_email = binding.fcaEditId.text.toString()
            val authNumber=binding.fcaEditauthcode.text.toString()
            viewModel.emailAuthcheck(authNumber,txt_email, object : EmailValidCallback {
                override fun onEmailValidResult(isValid: Boolean) {
                    Log.d("회원가입 - 이메일인증 콜백오버라이드", isValid.toString())
                    if(isValid==true){
                        User.email=txt_email
                        findNavController().navigate(R.id.action_createAccountFragment01_to_createAccountFragment02)
                    }
                    else{
                        Toast.makeText(requireContext(),"인증코드 틀림",Toast.LENGTH_LONG).show()
                    }
                }
            })
        }

    }

    private fun setupEmailEditText() {
        binding.fcaEditId.doOnTextChanged { text, _, _, _ ->
            if(text.toString() != viewModel.email.value) {
                viewModel.updateEmail(text.toString())
            }
        }

        viewModel.email.observe(viewLifecycleOwner) {
            if(it != binding.fcaEditId.text.toString()) {
                binding.fcaEditId.setText(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
