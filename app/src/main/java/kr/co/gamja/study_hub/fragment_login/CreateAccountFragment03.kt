package kr.co.gamja.study_hub.fragment_login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        var txt_nickname=binding.fca03EditlayoutNickname.editText.toString()
        binding.fca03TxtPagenumber.text=getString(R.string.txt_pagenumber,3)

        User.nickname=txt_nickname
        binding.fca03BtnFemale.setOnClickListener{
            User.gender="FEMALE"
        }
        binding.fca03BtnMale.setOnClickListener{
            User.gender="MALE"
        }
        if(User.nickname!=null && User.gender!=null){
            binding.fca03BtnNext.isEnabled=true
        }

       binding.fca03BtnNext.setOnClickListener{
            findNavController().navigate(R.id.action_createAccountFragment03_to_createAccountFragment04,null)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}
