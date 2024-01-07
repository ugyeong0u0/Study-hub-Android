package kr.co.gamja.study_hub.feature.studypage.allcomments

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
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.StudyHubApi
import kr.co.gamja.study_hub.databinding.FragmentAllCommentsBinding

class AllCommentsFragment : Fragment() {
    private lateinit var binding : FragmentAllCommentsBinding
    private lateinit var viewModel: AllCommentsViewModel
    private lateinit var adapter: AllCommentsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_all_comments, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = AllCommentViewModelFactory(AuthRetrofitManager.api)
        viewModel =ViewModelProvider(this,factory)[AllCommentsViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        getValue() // postId viewModel에 저장

        // 툴바 설정
        val toolbar = binding.commentToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        binding.iconBack.setOnClickListener {
            findNavController().navigateUp() // 뒤로 가기
        }

        adapter = AllCommentsAdapter(requireContext())
        binding.recycleComments.adapter=adapter
        binding.recycleComments.layoutManager=LinearLayoutManager(requireContext())

        observeData() // 페이징 데이터 업뎃
    }
    private fun getValue() {
        val receiveBundle = arguments
        if (receiveBundle != null) {
            val value = receiveBundle.getInt("postId")
            viewModel.setPostId(value) // postId 설정
            viewModel.getCommentsList(value) // 댓 개수 가져오기
            Log.d(tag, " value: $value")
        } else Log.e(tag, "receiveBundle is Null")
    }
    private fun observeData(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allContentsFlow.collectLatest {
                pagingData-> adapter.submitData(pagingData)
            }
        }
    }

}

class AllCommentViewModelFactory(private val studyHubApi: StudyHubApi): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AllCommentsViewModel::class.java)){
            return AllCommentsViewModel(studyHubApi) as T
        }
        throw IllegalArgumentException("AllCommentViewModel 모름")
    }
}