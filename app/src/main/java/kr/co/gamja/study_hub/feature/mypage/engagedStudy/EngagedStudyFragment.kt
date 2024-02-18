package kr.co.gamja.study_hub.feature.mypage.engagedStudy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
import kr.co.gamja.study_hub.data.repository.CallBackStringListener
import kr.co.gamja.study_hub.data.repository.OnItemsClickListener
import kr.co.gamja.study_hub.data.repository.StudyHubApi
import kr.co.gamja.study_hub.databinding.FragmentEngagedStudyBinding
import kr.co.gamja.study_hub.feature.mypage.applicationhistory.ApplicationHistoryViewModel
import kr.co.gamja.study_hub.global.CustomDialog
import kr.co.gamja.study_hub.global.OnDialogClickListener

class EngagedStudyFragment : Fragment() {
    val msg = this.javaClass.simpleName
    private lateinit var binding: FragmentEngagedStudyBinding
    private lateinit var viewModel: EngagedStudyViewModel
    private lateinit var adapter: EngagedStudyAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_engaged_study, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = EngagedStudyViewModelFactory(AuthRetrofitManager.api)
        viewModel = ViewModelProvider(this, factory)[EngagedStudyViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        adapter = EngagedStudyAdapter(requireContext()).apply {
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
        val toolbar = binding.engagedStudyToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        binding.iconBack.setOnClickListener {
            findNavController().navigateUp() // 뒤로 가기
        }
        // 클릭 이벤트 x 눌렀을 때 - 변수명 변경 못함
        adapter.setOnItemsClickListener(object : OnItemsClickListener {
            override fun getItemValue(whatItem: Int, itemValue: Int) {
                when (whatItem) {
                    1 -> {
                        //todo("지우기 눌림")
                        val head =
                            requireContext().resources.getString(R.string.deleteEngagedStudy_title)
                        val sub =
                            requireContext().resources.getString(R.string.deleteEngagedStudy_sub)
                        val no = requireContext().resources.getString(R.string.btn_cancel)
                        val yes = requireContext().resources.getString(R.string.btn_delete)
                        val dialog = CustomDialog(requireContext(), head, sub, no, yes)
                        dialog.showDialog()
                        dialog.setOnClickListener(object : OnDialogClickListener {
                            override fun onclickResult() {
                                // TODO("삭제 누를 시 통신")

                            }
                        })
                    }
                }
            }
        })
        // 크롬통해 카카오톡으로 가기
        adapter.setCallBackStringListener(object : CallBackStringListener {
            override fun isSuccess(result: String) {
                val url = result
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        })

    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.engagedStudyFlow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }

}

class EngagedStudyViewModelFactory(private val studyHubApi: StudyHubApi) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((EngagedStudyViewModel::class.java)))
            return EngagedStudyViewModel(studyHubApi) as T
        throw IllegalArgumentException("ViewModel class 모름")
    }

}