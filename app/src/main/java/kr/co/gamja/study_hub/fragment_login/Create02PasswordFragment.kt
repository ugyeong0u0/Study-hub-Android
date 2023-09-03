package kr.co.gamja.study_hub.fragment_login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.model.User
import kr.co.gamja.study_hub.databinding.FragmentCreate02PasswordBinding


class Create02PasswordFragment : Fragment() {
    private var _binding: FragmentCreate02PasswordBinding?=null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentCreate02PasswordBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 툴바 설정
        val toolbar = binding.createPassToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        binding.iconBack.setOnClickListener {
            val navcontroller = findNavController()
            navcontroller.navigateUp() // 뒤로 가기
        }

        binding.fca02TxtPagenumber.text=getString(R.string.txt_pagenumber,2)


        binding.fca02BtnOk.setOnClickListener{
            // 비번확인
            val txt_pass01=binding.fca02EditPassword.text.toString()
            val txt_pass02=binding.fca02EditPassword02.text.toString()
            Log.d("회원가입 - 비번1",txt_pass01)
            Log.d("회원가입 - 비번2",txt_pass02)

            if(txt_pass01.equals(txt_pass02)){
                User.password=txt_pass01
                binding.fca02BtnNext.isEnabled=true
                Log.d("회원가입 - User.Password", User.password.toString())
                Log.e("회원가입 - 비번 일치","")
            }
            else{
                Toast.makeText(requireContext(),"비밀번호 불일치", Toast.LENGTH_LONG).show()
                Log.e("회원가입 - 비번 불일치","")
            }
        }
        binding.fca02BtnNext.setOnClickListener{
            findNavController().navigate(R.id.action_createAccountFragment02_to_createAccountFragment03,null)
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}