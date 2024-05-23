package kr.co.gamja.study_hub.feature.mypage.myStudy

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.repository.*
import kr.co.gamja.study_hub.databinding.FragmentWrittenStudyBinding
import kr.co.gamja.study_hub.feature.mypage.MyInfoFragment
import kr.co.gamja.study_hub.feature.studypage.apply.ApplicationFragmentDirections
import kr.co.gamja.study_hub.feature.studypage.studyContent.correctStudy.BottomSheetFragment
import kr.co.gamja.study_hub.feature.toolbar.bookmark.PostingId
import kr.co.gamja.study_hub.global.CustomDialog
import kr.co.gamja.study_hub.global.OnDialogClickListener

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

        binding.iconBack.setOnClickListener {
            findNavController().navigateUp() // 뒤로 가기
        }

        //전체 삭제 버튼
        binding.btnDeleteAll.setOnClickListener{
            viewModel.deleteAllStudy()
        }

        writtenStudyAdapter = WrittenStudyAdapter(requireContext())
        binding.recylerWrittenList.adapter = writtenStudyAdapter
        binding.recylerWrittenList.layoutManager = LinearLayoutManager(requireContext())
        //item 별 three dot 메뉴 클릭 이벤트 추가
        writtenStudyAdapter.setOnMenuClickListener(object: OnMenuClickListener{
            override fun onClickThreeDot(postId : Int) {
                val bundle = Bundle()
                bundle.putInt("postId", postId)
                val modal = BottomSheetFragment()
                modal.arguments = bundle
                modal.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
                modal.show(parentFragmentManager, modal.tag)
            }
        })

//        setUpRecyclerView()
        observeData()
        viewModel.updateMyStudyListSize()

        writtenStudyAdapter.setOnItemClickListener(object: OnPostingIdClickListener {
            override fun getItemValue(whatItem: Int, postingId: PostingId) {
                val navController = findNavController()
                when (whatItem) {
                    // todo("api 연결 혹은 페이지 변경 연결")
                    // 마감 클릭시
                    1 -> {
                        Log.d(tag, "마감 버튼 눌림")
                        val head =
                            requireContext().resources.getString(R.string.head_shutdownStudy)
                        val sub =
                            requireContext().resources.getString(R.string.sub_shutdownStudy)
                        val yes =
                            requireContext().resources.getString(R.string.endStudy)
                        val no = requireContext().resources.getString(R.string.btn_no)
                        val dialog =
                            CustomDialog(requireContext(), head, sub, no, yes)
                        dialog.showDialog()
                        dialog.setOnClickListener(object : OnDialogClickListener {
                            override fun onclickResult() { // 마감 누를시
                                viewModel.shutDownStudy(postingId.postId, object : CallBackListener {
                                    override fun isSuccess(result: Boolean) {
                                        if (result) {
                                            writtenStudyAdapter.refresh() // 재로딩
                                        }
                                    }
                                })
                            }
                        })

                    }
                    // 참여자 클릭시
                    2 -> {
                        Log.d(tag, "참여자 버튼 눌림")
                        val bundle = Bundle()
                        Log.d("Participant", "${postingId.studyId}")
                        bundle.putInt("postId", postingId.postId)
                        bundle.putInt("studyId",postingId.studyId)
                        navController.navigate(R.id.action_writtenStudyFragment_to_participantFragment,bundle)
                        // todo("아이템 받는거까지")
                    }
                    // 스터디 수정 클릭시
                    3 -> {
                        Log.d(tag, "스터디 수정 버튼 눌림 -> 배포땜에 스터디 상세 보기로 변경")
                        val action = ApplicationFragmentDirections.actionGlobalStudyContentFragment(
                            true,
                            postingId.postId
                        )
                        findNavController().navigate(action)
                    }
                }
            }
        })

        lifecycleScope.launch {
            writtenStudyAdapter.loadStateFlow.collectLatest { loadState ->
                binding.writtenProgressBar.isVisible = loadState.refresh is LoadState.Loading
            }
        }

    }

    private fun setUpRecyclerView() {
        val writtenAdapter = this@WrittenStudyFragment.writtenStudyAdapter
        binding.recylerWrittenList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = writtenAdapter
        }
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