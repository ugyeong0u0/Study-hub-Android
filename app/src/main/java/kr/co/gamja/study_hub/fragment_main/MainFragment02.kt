package kr.co.gamja.study_hub.fragment_main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentMain02Binding

class MainFragment02 : Fragment() {
    private var _binding :FragmentMain02Binding?=null
    private val binding get() =_binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentMain02Binding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fm02BtnFileter.setOnClickListener{
            findNavController().navigate(R.id.action_mainFragment02_to_dialogFragment,null)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}