package kr.co.gamja.study_hub.feature.signup

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentEmailBinding
import kr.co.gamja.study_hub.global.CustomSnackBar
import kr.co.gamja.study_hub.global.ExtensionFragment.Companion.hideKeyboard
import kotlin.properties.Delegates

// 회원가입- 이메일 인증
class EmailFragment : Fragment() {
    private lateinit var binding: FragmentEmailBinding
    private val viewModel: EmailViewModel by viewModels()
    private var grayColor by Delegates.notNull<Int>() // Gray_80 : 이메일 정상
    private lateinit var grayStateList: ColorStateList
    private var redColor by Delegates.notNull<Int>() // R_50 : 오류
    private lateinit var redStateList: ColorStateList
    private var greenColor by Delegates.notNull<Int>() // Green_10 : 인증코드 정상
    private lateinit var greenStateList: ColorStateList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_email, container, false)
        return binding.root

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_global_loginFragment, null)
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        grayColor = ContextCompat.getColor(requireContext(), R.color.G_80)
        redColor = ContextCompat.getColor(requireContext(), R.color.R_50)
        greenColor = ContextCompat.getColor(requireContext(), R.color.GN_10)
        grayStateList = ColorStateList.valueOf(grayColor)
        redStateList = ColorStateList.valueOf(redColor)
        greenStateList = ColorStateList.valueOf(greenColor)

        // 에딧텍스트 자판 내리기
        binding.root.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    true
                }
                MotionEvent.ACTION_UP -> {
                    this.hideKeyboard()
                    v.performClick()
                    true
                }
                else -> false
            }
        }


        // 툴바 설정
        val toolbar = binding.createEmailToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        binding.iconBack.setOnClickListener {
            findNavController().navigate(R.id.action_global_loginFragment, null)
        }

        binding.txtPageNumber.text = getString(R.string.txt_pagenumber, 1)

        setupEmailEditText()
        setupEmailAuthText()
        // 이메일 양식 확인
        viewModel.validEmail.observe(viewLifecycleOwner, Observer { it ->
            if (it) binding.errorEmail.text = "이메일양식맞음"
        })
        // 이메일 인증 버튼 누름
        binding.btnAuth.setOnClickListener {
            Log.d("회원가입 btnAuth눌렀을때", "")
            viewModel.emailSend(object : EmailValidCallback {
                override fun onEmailValidResult(isValid: Boolean, status: String?) {
                    if (!isValid) {
                        if (status.equals("BAD_REQUEST")) {
                            //TODO("이메일 중복시로 변경")
                            binding.errorEmail.apply {
                                text = "이멜중복"
                                setTextColor(redColor)
                                isVisible = true
                            }
                            binding.editEmail.backgroundTintList = redStateList
                        }
                    } else {
                        // 이메일 인증 보냈을 때
                        binding.errorEmail.apply {
                            text = getString(R.string.txt_sendedemail)
                            setTextColor(grayColor)
                            isVisible = true
                        }
                        binding.editEmail.backgroundTintList = grayStateList
                    }
                }
            })

            binding.btnAuth.isVisible = false
            binding.btnResend.isVisible = true
            binding.txtAuthCode.isVisible = true
            binding.editAuthCode.isVisible = true
        }
        // 인증번호 재전송
        binding.btnResend.setOnClickListener {
            viewModel.emailSend(object : EmailValidCallback {
                override fun onEmailValidResult(isValid: Boolean, status: String?) {
                    if (!isValid) {
                        if (status.equals("BAD_REQUEST")) {
                            //TODO("이메일 중복시로 변경")
                            binding.errorEmail.apply {
                                text = "이멜중복"
                                setTextColor(redColor)
                                isVisible = true
                            }
                            binding.editEmail.backgroundTintList = redStateList
                        }
                    } else {
                        // 이메일 인증 보냈을 때
                        binding.errorEmail.apply {
                            text = getString(R.string.txt_sendedemail)
                            setTextColor(grayColor)
                            isVisible = true
                        }
                        binding.editEmail.backgroundTintList = grayStateList

                    }

                }
            })
            hideKeyboardForResend()
            CustomSnackBar.make(
                binding.layoutRelative,
                getString(R.string.txt_resendAlarm),
                binding.btnNext
            ).show()
        }

        // 인증코드확인
        binding.btnNext.setOnClickListener {
            val txt_email = binding.editEmail.text.toString()
            viewModel.emailAuthcheck(object : EmailCodeValidCallback {
                override fun onEmailCodeValidResult(isValid: Boolean) {
                    Log.d("회원가입 - 이메일인증 콜백오버라이드", isValid.toString())
                    if (isValid == true) {
                        User.email = txt_email
                        findNavController().navigate(
                            R.id.action_createAccountFragment01_to_createAccountFragment02,
                            null
                        )
                    } else {
                        CustomSnackBar.make(
                            binding.layoutRelative,
                            getString(R.string.txt_errorAuthCode),
                            binding.btnNext,true,R.drawable.icon_warning_m_orange_8_12
                        ).show()
                    }
                }
            })
        }

    }

    private fun setupEmailEditText() {
        binding.editEmail.doOnTextChanged { text, _, _, _ ->
            if (text.toString() != viewModel.email.value) {
                viewModel.setEmail(text.toString())
            }
        }

        viewModel.email.observe(viewLifecycleOwner) {
            if (it != binding.editEmail.text.toString()) {
                binding.editEmail.setText(it)
            }
        }
        viewModel.validEmail.observe(viewLifecycleOwner) {
            if (!it) {
                binding.errorEmail.apply {
                    text = getString(R.string.txterror_email)
                    setTextColor(redColor)
                    isVisible = true
                }
                binding.editEmail.backgroundTintList = redStateList
                binding.btnResend.isEnabled = false // 양식이 틀리면 인증 버튼 안되게
            } else {
                binding.errorEmail.isVisible = false
                binding.editEmail.backgroundTintList = grayStateList
                binding.btnAuth.isEnabled = true
                binding.btnResend.isEnabled = true
            }
        }
    }

    private fun setupEmailAuthText() {
        binding.editAuthCode.doOnTextChanged { text, _, _, _ ->
            if (text.toString() != viewModel.authNumber.value) {
                viewModel.setAuthNumber(text.toString())
            }
        }
        viewModel.authNumber.observe(viewLifecycleOwner) {
            if (it != binding.editAuthCode.text.toString())
                binding.editAuthCode.setText(it)
        }
        viewModel.validAuthNumber.observe(viewLifecycleOwner) {
            if (it) binding.editAuthCode.backgroundTintList = greenStateList
            else binding.editAuthCode.backgroundTintList = grayStateList
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
