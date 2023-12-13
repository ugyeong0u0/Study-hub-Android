package kr.co.gamja.study_hub.feature.mypage.myStudy

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.OnItemsClickListener
import kr.co.gamja.study_hub.data.repository.StudyHubApi
import kr.co.gamja.study_hub.databinding.FragmentWrittenStudyBinding

class WrittenStudyFragment : Fragment() {
    private lateinit var binding: FragmentWrittenStudyBinding
    private lateinit var viewModel: WrittenStudyViewModel
    private lateinit var writtenStudyAdapter: WrittenStudyAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_written_study, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = WrittenStudyViewModelFactory(AuthRetrofitManager.api)
        viewModel = ViewModelProvider(this, factory)[WrittenStudyViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // 툴바 설정
        val toolbar = binding.writtenPageToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""
        writtenStudyAdapter = WrittenStudyAdapter(requireContext())
        setUpRecyclerView()
        observeData()
        viewModel.updateMyStudyListSize()
    }

    private fun setUpRecyclerView() {
        val writtenAdapter = this@WrittenStudyFragment.writtenStudyAdapter
        binding.recylerWrittenList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = writtenAdapter
        }
        writtenAdapter.setOnItemClickListener(object : OnItemsClickListener {
            override fun getItemValue(whatItem: Int, itemValue: Int) {
                when (whatItem) {
                    // todo("api 연결 혹은 페이지 변경 연결")
                    // 마감 클릭시
                    1 -> {
                        Log.d(tag, "마감 버튼 눌림")
                    }
                    // 참여자 클릭시
                    2 -> {
                        Log.d(tag, "참여자 버튼 눌림")
                    }
                    // 스터디 수정 클릭시
                    3 -> {
                        Log.d(tag, "스터디 수정 버튼 눌림")
                    }
                }
            }
        })
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.myStudyFlow.collectLatest { pagingData ->
                writtenStudyAdapter.submitData(pagingData) // 데이터 업데이트
            }
        }
    }
}

// 생성자 있는 뷰모델 인스턴스 생성
class WrittenStudyViewModelFactory(private val studyHubApi: StudyHubApi) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WrittenStudyViewModel::class.java)) { // 타입 확인
            return WrittenStudyViewModel(studyHubApi) as T
        }
        throw IllegalArgumentException("ViewModel class 모름")
    }
}