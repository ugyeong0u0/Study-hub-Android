package kr.co.gamja.study_hub.feature.mypage.currentPassword

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
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.repository.CallBackListener
import kr.co.gamja.study_hub.databinding.FragmentCurrentPasswordBinding
import kr.co.gamja.study_hub.global.CustomSnackBar

class CurrentPasswordFragment : Fragment() {
    private lateinit var binding: FragmentCurrentPasswordBinding
    private val viewModel: CurrentPasswordViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_current_password, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // 툴바 설정
        val toolbar = binding.currentPasswordToolbar
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
        // 비번 관찰 후 그에 따른 다음 버튼 활성화
        viewModel.currentPassword.observe(viewLifecycleOwner) {
            if (it.toString().length > 1) {
                viewModel.updateEnableBtn()
            }
        }
        binding.btnComplete.setOnClickListener {
            Log.d(tag, "다음 눌림")
            hideKeyboardForBtnComplete()
            viewModel.isCurrentPasswordValid(viewModel.currentPassword.value.toString(),
                object : CallBackListener {
                    override fun isSuccess(result: Boolean) {
                        if (result) {
                            // 비번 수정 페이지에 true값 보냄
                            val bundle = Bundle()
                            bundle.putBoolean("auth", true)
                            findNavController().navigate(
                                R.id.action_currentPasswordFragment_to_newPasswordFragment,
                                bundle
                            )
                        } else {
                            CustomSnackBar.make(
                                binding.layoutRelative,
                                getString(R.string.error_notMatchPassword),
                                null,
                                true,
                                R.drawable.icon_warning_m_orange_8_12
                            ).show()
                        }
                    }
                })

        }
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