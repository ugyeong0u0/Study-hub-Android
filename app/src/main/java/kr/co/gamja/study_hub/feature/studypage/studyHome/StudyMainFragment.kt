package kr.co.gamja.study_hub.feature.studypage.studyHome

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.repository.*
import kr.co.gamja.study_hub.databinding.FragmentStudyMainBinding
import kr.co.gamja.study_hub.feature.home.MainHomeFragmentDirections
import kr.co.gamja.study_hub.global.CustomDialog
import kr.co.gamja.study_hub.global.CustomSnackBar
import kr.co.gamja.study_hub.global.OnDialogClickListener
import kotlin.properties.Delegates

// todo 스터디 컨텐츠 가는거 비회원 추가하기
class StudyMainFragment : Fragment() {
    private val msgTag = this.javaClass.simpleName
    private lateinit var binding: FragmentStudyMainBinding
    private lateinit var viewModel: StudyMainViewModel
    private lateinit var adapter: StudyMainAdapter
    private var doubleBackPressed = false

    // 같은 버튼 눌렸을 때 변화 x 하기
    private var allBtnEnable = true
    private var popularBtnEnable = false

    // 전체 / 인기 선택 버튼 색
    private lateinit var selectedDrawable: Drawable
    private lateinit var nonSelectedDrawable: Drawable
    private var selectedTextColor by Delegates.notNull<Int>()
    private var nonSelectedTextColor by Delegates.notNull<Int>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (doubleBackPressed) {
                        requireActivity().finish()
                    } else {
                        doubleBackPressed = true
                        val activity = requireActivity() as AppCompatActivity
                        val bottomView = activity.findViewById<View>(R.id.bottom_nav)
                        CustomSnackBar.make(
                            binding.layoutRelative,
                            getString(R.string.btnBack_login), bottomView, false
                        ).show()
                        view?.postDelayed({ doubleBackPressed = false }, 2000)
                    }
                }
            }
        )
    }

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
        val factory = StudyMainViewModelFactory(AuthRetrofitManager.api, RetrofitManager.api)
        viewModel = ViewModelProvider(this, factory)[StudyMainViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // 회원 비회원 여부 결정
        viewModel.isUserOrNotUser(object : CallBackListener {
            override fun isSuccess(result: Boolean) {
                Log.e(msgTag,"1"+viewModel.isUserLogin.value!!.toString())
            }
        })


        selectedDrawable =
            ResourcesCompat.getDrawable(resources, R.drawable.bg_black_radius_20, null)!!
        nonSelectedDrawable =
            ResourcesCompat.getDrawable(resources, R.drawable.bg_30_radius_20, null)!!
        selectedTextColor = ContextCompat.getColor(requireContext(), R.color.syswhite)
        nonSelectedTextColor = ContextCompat.getColor(requireContext(), R.color.BG_90)


        // 스터디 조회 리사이클러뷰 연결 및 스터디 개수 확인
        adapter = StudyMainAdapter(requireContext()).apply {
            Log.e(msgTag,"2"+viewModel.isUserLogin.value!!.toString())
            addLoadStateListener { loadState ->
                val isEmptyList = loadState.refresh is LoadState.NotLoading && itemCount == 0
                viewModel.isList.postValue(!isEmptyList)
            }
        }
        viewModel.isUserLogin.observe(viewLifecycleOwner){
            if(it){
                adapter.isUserLogin=it
                Log.e(msgTag,"2"+viewModel.isUserLogin.value!!.toString())
            }
        }
        binding.recyclerStudyMain.adapter = adapter
        binding.recyclerStudyMain.layoutManager = LinearLayoutManager(requireContext())

        viewModel.setReloadTrigger()
        observeData()

        // 툴바 설정
        val toolbar = binding.studyMainToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        binding.iconBookmark.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean("isUser", viewModel.isUserLogin.value!!)
            findNavController().navigate(
                R.id.action_global_mainBookmarkFragment,
                bundle
            )
        }

        // 스터디 생성하기
        binding.btnFlaot.setOnClickListener {
            if (viewModel.isUserLogin.value == true) {
                // 스터디 새로 생성함을 알리는 bundle(수정하기랑 구분하기 위해)
                val bundle = Bundle()
                bundle.putBoolean("isCorrectStudy", false)
                findNavController().navigate(
                    R.id.action_StudyFragment01_to_createStudyFragment,
                    bundle
                )
            } else {
                needLogin()
            }
        }

        // 스터디 전체 조회 버튼
        binding.btnAllStudy.setOnClickListener {
            if (!allBtnEnable) { // 같은 버튼 눌리지 않게
                setButtonOption(false) // 버튼 색, 글자색 변경
                viewModel.setIsHot(false)
                viewModel.setReloadTrigger()
                observeData()
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
                allBtnEnable = false
                popularBtnEnable = true
            }
        }

        // 북마크 삭제 저장 api연결- 북마크 뷰모델 공유
        adapter.setOnBookmarkClickListener(object : OnBookmarkClickListener {
            override fun onItemClick(tagId: String?, postId: Int?) {

                if (viewModel.isUserLogin.value == true) {
                    Log.i("북마크4 onItemClickListener콜백1", "")
                    viewModel.saveDeleteBookmarkItem(postId, object : CallBackListener {
                        override fun isSuccess(result: Boolean) {
                            Log.i("북마크5 onItemClickListener콜백2", "")
                            if (result)
                                Log.d(msgTag, "회원인 경우")
                            else
                                Log.d(msgTag, "비회원인 경우")
                        }
                    })
                } else {
                    needLogin() // 비회원
                }
            }
        })
        // 리스트 아이템 자체 클릭
        adapter.setViewClickListener(object : OnViewClickListener {
            override fun onViewClick(postId: Int?) {
                val action = MainHomeFragmentDirections.actionGlobalStudyContentFragment(
                    viewModel.isUserLogin.value!!, postId!!
                )
                findNavController().navigate(action)
            }
        })

        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadState ->
                binding.mainHomeProgressBar.isVisible = loadState.refresh is LoadState.Loading
                binding.iconBookmark.isEnabled = !binding.mainHomeProgressBar.isVisible
            }
        }
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

    fun needLogin() {
        Log.d(msgTag, "비회원 누름")
        val head =
            requireContext().resources.getString(R.string.head_goLogin)
        val sub =
            requireContext().resources.getString(R.string.sub_goLogin)
        val yes =
            requireContext().resources.getString(R.string.txt_login)
        val no = requireContext().resources.getString(R.string.btn_cancel)
        val dialog =
            CustomDialog(requireContext(), head, sub, no, yes)
        dialog.showDialog()
        dialog.setOnClickListener(object : OnDialogClickListener {
            override fun onclickResult() { // 로그인하러가기 누를시
                findNavController().navigate(R.id.action_global_loginFragment, null)
            }
        })
    }
}

class StudyMainViewModelFactory(
    private val studyHubApiAuth: StudyHubApi,
    private val studyHubApi: StudyHubApi
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((StudyMainViewModel::class.java)))
            return StudyMainViewModel(studyHubApiAuth, studyHubApi) as T
        throw IllegalArgumentException("ViewModel class 모름")
    }

}