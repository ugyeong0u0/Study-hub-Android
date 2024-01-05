package kr.co.gamja.study_hub.feature.home.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentSearchBinding
import kr.co.gamja.study_hub.global.ExtensionFragment.Companion.hideKeyboard

class SearchFragment : Fragment() {
    private val msgTag = this.javaClass.simpleName
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()

    //글자수 처리를 위한 변수
    private var lastLength = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

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
        }
        
        //인기 버튼
        binding.btnPopularOrder.setOnClickListener{
            binding.isAll = false
            binding.isHot = true
            binding.isDepartment = false
            //가지고 있는 리스트를 배열 순서만 바꿔서
        }
        
        //학과 버튼
        binding.btnMajorOrder.setOnClickListener{
            binding.isAll = false
            binding.isHot = false
            binding.isDepartment = true
            //학과 순으로 순서 변경
        }
        
        // 에딧텍스트 자판 내리기
        binding.root.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    true
                }
                MotionEvent.ACTION_UP -> {
                    this.hideKeyboard()
                    v.performClick()
                    true
                }
                else -> false
            }
        }
        // 툴바 설정
        val toolbar = binding.searchMainToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        binding.iconBack.setOnClickListener {
            val navcontroller = findNavController()
            navcontroller.navigateUp() // 뒤로 가기
        }

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
        // 글자 입력시 돋보기 x자보이게 처리
        viewModel.searchWord.observe(viewLifecycleOwner) {
            viewModel.updateSearchImg()
        }
        // editText 검색어 지우기
        binding.btnTextDelete.setOnClickListener { binding.editSearch.text.clear() }

        //검색 클릭 시
        binding.editSearch.addTextChangedListener (object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.length > lastLength){
                    val isHot = binding.isHot ?: false
                    val isDepartment = binding.isDepartment ?: false
                    onUpdateStudys(s.toString(), isHot, isDepartment)
                }
                lastLength = s?.length ?: 0
            }
        })

        //recyclerView 설정

    }
    fun onUpdateStudys(s : String, isHot: Boolean, isDepartment: Boolean){
        val searchContent = s.substring(0,s.length-1)
        if (searchContent.length != 0){
            viewModel.fetchStudys(searchContent, isHot, isDepartment)
        }
    }
}