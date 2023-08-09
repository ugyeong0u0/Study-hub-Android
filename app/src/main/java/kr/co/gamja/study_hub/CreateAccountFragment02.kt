package kr.co.gamja.study_hub

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.custom.FunctionLogin
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
        val functionLogin: FunctionLogin = FunctionLogin(requireContext())
        binding.fca02TxtPagenumber.text=getString(R.string.txt_pagenumber,2)
        binding.fca02BtnNext.setOnClickListener{
            findNavController().navigate(R.id.action_createAccountFragment02_to_createAccountFragment03,null)
        }
        // 비번확인
        var fca02Layout_password=binding.fca02EditlayoutPassword
        var caf02_flag_password :Boolean = functionLogin.loginTextWatcher(null,fca02Layout_password)
        if (caf02_flag_password){
            binding.fca02BtnNext.isEnabled=true
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}