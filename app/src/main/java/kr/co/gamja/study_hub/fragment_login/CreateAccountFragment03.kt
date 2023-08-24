package kr.co.gamja.study_hub.fragment_login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.custom.FunctionLogin
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

        val functionLogin: FunctionLogin = FunctionLogin(requireContext())
        var fca03Layout_nickname=binding.fca03EditlayoutNickname
        var fca03_flag_nickname :Boolean=functionLogin.nicknameWatcher(fca03Layout_nickname)
        binding.fca03TxtPagenumber.text=getString(R.string.txt_pagenumber,3)
        binding.fca03BtnNext.setOnClickListener{
            findNavController().navigate(R.id.action_createAccountFragment03_to_createAccountFragment04,null)
        }
        if (fca03_flag_nickname){
            binding.fca03BtnNext.isEnabled=true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}
