package kr.co.gamja.study_hub.feature.signup.nickname

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
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

        //공백 여부
        var isSpaced = false

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
        binding.txtPageNumber.text = getString(R.string.txt_pagenumber, 4)



        // 닉네임 띄어쓰기, 이모지 방지
        val nicknameEditText = binding.editNickName

        nicknameEditText.doOnTextChanged { text, _, _, _ ->
            text?.let {
                val filteredText = viewModel.filterText(it.toString())
                if (it.toString() != viewModel.nickname.value) {
                    Log.d("nickname수정fragment", "닉네임 바뀜")
                    viewModel.updateNickname(filteredText)
                    viewModel.updateNickNameError(null) // 닉네임 수정일어날시 닉네임 중복확인 재요청
                }
            }
        }

        viewModel.nickname.observe(viewLifecycleOwner) { newNickname ->
            if (nicknameEditText.text.toString() != newNickname) {
                nicknameEditText.setText(newNickname)
                nicknameEditText.setSelection(newNickname.length) // 커서 마지막으로 이동시킴
            }


        }
        // 닉네임 중복 확인 후 다음 페이지로 이동
        viewModel.nicknameError.observe(viewLifecycleOwner){

            if(viewModel.gender.value?.isNotEmpty() == true && it==false){
                viewModel.setEnableNextBtn(true)
            }else{
                viewModel.setEnableNextBtn(false)
            }

        }

        viewModel.gender.observe(viewLifecycleOwner){newGender->
            if(newGender.isNotEmpty() && viewModel.nicknameError.value==false){
                viewModel.setEnableNextBtn(true)
            }else{
                viewModel.setEnableNextBtn(false)
            }

        }




        binding.btnFemale.setOnClickListener {
            binding.btnFemale.background = selectedDrawable
            binding.btnMale.background = nonSeletedDrawaable

//            var txt_nickname = binding.editNickName.text.toString()

//            User.nickname = txt_nickname
//            Log.d("회원가입 - User.nickname클릭", User.nickname.toString())
              viewModel.updateGender("FEMALE")
//            Log.d("회원가입 - 여자", User.gender.toString())

//            if (User.nickname != null && User.gender != null && viewModel.nicknameError.value==false && !isSpaced) {
//               viewModel.setEnableNextBtn(true)
//            } else {
//                Log.d("회원가입 - 닉네임이나 성별 null", User.gender.toString() + User.nickname.toString())
//                viewModel.setEnableNextBtn(false)
//            }
        }
        binding.btnMale.setOnClickListener {

            binding.btnFemale.background = nonSeletedDrawaable
            binding.btnMale.background = selectedDrawable


//            var txt_nickname = binding.editNickName.text.toString()
//            User.nickname = txt_nickname
//            Log.d("회원가입 - User.nickname 성별클릭", User.nickname.toString())
//            User.gender = "MALE"
            viewModel.updateGender("MALE")
//            Log.d("회원가입 - 남자", User.gender.toString())
//            if (User.nickname != null && User.gender != null && viewModel.nicknameError.value==false && !isSpaced) {
//                viewModel.setEnableNextBtn(true)
//            } else {
//                Log.d("회원가입 - 닉네임이나 성별 null", User.gender.toString() + User.nickname.toString())
//                viewModel.setEnableNextBtn(false)
//            }
        }

        binding.btnNext.setOnClickListener {

            User.nickname = viewModel.nickname.value
            User.gender = viewModel.gender.value

            //다른 입력들이 모두 완료되었다면
            findNavController().navigate(
                R.id.action_createAccountFragment03_to_createAccountFragment04,
                null
            )
            Log.d("회원가입 - User.nickname 학과로", User.nickname.toString())
            Log.d("회원가입 - User.gender 학과로", User.gender.toString())
        }



        // 닉네임 중복 api
        binding.btnNicknameOverlapCheck.setOnClickListener{
            //nickname에 공백이 있다면
            if (checkSpace(binding.editNickName.text.toString()) || binding.editNickName.text!!.isBlank()){
                val textColor = ContextCompat.getColor(requireContext(), R.color.R_50)
                binding.errorNickName.visibility = VISIBLE
                binding.errorNickName.text = "공백이 없는 문자를 입력해주세요"
                binding.errorNickName.setTextColor(textColor)
                isSpaced = true
            } else{
                viewModel.isDuplicationNickname()
                isSpaced = false
            }
        }
        // 닉네임 길이 표시
        viewModel.nickname.observe(viewLifecycleOwner) {
            viewModel.setNicknameLength(it.length) // 닉네임 길이 업뎃
        }
    }

    //공백이 있는 지 확인하고 있다면 오류 메시지를 넣음
    private fun checkSpace(text : String) : Boolean{
        var result = false
        text.forEach{ ch ->
            if (ch == ' ') result = true
        }
        return result
    }

}
