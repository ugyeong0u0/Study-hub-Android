package kr.co.gamja.study_hub.feature.toolbar.bookmark

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.repository.AuthRetrofitManager
import kr.co.gamja.study_hub.data.repository.OnBookmarkClickListener
import kr.co.gamja.study_hub.data.repository.OnPostingIdClickListener
import kr.co.gamja.study_hub.data.repository.StudyHubApi
import kr.co.gamja.study_hub.databinding.FragmentBookmarkBinding
import kr.co.gamja.study_hub.global.CustomDialog
import kr.co.gamja.study_hub.global.OnDialogClickListener

class BookmarkFragment : Fragment() {
    private val tagMsg = this.javaClass.simpleName
    private lateinit var binding: FragmentBookmarkBinding
    private lateinit var viewModel: BookmarkViewModel
    private lateinit var adapter: BookmarkAdapter
    var isUser: Boolean = true // 로그인 유저(true)인지 아니면 둘러보기 유저인지(false)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_bookmark, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = BookmarkViewModelFactory(AuthRetrofitManager.api)
        viewModel = ViewModelProvider(this, factory = factory)[BookmarkViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        // 둘러보기인지 누른 페이지 bundle로 받음
        val receiveBundle = arguments
        if (receiveBundle != null) {
            isUser = receiveBundle.getBoolean("isUser")
//            Log.e(tagMsg, "유저인지$isUser")
        } else Log.e(
            tagMsg,
            "a bundle from mainHomeFragment is error" // todo("로그아웃 후 재로그인한 경우도 여기로 가는데 문제는 없음 ")
        )
        viewModel.isUserLogin.value = isUser // 회원 비회원인지 저장
        // 툴바 설정
        val toolbar = binding.bookMarkToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""


        binding.iconBack.setOnClickListener {
            val navcontroller = findNavController()
            navcontroller.navigateUp() // 뒤로 가기
        }
        // 북마크 조회 리사이클러뷰 연결
        adapter = BookmarkAdapter(requireContext())

        binding.recylerBookmark.adapter = adapter
        binding.recylerBookmark.layoutManager = LinearLayoutManager(requireContext())

        // 북마크 삭제 저장 api 연결
        adapter.setOnBookmarkClickListener(object : OnBookmarkClickListener {
            override fun onItemClick(tagId: String?, postId: Int?) {
                viewModel.saveDeleteBookmarkItem(postId)
            }
        })
        // 스터디 컨텐츠 자세히 보기
        adapter.setOnItemsClickListener(object : OnPostingIdClickListener {
            override fun getItemValue(whatItem: Int, postingId: PostingId) {
                when (whatItem) {
                    1 -> {
                        val action =
                            BookmarkFragmentDirections.actionGlobalStudyContentFragment(postingId.postId)
                        findNavController().navigate(action)
                    }
                    2 -> { // 신청하기 일 때
                        val bundle = Bundle()
                        bundle.putString("whatPage","bookmark") // 북마크 페이지에서 왔음을 알림, 신청후 백스택제거 때문
                        bundle.putInt("postId", postingId.postId)
                        bundle.putInt("studyId", postingId.studyId)
                        findNavController().navigate(R.id.action_global_applicationFragment, bundle)
                    }
                }
            }
        })

        observeData() // 페이징 데이터 업데이트
        viewModel.getBookmarkList() // 북마크 총 개수 업데이트

        // 전체 삭제
        // TODO("리스트 값이 있을 때만 삭제 버튼 가능하게 기능 추가 ")
        binding.btnDeleteAll.setOnClickListener {
            val head = requireContext().resources.getString(R.string.q_deleteBookmark)
            val no = requireContext().resources.getString(R.string.btn_cancel)
            val yes = requireContext().resources.getString(R.string.btn_delete)
            val dialog = CustomDialog(requireContext(), head, null, no, yes)
            dialog.showDialog()
            dialog.setOnClickListener(object : OnDialogClickListener {
                override fun onclickResult() {
                    // TODO("삭제 누를 시 통신")
                }
            })
        }

    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.myBookmarkFlow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }
}


class BookmarkViewModelFactory(private val authApi: StudyHubApi) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookmarkViewModel::class.java)) {
            return BookmarkViewModel(authApi) as T
        }
        throw IllegalArgumentException("ViewModel class 모름")
    }
}