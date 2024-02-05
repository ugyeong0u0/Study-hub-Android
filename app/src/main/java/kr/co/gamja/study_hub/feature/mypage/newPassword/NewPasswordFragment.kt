package kr.co.gamja.study_hub.feature.mypage.newPassword

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.repository.CallBackListener
import kr.co.gamja.study_hub.databinding.FragmentNewPasswordBinding
import kr.co.gamja.study_hub.global.CustomSnackBar


class NewPasswordFragment : Fragment() {
    private lateinit var binding: FragmentNewPasswordBinding
    private val viewModel: NewPasswordViewModel by viewModels()
    private var value = false
    private val tagMsg = this.javaClass.simpleName
    var fromPage: String = "" // 이메일로 비번찾기 페이지 bundle로 받음
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_new_password,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // 툴바 설정
        val toolbar = binding.newPasswordToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        binding.iconBack.setOnClickListener {
            val navcontroller = findNavController()
            navcontroller.navigateUp() // 뒤로 가기
        }

        binding.togglePassword.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // 비밀번호가 안보이고 있는데 보이게
                if (binding.togglePassword.tag.toString() == "0") {
                    binding.togglePassword.tag = "1"
                    binding.editPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    binding.togglePassword.setImageResource(R.drawable.icon_eye_open_black)
                } else { // 비밀번호가 보일때 안보이게
                    binding.togglePassword.tag = "0"
                    binding.editPassword.inputType =
                        InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
                    binding.togglePassword.setImageResource(R.drawable.icon_eye_close_gray)
                }
                // editRePassword 토글버튼 누를때마다 커서 이동 방지
                binding.editPassword.setSelection(binding.editPassword.text.toString().length)
            }
        })
        binding.toggleRePassword.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // 비밀번호가 안보이고 있는데 보이게
                if (binding.toggleRePassword.tag.toString() == "0") {
                    binding.toggleRePassword.tag = "1"
                    binding.editRePassword.inputType =
                        InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    binding.toggleRePassword.setImageResource(R.drawable.icon_eye_open_black)
                } else { // 비밀번호가 보일때 안보이게
                    binding.toggleRePassword.tag = "0"
                    binding.editRePassword.inputType =
                        InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
                    binding.toggleRePassword.setImageResource(R.drawable.icon_eye_close_gray)
                }
                // editRePassword 토글버튼 누를때마다 커서 이동 방지
                binding.editRePassword.setSelection(binding.editRePassword.text.toString().length)
            }
        })

        viewModel.password.observe(viewLifecycleOwner) {
            viewModel.updateEnableBtn()
        }
        viewModel.rePassword.observe(viewLifecycleOwner) {
            viewModel.updateEnableBtn()
        }

        binding.btnComplete.setOnClickListener {
            hideKeyboardForBtnComplete()
            getValue() // frompage 값 가져옴
            viewModel.changePassword(value, object : CallBackListener {
                override fun isSuccess(result: Boolean) {
                    if (result) {
                        CustomSnackBar.make(
                            binding.layoutRelative,
                            getString(R.string.txt_changePasswordSuccessfully),
                            null,
                            true
                        ).show()
//                        Log.e(tagMsg, "when절 전:"+ fromPage)
                        when (fromPage) {
                            // 마이페이지에서 비번변경할 시 - currentPassword.kt에서옴
                            "changePassword" -> {
//                                Log.e(tagMsg,"changePassword했고 mypageinfo로")
                                findNavController().popBackStack(R.id.currentPasswordFragment, true)
                            }
                            // 마이페이지에서 비번찾기 누를시 - currentPassword.kt에서 비번찾기 눌러서 옴
                            "myPage" -> {
//                                Log.e(tagMsg,"mypageinfo로")
                                findNavController().popBackStack(R.id.currentPasswordFragment, true)
                            }
                            // 로그인에서 비번 찾기 누를 시 - login.kt에서 옴
                            "login" -> {
//                                Log.e(tagMsg,"login으로")
                                // 네비게이션 그래프 변경
                                findNavController().setGraph(R.navigation.nav_graph_login_signup)
                                // 로그인 프래그먼트로 이동
                                findNavController().navigate(R.id.loginFragment)
                            }
                        }

                    } else Log.e(tag, "비번 변경 안됨 에러메시지 확인바람")
                }
            })
        }
    }

    private fun getValue() {
        val receiveBundle = arguments
        if (receiveBundle != null) {
            value = receiveBundle.getBoolean("auth")
            fromPage = receiveBundle.getString("page").toString() // 페이지 어디서 왔는지
            Log.d(tag, " value: $value fromPage : $fromPage")
        } else Log.e(tag, "receiveBundle is Null")
    }

    private fun hideKeyboardForBtnComplete() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusView = requireActivity().currentFocus
        if (currentFocusView != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocusView.windowToken, 0)
        }
    }
}