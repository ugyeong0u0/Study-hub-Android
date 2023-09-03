package kr.co.gamja.study_hub.fragment_login

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.model.User
import kr.co.gamja.study_hub.databinding.FragmentCreate01EmailBinding
import kr.co.gamja.study_hub.model.*

// 회원가입- 이메일 인증
class Create01EmailFragment : Fragment() {
    private var _binding: FragmentCreate01EmailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreate01EmailBinding.inflate(inflater, container, false)
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

        // editText
        // TODO("수정 필요")
        viewModel.getInputEmailLiveData().observe(viewLifecycleOwner, Observer { userInput ->
            if (!userInput.matches(EMAIL.toRegex())) {
                binding.fcaEditlayoutEmail.error = "양식 오류"
            } else {
                binding.fcaEditlayoutEmail.error = "정답"
                val errorColor = ContextCompat.getColor(requireContext(), R.color.G_10)
                val stateList = ColorStateList.valueOf(errorColor)
                binding.fcaEditlayoutEmail.setErrorTextColor(stateList)
                binding.fcaEditlayoutEmail.boxStrokeColor = ContextCompat.getColor(
                    requireContext(),
                    R.color.G_10
                )
            }
        })
        binding.fcaEditId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.setInputEmailLiveData(s.toString())
            }
        })


        // 이메일 인증 버튼 누름
        binding.btnAuth.setOnClickListener {
            val txt_email = binding.fcaEditId.text.toString()
            Log.d("회원가입 btnAuth눌렀을때", txt_email)
            viewModel.emailSend(txt_email)
            Log.d("회원가입 viewmodel 후 ", "")
            binding.btnAuth.isVisible = false
            binding.btnResend.isVisible = true
            binding.fcaTxtWordauthcode.isVisible = true
            binding.fcaEditauthcode.isVisible = true
        }
        // 인증번호 재전송
        binding.btnResend.setOnClickListener {
            val txt_email = binding.fcaEditId.text.toString()
            viewModel.emailSend(txt_email)
        }
        // 인증코드확인
        binding.fcaBtnNext.setOnClickListener {
            val txt_email = binding.fcaEditId.text.toString()
            val authNumber = binding.fcaEditauthcode.text.toString()
            viewModel.emailAuthcheck(authNumber, txt_email, object : EmailValidCallback {
                override fun onEmailValidResult(isValid: Boolean) {
                    Log.d("회원가입 - 이메일인증 콜백오버라이드", isValid.toString())
                    if (isValid == true) {
                        User.email = txt_email
                        findNavController().navigate(R.id.action_createAccountFragment01_to_createAccountFragment02)
                    } else {
                        Toast.makeText(requireContext(), "인증코드 틀림", Toast.LENGTH_LONG).show()
                    }
                }
            })
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
