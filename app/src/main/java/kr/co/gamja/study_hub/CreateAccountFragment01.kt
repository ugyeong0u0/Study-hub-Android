package kr.co.gamja.study_hub

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.databinding.FragmentCreateAccount01Binding



class CreateAccountFragment01 : Fragment() {
    private var _binding: FragmentCreateAccount01Binding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateAccount01Binding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // UI페이지 번호 설정
        binding.fcaTxtPagenumber.text=getString(R.string.txt_pagenumber,1)
        binding.fcaBtnNext.setOnClickListener{
            findNavController().navigate(R.id.action_createAccountFragment01_to_createAccountFragment02,null)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}