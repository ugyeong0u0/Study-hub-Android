package kr.co.gamja.study_hub.feature.mypage.applicationhistory

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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
import kr.co.gamja.study_hub.data.repository.*
import kr.co.gamja.study_hub.databinding.FragmentApplicationHistoryBinding
import kr.co.gamja.study_hub.feature.studypage.apply.ApplicationFragmentDirections
import kr.co.gamja.study_hub.feature.studypage.studyContent.ContentFragmentDirections
import kr.co.gamja.study_hub.feature.studypage.studyHome.StudyMainViewModel
import kr.co.gamja.study_hub.global.CustomDialog
import kr.co.gamja.study_hub.global.CustomSnackBar
import kr.co.gamja.study_hub.global.OnDialogClickListener

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
        viewModel.updateListSize() // 리스트 개수 업데이트

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
                        Log.e(msg, "지우기 눌림")
                        val head =
                            requireContext().resources.getString(R.string.deleteEngagedStudy_title)
                        val sub =
                            requireContext().resources.getString(R.string.deleteApplyHistory_sub)
                        val no = requireContext().resources.getString(R.string.btn_cancel)
                        val yes = requireContext().resources.getString(R.string.btn_delete)
                        val dialog = CustomDialog(requireContext(), head, sub, no, yes)
                        dialog.showDialog()
                        dialog.setOnClickListener(object : OnDialogClickListener {
                            override fun onclickResult() {
                                viewModel.deleteApplyStudy(itemValue, object : CallBackListener {
                                    override fun isSuccess(result: Boolean) {
                                        if (result) {
                                            CustomSnackBar.make(
                                                binding.layoutRelative,
                                                getString(R.string.complete_deletion), null, true
                                            ).show()
                                            adapter.refresh() // 리사이클러뷰 refresh
                                            viewModel.updateListSize() // 신청 내역 리스트 개수 업데이트
                                        }
                                    }
                                })
                            }
                        })
                    }
                    2 -> {
                        Log.i(msg, "거절 이유 보기 눌림")
                        val bundle = Bundle()
                        bundle.putInt("studyId", itemValue)
                        findNavController().navigate(
                            R.id.action_global_checkRefusalReasonFragment,
                            bundle
                        )
                    }
                    3 -> {
                        Log.i(msg, "컨텐츠 보기 눌림")

                        val action = ApplicationFragmentDirections.actionGlobalStudyContentFragment(
                            true,
                            itemValue
                        )
                        findNavController().navigate(action)
                    }
                }
            }
        })

        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadState ->
                binding.applicationHistoryProgressBar.isVisible = loadState.refresh is LoadState.Loading
            }
        }
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