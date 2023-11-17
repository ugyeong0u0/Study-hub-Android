package kr.co.gamja.study_hub.feature.signup.nickname

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentNicknameBinding
import kr.co.gamja.study_hub.feature.signup.major.User
import kr.co.gamja.study_hub.global.ExtensionFragment.Companion.hideKeyboard


class NicknameFragment : Fragment() {
    private lateinit var binding: FragmentNicknameBinding
    private lateinit var selectedDrawable: Drawable
    private lateinit var nonSeletedDrawaable: Drawable
    private val viewModel: NicknameViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_nickname, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

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

        selectedDrawable = ResourcesCompat.getDrawable(
            resources,
            R.drawable.btn_gender_o_deactivation_stroke_o_50,
            null
        )!!
        nonSeletedDrawaable =
            ResourcesCompat.getDrawable(resources, R.drawable.btn_gender_g_100_stroke_g_80, null)!!
        // 툴바 설정
        val toolbar = binding.createUserToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        binding.iconBack.setOnClickListener {
            val navcontroller = findNavController()
            navcontroller.navigateUp() // 뒤로 가기
        }
        binding.txtPageNumber.text = getString(R.string.txt_pagenumber, 3)

        binding.btnFemale.setOnClickListener {
            binding.btnFemale.background = selectedDrawable
            binding.btnMale.background = nonSeletedDrawaable

            var txt_nickname = binding.editNickName.text.toString()
            User.nickname = txt_nickname
            Log.d("회원가입 - User.nickname클릭", User.nickname.toString())
            User.gender = "FEMALE"
            Log.d("회원가입 - 여자", User.gender.toString())

            if (User.nickname != null && User.gender != null && viewModel.nicknameError.value==false) {
                binding.btnNext.isEnabled = true
            } else {
                Log.d("회원가입 - 닉네임이나 성별 null", User.gender.toString() + User.nickname.toString())
            }
        }
        binding.btnMale.setOnClickListener {

            binding.btnFemale.background = nonSeletedDrawaable
            binding.btnMale.background = selectedDrawable
            var txt_nickname = binding.editNickName.text.toString()
            User.nickname = txt_nickname
            Log.d("회원가입 - User.nickname 성별클릭", User.nickname.toString())
            User.gender = "MALE"
            Log.d("회원가입 - 남자", User.gender.toString())
            if (User.nickname != null && User.gender != null && viewModel.nicknameError.value==false) {
                binding.btnNext.isEnabled = true
            } else {
                Log.d("회원가입 - 닉네임이나 성별 null", User.gender.toString() + User.nickname.toString())
            }
        }

        binding.btnNext.setOnClickListener {
            findNavController().navigate(
                R.id.action_createAccountFragment03_to_createAccountFragment04,
                null
            )
            Log.d("회원가입 - User.nickname 학과로", User.nickname.toString())
            Log.d("회원가입 - User.gender 학과로", User.gender.toString())
        }
        // 닉네임 중복 api
        binding.btnNicknameOverlapCheck.setOnClickListener{
            viewModel.isDuplicationNickname()
        }
        // 닉네임 길이 표시
        viewModel.nickname.observe(viewLifecycleOwner) {
            viewModel.setNicknameLength(it.length) // 닉네임 길이 업뎃
        }
    }

}
