package kr.co.gamja.study_hub.fragment_login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.User
import kr.co.gamja.study_hub.databinding.FragmentCreateAccount03Binding


class CreateAccountFragment03 : Fragment() {
    private var _binding:FragmentCreateAccount03Binding?=null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentCreateAccount03Binding.inflate(inflater,container,false)
        val view=binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 툴바 설정
        val toolbar = binding.createUserToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        binding.iconBack.setOnClickListener {
            val navcontroller = findNavController()
            navcontroller.navigateUp() // 뒤로 가기
        }
        binding.fca03TxtPagenumber.text=getString(R.string.txt_pagenumber,3)

        binding.fca03BtnFemale.setOnClickListener{
            var txt_nickname=binding.fca03Editnickname.text.toString()
            User.nickname=txt_nickname
            Log.d("회원가입 - User.nickname성별클릭",User.nickname.toString())
            User.gender="FEMALE"
            Log.d("회원가입 - 여자",User.gender.toString())

            if(User.nickname!=null && User.gender!=null){
                binding.fca03BtnNext.isEnabled=true
            }
            else{
                Log.d("회원가입 - 닉네임이나 성별 null",User.gender.toString()+User.nickname.toString())
            }
        }
        binding.fca03BtnMale.setOnClickListener{
            var txt_nickname=binding.fca03Editnickname.text.toString()
            User.nickname=txt_nickname
            Log.d("회원가입 - User.nickname 성별클릭",User.nickname.toString())
            User.gender="MALE"
            Log.d("회원가입 - 남자",User.gender.toString())
            if(User.nickname!=null && User.gender!=null){
                binding.fca03BtnNext.isEnabled=true
            }
            else{
                Log.d("회원가입 - 닉네임이나 성별 null",User.gender.toString()+User.nickname.toString())
            }
        }


       binding.fca03BtnNext.setOnClickListener{
            findNavController().navigate(R.id.action_createAccountFragment03_to_createAccountFragment04,null)
           Log.d("회원가입 - User.nickname 학과로",User.nickname.toString())
           Log.d("회원가입 - User.gender 학과로",User.gender.toString())
       }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}
