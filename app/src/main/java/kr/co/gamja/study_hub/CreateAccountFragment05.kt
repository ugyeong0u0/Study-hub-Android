package kr.co.gamja.study_hub

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.databinding.FragmentCreateAccount05Binding


class CreateAccountFragment05 : Fragment() {
    private var _binding : FragmentCreateAccount05Binding?=null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentCreateAccount05Binding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fca05BtnStart.setOnClickListener{
            findNavController().navigate(R.id.action_createAccountFragment05_to_createAccountFragment06)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}