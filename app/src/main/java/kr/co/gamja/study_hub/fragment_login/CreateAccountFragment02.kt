package kr.co.gamja.study_hub.fragment_login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.User

import kr.co.gamja.study_hub.databinding.FragmentCreateAccount02Binding


class CreateAccountFragment02 : Fragment() {
    private var _binding: FragmentCreateAccount02Binding?=null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentCreateAccount02Binding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fca02TxtPagenumber.text=getString(R.string.txt_pagenumber,2)

        // 비번확인
        val txt_pass01=binding.fca02EditlayoutPassword.editText.toString()
        val txt_pass02=binding.fca02EditlayoutPassword02.editText.toString()

        binding.fca02BtnOk.setOnClickListener{
            if(txt_pass01==txt_pass02){
                User.password=txt_pass01
                binding.fca02BtnNext.isEnabled=true
            }
            else{
                Toast.makeText(requireContext(),"비밀번호 불일치", Toast.LENGTH_LONG).show()
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