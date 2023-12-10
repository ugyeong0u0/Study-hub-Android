package kr.co.gamja.study_hub.feature.mypage.myStudy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentWrittenStudyBinding

class WrittenStudyFragment : Fragment() {
    private lateinit var binding:FragmentWrittenStudyBinding
    private val viewModel : WrittenStudyViewModel by viewModels()
    private val writtenStudyAdapter=WrittenStudyAdapter(requireContext())
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

        // 툴바 설정
        val toolbar = binding.writtenPageToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        setUpRecyclerView()
        observeData()
    }
    private fun setUpRecyclerView(){
        binding.recylerWrittenList.apply {
            layoutManager=LinearLayoutManager(requireContext())
            adapter=this@WrittenStudyFragment.writtenStudyAdapter
        }
    }
    private fun observeData(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.myStudyFlow.collectLatest {
                pagingData->writtenStudyAdapter.submitData(pagingData) // 데이터 업데이트
            }
        }
    }
}