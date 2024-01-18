package kr.co.gamja.study_hub.feature.studypage.studyHome

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.datastore.App
import kr.co.gamja.study_hub.data.repository.*
import kr.co.gamja.study_hub.databinding.FragmentStudyMainBinding
import kr.co.gamja.study_hub.feature.home.MainHomeFragmentDirections
import kr.co.gamja.study_hub.feature.login.LoginCallback
import kotlin.properties.Delegates

class StudyMainFragment : Fragment() {
    private val msgTag = this.javaClass.simpleName
    private lateinit var binding: FragmentStudyMainBinding
    private lateinit var viewModel: StudyMainViewModel
    private lateinit var adapter: StudyMainAdapter

    // 같은 버튼 눌렸을 때 변화 x 하기
    private var allBtnEnable = true
    private var popularBtnEnable = false

    // 전체 / 인기 선택 버튼 색
    private lateinit var selectedDrawable: Drawable
    private lateinit var nonSelectedDrawable: Drawable
    private var selectedTextColor by Delegates.notNull<Int>()
    private var nonSelectedTextColor by Delegates.notNull<Int>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_study_main, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // todo("회원 비회원 여부")
        val factory = StudyMainViewModelFactory(AuthRetrofitManager.api)
        viewModel = ViewModelProvider(this, factory)[StudyMainViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        selectedDrawable =
            ResourcesCompat.getDrawable(resources, R.drawable.bg_black_radius_20, null)!!
        nonSelectedDrawable =
            ResourcesCompat.getDrawable(resources, R.drawable.bg_30_radius_20, null)!!
        selectedTextColor = ContextCompat.getColor(requireContext(), R.color.syswhite)
        nonSelectedTextColor = ContextCompat.getColor(requireContext(), R.color.BG_90)


        // 스터디 조회 리사이클러뷰 연결
        adapter = StudyMainAdapter(requireContext())
        binding.recyclerStudyMain.adapter = adapter
        binding.recyclerStudyMain.layoutManager = LinearLayoutManager(requireContext())

        viewModel.setReloadTrigger()
        observeData()
        viewModel.getStudyList() // 스터디 총개수

        // 툴바 설정
        val toolbar = binding.studyMainToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""
        binding.iconBookmark.setOnClickListener {
            findNavController().navigate(
                R.id.action_global_mainBookmarkFragment,
                null
            )
        }
        binding.iconAlarm.setOnClickListener {
            findNavController().navigate(
                R.id.action_global_mainAlarmFragment,
                null
            )
        }

        // 스터디 생성하기
        binding.btnFlaot.setOnClickListener {
            findNavController().navigate(R.id.action_StudyFragment01_to_createStudyFragment, null)
        }

        // 스터디 전체 조회 버튼
        binding.btnAllStudy.setOnClickListener {
            if (!allBtnEnable) { // 같은 버튼 눌리지 않게
                setButtonOption(false) // 버튼 색, 글자색 변경
                viewModel.setIsHot(false)
                viewModel.setReloadTrigger()
                observeData()
                viewModel.getStudyList() // 스터디 게시글 총 개수 업데이트
                allBtnEnable = true
                popularBtnEnable = false
            }
        }

        // 스터디 인기 조회 버튼
        binding.btnPopularOrder.setOnClickListener {
            if (!popularBtnEnable) { // 같은 버튼 눌리지 않게
                setButtonOption(true) // 버튼 색, 글자색 변경
                viewModel.setIsHot(true)
                viewModel.setReloadTrigger()
                observeData()
                viewModel.getStudyList() // 스터디 게시글 총 개수 업데이트
                allBtnEnable = false
                popularBtnEnable = true
            }
        }

        // 북마크 삭제 저장 api연결- 북마크 뷰모델 공유
        adapter.setOnBookmarkClickListener(object : OnBookmarkClickListener {
            override fun onItemClick(tagId: String?, postId: Int?) {
                Log.i("북마크4 onItemClickListener콜백1", "")
                viewModel.saveDeleteBookmarkItem(postId, object : CallBackListener {
                    override fun isSuccess(result: Boolean) {
                        Log.i("북마크5 onItemClickListener콜백2", "")
                        if (result)
                            Log.d(msgTag, "회원인 경우")
                        else
                            Log.d(msgTag, ",비회원인 경우")
                    }
                })
            }
        })
        // 리스트 아이템 자체 클릭
        adapter.setViewClickListener(object : OnViewClickListener {
            override fun onViewClick(postId: Int?) {
                val action = MainHomeFragmentDirections.actionGlobalStudyContentFragment(postId!!)
                findNavController().navigate(action)
            }
        })

    }


    // 버튼색 글자색 변경
    private fun setButtonOption(isHot: Boolean) {
        if (isHot) {
            binding.btnAllStudy.apply {
                background = nonSelectedDrawable
                setTextColor(nonSelectedTextColor)
            }
            binding.btnPopularOrder.apply {
                background = selectedDrawable
                setTextColor(selectedTextColor)
            }
        } else {
            binding.btnAllStudy.apply {
                background = selectedDrawable
                setTextColor(selectedTextColor)
            }
            binding.btnPopularOrder.apply {
                background = nonSelectedDrawable
                setTextColor(nonSelectedTextColor)
            }
        }

    }

    private fun observeData() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.studyMainFlow.collectLatest { pagingData ->
                adapter.submitData(pagingData)

                delay(300) // 데이터가 로딩되기전에 리사이클러뷰 아이템 포지션가져와서 dalay 줌 todo("300밀리초가 적절한 시간인지...")

                binding.recyclerStudyMain.post {
                    val firstVisibleItemPosition =
                        (binding.recyclerStudyMain.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                    Log.d("리사이클러뷰 상단 postion : ", firstVisibleItemPosition.toString())
                    if (firstVisibleItemPosition > 0) {
                        binding.recyclerStudyMain.scrollToPosition(0)

                    }
                }
            }
        }
    }
}

class StudyMainViewModelFactory(private val studyHubApi: StudyHubApi) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((StudyMainViewModel::class.java)))
            return StudyMainViewModel(studyHubApi) as T
        throw IllegalArgumentException("ViewModel class 모름")
    }

}