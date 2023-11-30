package kr.co.gamja.study_hub.feature.studypage.studyHome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.repository.OnViewClickListener
import kr.co.gamja.study_hub.databinding.FragmentStudyMainBinding
import kr.co.gamja.study_hub.feature.home.HomeViewModel
import kr.co.gamja.study_hub.feature.home.MainHomeFragmentDirections
import kr.co.gamja.study_hub.feature.toolbar.bookmark.BookmarkViewModel
import kr.co.gamja.study_hub.feature.toolbar.bookmark.OnItemClickListener

class StudyMainFragment : Fragment() {
    private lateinit var binding: FragmentStudyMainBinding
    private val viewModel: HomeViewModel by activityViewModels()
    private val bookmarkViewModel: BookmarkViewModel by activityViewModels()
    private var page = 0 // 스터디 조회 시작 페이지
    private var isLastPage = false // 스터디 조회 마지막 페이지
    private var isFirstPage = false // 스터디 조회 첫번째 페이지
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


        /*// 스터디 전체 조회 api통신
        viewModel.getStudyPosts(
            adapter,
            false,
            0,
            3,
            null,
            true,
            object : OnScrollCallBackListener {
                override fun isFirst(result: Boolean) {
                    isFirstPage = result
                }

                override fun isLast(result: Boolean) {
                    isLastPage = result
                }

            })*/
        /*// 스터디 조회- 리사이클러뷰 페이지네이션
        binding.recyclerStudyMain.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                if (!isLastPage && visibleItemPosition == 2) { // 페이지 내릴 때
                    page++ // 페이지 +1
                    Log.e(tag, "라스트페이지로" + visibleItemPosition.toString())
                    Toast.makeText(requireContext(), "담페이지로", Toast.LENGTH_SHORT).show()
                    // todo("널 넣을시?" )
                    viewModel.getStudyPosts(
                        adapter,
                        false,
                        page,
                        3,
                        null,
                        true,
                        object : OnScrollCallBackListener {
                            override fun isFirst(result: Boolean) {
                            }

                            override fun isLast(result: Boolean) {
                            }
                        })
                } else if (!isFirstPage && visibleItemPosition == 1) { // 페이지 올릴 때
                    page--
                    Log.e(tag, "앞페이지로" + visibleItemPosition.toString())
                    Toast.makeText(requireContext(), "앞페이지로", Toast.LENGTH_SHORT).show()
                    viewModel.getStudyPosts(
                        adapter,
                        false,
                        page,
                        3,
                        null,
                        true,
                        object : OnScrollCallBackListener {
                            override fun isFirst(result: Boolean) {
                            }

                            override fun isLast(result: Boolean) {
                            }
                        })
                } else {
                    Toast.makeText(requireContext(), "앞도 뒤도 아님", Toast.LENGTH_SHORT).show()
                }
            }
        })*/
        // 북마크 삭제 저장 api연결- 북마크 뷰모델 공유
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(tagId: String?, postId: Int?) {
                bookmarkViewModel.saveDeleteBookmarkItem(postId)
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


}