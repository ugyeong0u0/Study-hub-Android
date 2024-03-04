package kr.co.gamja.study_hub.feature.home.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.model.ContentXXXX
import kr.co.gamja.study_hub.data.repository.OnViewClickListener
import kr.co.gamja.study_hub.databinding.FragmentSearchBinding
import kr.co.gamja.study_hub.feature.home.ItemOnRecruitingAdapter
import kr.co.gamja.study_hub.feature.studypage.studyContent.ContentFragmentDirections
import kr.co.gamja.study_hub.global.ExtensionFragment.Companion.hideKeyboard

class SearchFragment : Fragment() {
    private val msgTag = this.javaClass.simpleName
    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: SearchViewModel

    private lateinit var searchItemAdapter : SearchItemAdapter
    private var page = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //viewModel 초기화
        viewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)

        //keyword로 받은 데이터가 있다면 view model에 저장
        val keyword = arguments?.getString("keyword")

        if (keyword != null) {
            viewModel.saveKeyword(keyword)

        }
        binding.keyword = viewModel.getKeyword()

        //검색 결과 리스트
        viewModel.studys.observe(viewLifecycleOwner) { studys ->
            Log.d("SearchFragment", "${studys}")
            searchItemAdapter.updateList(studys)
            binding.isEmpty = studys.isEmpty()
            binding.cntItem = "${studys.size}개"
        }

        //ViewCreated 시 list update
        viewModel.fetchStudys(
            isHot = false,
            isDepartment = false
        )

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val receiveBundle = arguments
        if (receiveBundle != null) {
            viewModel.isUserLogin.value= receiveBundle.getBoolean("isUser")
        //Log.e(tagMsg, "유저인지$isUser")
        } else Log.e(
            msgTag,
            "a bundle from mainHomeFragment is error" // todo("로그아웃 후 재로그인한 경우도 여기로 가는데 문제는 없음 ")
        )

        //버튼 확인
        binding.isAll = true
        binding.isHot = false
        binding.isDepartment = false
        
        //전체 버튼
        binding.btnAllStudy.setOnClickListener{
            binding.isAll = true
            binding.isHot = false
            binding.isDepartment = false
            //조회 순서 그대로
            if (binding.editSearch.text.toString().length > 0){
                viewModel.fetchStudys(false, false)
            }
        }
        
        //인기 버튼
        binding.btnPopularOrder.setOnClickListener{
            binding.isAll = false
            binding.isHot = true
            binding.isDepartment = false
            //인기 순서
            if (binding.editSearch.text.toString().length > 0){
                viewModel.fetchStudys(true, false)
            }
        }
        
        //학과 버튼
        binding.btnMajorOrder.setOnClickListener{
            binding.isAll = false
            binding.isHot = false
            binding.isDepartment = true
            //학과 순으로 순서 변경
            if (binding.editSearch.text.toString().length > 0){
                viewModel.fetchStudys( false, true)
            }
        }

        // 툴바 설정
        val toolbar = binding.searchMainToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        binding.iconBack.setOnClickListener {
            findNavController().navigateUp() // 뒤로 가기
        }

        binding.iconBookmark.setOnClickListener {
            findNavController().navigate(
                R.id.action_global_mainBookmarkFragment,
                null
            )
        }
        // SearchingFragment로 이동
        binding.btnTextDelete.setOnClickListener {
            //초기화
            findNavController().navigate(
                R.id.action_search_to_search_recommend,
                null
            )
        }

        //editSearch가 Focus되면 navigateUp()
        binding.editSearch.setOnTouchListener { _, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                val bundle = Bundle()
                bundle.putString("beforeKeyword", viewModel.getKeyword())
                arguments = bundle
                findNavController().navigate(
                    R.id.action_search_to_search_recommend,
                    arguments
                )
                return@setOnTouchListener true
            }
            false
        }

        binding.editSearch.setOnClickListener{
            //이전 데이터를 가지고 있어야 함
            val bundle = Bundle()
            bundle.putString("beforeKeyword", viewModel.getKeyword())
            arguments = bundle
            findNavController().navigate(
                R.id.action_search_to_search_recommend,
                arguments
            )
        }

        //recyclerView 설정
        // 모집중 스터디 어댑터 연결
        searchItemAdapter = SearchItemAdapter(requireContext())
        searchItemAdapter.setOnClickListener(object : SearchItemAdapter.OnViewClickListener{
            override fun onClick(action: Int) {
                val navigateAction = ContentFragmentDirections.actionGlobalStudyContentFragment(viewModel.isUserLogin.value!!,action)
                findNavController().navigate(
                    navigateAction
                )
            }
        })
        val _layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerStudy.apply{
            adapter = searchItemAdapter
            layoutManager = _layoutManager
            addItemDecoration(RecyclerViewDecoration(30))
            addOnScrollListener(object : OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val visibleItemCount = _layoutManager.childCount
                    val totalItemCount = _layoutManager.itemCount
                    val firstVisibleItem = _layoutManager.findFirstVisibleItemPosition()

                    if ((visibleItemCount + firstVisibleItem) >= totalItemCount && firstVisibleItem >= 0) {
                        //데이터를 받아올 함수
                        page += 1
                        lifecycleScope.launch(Dispatchers.IO){
                            val isHot = binding.isHot ?: false
                            val isDepartment = binding.isDepartment ?: false
                            viewModel.addSearchData(
                                isHot = isHot,
                                isDepartment = !isDepartment,
                                page = page
                            )
                        }
                    }
                }
            })
        }
    }
}