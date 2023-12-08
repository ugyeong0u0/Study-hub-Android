package kr.co.gamja.study_hub.feature.mypage.myStudy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentWrittenStudyBinding

class WrittenStudyFragment : Fragment() {
    private lateinit var binding:FragmentWrittenStudyBinding
    private val viewModel : WrittenStudyViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding =DataBindingUtil.inflate(inflater,R.layout.fragment_written_study,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }
}