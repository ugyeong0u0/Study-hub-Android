package kr.co.gamja.study_hub.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentManualBinding


class ManualFragment : Fragment() {
    private lateinit var binding: FragmentManualBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_manual, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 툴바 설정
        val toolbar = binding.maunalToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""
        binding.iconBack.setOnClickListener {
            val navcontroller = findNavController()
            navcontroller.navigateUp() // 뒤로 가기
        }
        binding.btnNext.setOnClickListener{
            findNavController().navigate(R.id.action_global_createStudyFragment,null)
        }
    }
}