package kr.co.gamja.study_hub.fragment_login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentCreate05EndBinding


class Create05EndFragment : Fragment() {
    private var _binding : FragmentCreate05EndBinding?=null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentCreate05EndBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fca05BtnStart.setOnClickListener{
            findNavController().navigate(R.id.action_create05EndFragment_to_login)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}