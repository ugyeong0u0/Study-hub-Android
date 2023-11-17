package kr.co.gamja.study_hub.feature.studypage.studyHome

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentStudyMainBinding
import kr.co.gamja.study_hub.feature.toolbar.bookmark.BookmarkCallback
import kr.co.gamja.study_hub.feature.toolbar.bookmark.BookmarkViewModel
import kr.co.gamja.study_hub.feature.toolbar.bookmark.OnItemClickListener

class StudyMainFragment : Fragment() {
    private lateinit var binding: FragmentStudyMainBinding
    private val viewModel: StudyMainViewModel by viewModels()
    private val bookmarkViewModel: BookmarkViewModel by activityViewModels()
    private var page = 0 // 스터디 조회 시작 페이지
    private var isLastPage = false // 스터디 조회 마지막 페이지인지
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_study_main, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
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

        // 스터디 조회 리사이클러뷰 연결
        val adapter = StudyMainAdapter(requireContext())
        binding.recyclerStudyMain.adapter = adapter
        binding.recyclerStudyMain.layoutManager = LinearLayoutManager(requireContext())

        // 스터디 조회 api통신
        viewModel.getStudyPosts(adapter, page, object : getStudyCallback {
            override fun isLastPage(lastPage: Boolean) {
                isLastPage = lastPage
                Log.d(tag, "스터디조회 마지막 페이지?" + isLastPage)
            }
        })
        // 스터디 조회- 리사이클러뷰 페이지네이션
        binding.recyclerStudyMain.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                if (!isLastPage && lastVisibleItemPosition == 9) {
                    page++ // 페이지 +1
                    Log.d(tag, "라스트 페이지2-" + lastVisibleItemPosition.toString())
                    viewModel.getStudyPosts(adapter, page, object : getStudyCallback {
                        override fun isLastPage(lastPage: Boolean) {
                            //TODO("써야함?")
                        }
                    })
                } else {
                    Toast.makeText(requireContext(), "마지막 페이지임 ", Toast.LENGTH_SHORT).show()
                }
            }
        })
        // 북마크 삭제 저장 api연결- 북마크 뷰모델 공유
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(tagId: String?, postId: Int?) {
                bookmarkViewModel.saveDeleteBookmarkItem(postId)
            }
        })

    }



}