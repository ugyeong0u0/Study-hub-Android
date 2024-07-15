package kr.co.gamja.study_hub.feature.mypage.changeNickname

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.repository.CallBackListener
import kr.co.gamja.study_hub.databinding.FragmentChangeNicknameBinding
import kr.co.gamja.study_hub.global.CustomSnackBar

class ChangeNicknameFragment : Fragment() {

    private lateinit var binding: FragmentChangeNicknameBinding
    private val viewModel: ChangeNickNameViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_change_nickname, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // 툴바 설정
        val toolbar = binding.changeNicknameToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        binding.iconBack.setOnClickListener {
            val navcontroller = findNavController()
            navcontroller.navigateUp() // 뒤로 가기
        }
        viewModel.nickname.observe(viewLifecycleOwner) {
            viewModel.updateNicknameLength(it.length) // 닉네임 길이 업뎃
        }

        // 닉네임 띄어쓰기, 이모지 방지
        val nicknameEditText = binding.editNewNickname

        nicknameEditText.doOnTextChanged { text, _, _, _ ->
            text?.let {
                val filteredText = viewModel.filterText(it.toString())
                if (it.toString() != viewModel.nickname.value) {
                    Log.d("nickname수정fragment", "닉네임 바뀜")
                    viewModel.updateNickname(filteredText)
                }
            }
        }

        viewModel.nickname.observe(viewLifecycleOwner) { newNickname ->
            if (nicknameEditText.text.toString() != newNickname) {
                nicknameEditText.setText(newNickname)
                nicknameEditText.setSelection(newNickname.length) // 커서 마지막으로 이동시킴
            }
        }

        binding.btnComplete.setOnClickListener {
            hideKeyboardForBtnComplete()
            viewModel.isDuplicationNickname(object : CallBackListener {
                override fun isSuccess(result: Boolean) {
                    // 닉네임 중복 확인 후 수정
                    Log.d(tag, "닉네임 중복인경우 false임. result: ${result}")
                    if (result) {
                        viewModel.putChangedNickname(object : CallBackListener {
                            override fun isSuccess(result: Boolean) {
                                if (result) {
                                    CustomSnackBar.make(
                                        binding.layoutRelative,
                                        getString(R.string.txt_successChangeNickname),
                                        null,
                                        true
                                    ).show()
                                    val navcontroller = findNavController()
                                    navcontroller.navigateUp() // 내 정보 페이지로 돌아감
                                }
                            }
                        }) // 닉네임 수정 api
                    } else {
                        // 중복 snackBar
                        CustomSnackBar.make(
                            binding.layoutRelative,
                            getString(R.string.txt_dontUseThisNickname),
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