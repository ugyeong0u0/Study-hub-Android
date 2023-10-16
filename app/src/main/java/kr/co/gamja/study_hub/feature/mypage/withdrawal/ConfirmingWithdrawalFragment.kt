package kr.co.gamja.study_hub.feature.mypage.withdrawal

import android.content.Context
import android.os.Bundle
import android.text.InputType
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
import kr.co.gamja.study_hub.databinding.FragmentConfirmingWithdrawalBinding

class ConfirmingWithdrawalFragment : Fragment() {
    private val tag = this.javaClass.simpleName
    private lateinit var binding: FragmentConfirmingWithdrawalBinding
    private val viewModel: ConfirmingWithdrawalViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_confirming_withdrawal,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val toolbar = binding.confirmingWithdrawalToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        binding.iconBack.setOnClickListener {
            val navcontroller = findNavController()
            navcontroller.navigateUp() // 뒤로 가기
        }
        // 비번 관찰 후 그에 따른 다음 버튼 활성화
        viewModel.currentPassword.observe(viewLifecycleOwner) {
            if (it.toString().length > 1) {
                viewModel.updateEnableBtn()
            }
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

        binding.btnWithdrawal.setOnClickListener {
            hideKeyboardForBtnComplete()
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