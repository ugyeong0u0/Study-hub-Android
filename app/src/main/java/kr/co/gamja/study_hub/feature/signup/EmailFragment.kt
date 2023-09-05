package kr.co.gamja.study_hub.feature.signup

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentEmailBinding
import kr.co.gamja.study_hub.global.CustomSnackBar

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

        binding.txtPageNumber.text = getString(R.string.txt_pagenumber, 1)

        setupEmailEditText()
        setupEmailAuthText()

        // 이메일 인증 버튼 누름
        binding.btnAuth.setOnClickListener {
            Log.d("회원가입 btnAuth눌렀을때", "")
            viewModel.emailSend()

            binding.btnAuth.isVisible = false
            binding.btnResend.isVisible = true
            binding.txtAuthCode.isVisible = true
            binding.editAuthCode.isVisible = true
        }
        // 인증번호 재전송
        binding.btnResend.setOnClickListener{
            viewModel.emailSend()
            CustomSnackBar.make(binding.layoutRelative,getString(R.string.txt_resendAlarm), binding.btnNext).show()
        }

        // 인증코드확인
        binding.btnNext.setOnClickListener{
            val txt_email = binding.editEmail.text.toString()
            viewModel.emailAuthcheck( object : EmailValidCallback {
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
        binding.editEmail.doOnTextChanged { text, _, _, _ ->
            if(text.toString() != viewModel.email.value) {
                viewModel.updateEmail(text.toString())
            }
        }

        viewModel.email.observe(viewLifecycleOwner) {
            if(it != binding.editEmail.text.toString()) {
                binding.editEmail.setText(it)
            }
        }
    }
    private fun setupEmailAuthText(){
        binding.editAuthCode.doOnTextChanged { text, _, _, _ ->
            if(text.toString()!=viewModel.authNumber.value){
                viewModel.updateAuthNumber(text.toString())
            }
        }
        viewModel.authNumber.observe(viewLifecycleOwner, Observer { it->
            if(it!=binding.editAuthCode.text.toString())
                binding.editAuthCode.setText(it)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
