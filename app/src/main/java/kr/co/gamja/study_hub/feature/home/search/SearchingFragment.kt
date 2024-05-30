package kr.co.gamja.study_hub.feature.home.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentSearchingBinding
import kr.co.gamja.study_hub.global.ExtensionFragment.Companion.hideKeyboard
import kr.co.gamja.study_hub.global.RcvDecoration

class SearchingFragment : Fragment() {

    private lateinit var binding : FragmentSearchingBinding
    private val viewModel : RecommendViewModel by viewModels()
    private lateinit var adapter : RecommendItemAdapter
    private var lastLength = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_searching, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRcv()

        binding.apply{

            val beforeKeyword = arguments?.getString("beforeKeyword")
            if(beforeKeyword != null) {
                editSearch.setText(beforeKeyword)
            }

            keyword = editSearch.text.toString()

            isEnabled = false

            //observer 설정
            viewModel.recommendList.observe(viewLifecycleOwner, Observer{
                adapter.submitList(it)
            })

            // 툴바 설정
            val toolbar = searchMainToolbar
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
            (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

            // editText 검색어 지우기
            binding.btnTextDelete.setOnClickListener {
                binding.editSearch.text.clear()
                viewModel.resetList()
            }

            //검색 내용 변화 event
            binding.editSearch.addTextChangedListener (object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.resetList()
                    if (!s.isNullOrEmpty()){
                        viewModel.searchRecommend(s.toString())
                    } else {
                        isEnabled = true
                    }
                    lastLength = s?.length ?: 0
                }
                override fun afterTextChanged(s: Editable?) {
                }
            })

            //ime action (search) event >> 검색어 내용을 기반으로 search fragment로 이동
            editSearch.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH || event.keyCode == KeyEvent.KEYCODE_ENTER){
                    val keyword = editSearch.text.toString()
                    val bundle = Bundle()
                    bundle.putString("keyword", keyword)
                    arguments = bundle
                    findNavController().navigate(
                        R.id.action_search_recommend_to_search_main,
                        arguments
                    )
                    return@setOnEditorActionListener true
                }
                false
            }

            //recyclerView를 눌러도 keyboard를 숨길 수 있도록
            rcvSearchResult.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    hideKeyboard()
                }
                false
            }
        }
    }

    //recycler view 초기화
    private fun initRcv(){
        binding.apply{

            adapter = RecommendItemAdapter()
            val listener = object : RecommendItemAdapter.OnRecommendClick{
                override fun onClick(value : String) {
                    // SearchFragment로 이동
                    // argument에 value를 전달
                    val bundle = Bundle()
                    bundle.putString("keyword", value)
                    arguments = bundle
                    findNavController().navigate(
                        R.id.action_search_recommend_to_search_main,
                        arguments
                    )
                }
            }
            adapter.setOnClickListener(listener)

            rcvSearchResult.adapter = adapter
            rcvSearchResult.layoutManager = LinearLayoutManager(requireContext())

            val itemSpace = resources.getDimensionPixelSize(R.dimen.eight)
            val deco = RcvDecoration(itemSpace)
            rcvSearchResult.addItemDecoration(deco)
        }
    }
}