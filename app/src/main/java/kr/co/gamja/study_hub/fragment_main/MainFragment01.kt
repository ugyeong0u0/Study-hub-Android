package kr.co.gamja.study_hub.fragment_main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentMain01Binding

class MainFragment01 : Fragment() {
    private var _binding : FragmentMain01Binding?=null
    private val binding get() =_binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentMain01Binding.inflate(layoutInflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 검색창으로 넘어감
        binding.fm01BtnSearch.setOnClickListener{
            findNavController().navigate(R.id.action_mainFragment01_to_mainFragment02,null)
        }
        // 설명으로 넘어감
        binding.goGuide.setOnClickListener{
            findNavController().navigate(R.id.action_mainFragment01_to_mainFragment03,null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}