package kr.co.gamja.study_hub.feature.mypage.applicationhistory

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.OnBookmarkClickListener
import kr.co.gamja.study_hub.data.repository.OnItemsClickListener
import kr.co.gamja.study_hub.data.repository.StudyHubApi
import kr.co.gamja.study_hub.databinding.FragmentApplicationHistoryBinding
import kr.co.gamja.study_hub.feature.studypage.studyHome.StudyMainViewModel

class ApplicationHistoryFragment : Fragment() {
    val msg = this.javaClass.simpleName
    private lateinit var binding: FragmentApplicationHistoryBinding
    private lateinit var viewModel: ApplicationHistoryViewModel
    private lateinit var adapter: ApplicationHistoryAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_application_history,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = ApplicationHistoryViewModelFactory(AuthRetrofitManager.api)
        viewModel = ViewModelProvider(this, factory)[ApplicationHistoryViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        adapter = ApplicationHistoryAdapter(requireContext()).apply {
            addLoadStateListener { loadState ->
                val isEmptyList = loadState.refresh is LoadState.NotLoading && itemCount == 0
                viewModel.isList.postValue(!isEmptyList)
            }
        }

        binding.recyclerEngagedStudy.adapter = adapter
        binding.recyclerEngagedStudy.layoutManager = LinearLayoutManager(requireContext())

        viewModel.setReloadTrigger()

        observeData()

        // 툴바 설정
        val toolbar = binding.applicationHistoryToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        binding.iconBack.setOnClickListener {
            findNavController().navigateUp() // 뒤로 가기
        }

        adapter.setOnItemsClickListener(object : OnItemsClickListener {
            override fun getItemValue(whatItem: Int, itemValue: Int) {
                when (whatItem) {
                    1 -> {
                        // todo()
                        Log.e(msg, "지우기 눌림")
                    }
                    2 -> {// todo()
                        Log.e(msg, "거절 이유 눌림")

                    }
                    3 -> { // todo()
                        Log.e(msg, "컨텐츠 보기 눌림")
                    }
                }
            }
        })
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.applicationHistoryFlow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }

}

class ApplicationHistoryViewModelFactory(private val studyHubApi: StudyHubApi) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((ApplicationHistoryViewModel::class.java)))
            return ApplicationHistoryViewModel(studyHubApi) as T
        throw IllegalArgumentException("ViewModel class 모름")
    }

}