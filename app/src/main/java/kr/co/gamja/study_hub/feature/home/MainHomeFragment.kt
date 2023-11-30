package kr.co.gamja.study_hub.feature.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.repository.OnViewClickListener
import kr.co.gamja.study_hub.databinding.FragmentMainHomeBinding
import kr.co.gamja.study_hub.feature.toolbar.bookmark.BookmarkViewModel
import kr.co.gamja.study_hub.feature.toolbar.bookmark.OnItemClickListener
import kr.co.gamja.study_hub.global.CustomSnackBar


class MainHomeFragment : Fragment() {
    private lateinit var binding: FragmentMainHomeBinding
    private val viewModel: HomeViewModel by activityViewModels()
    private val bookmarkViewModel: BookmarkViewModel by activityViewModels()
    private var doubleBackPressed = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_main_home, container, false)
        return binding.root
    }

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        // 툴바 설정
        val toolbar = binding.mainToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""
        binding.iconH.setOnClickListener {
            findNavController().navigate(
                R.id.action_mainFragment01_self,
                null
            )
        }
        binding.iconStudyHub.setOnClickListener {
            findNavController().navigate(
                R.id.action_mainFragment01_self,
                null
            )
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

        // 검색창으로 넘어감
        binding.btnSearch.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment01_to_mainFragment02, null)
        }
        // 설명으로 넘어감
        binding.btnGoGuide.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment01_to_mainFragment03, null)
        }
        // 모집중 스터디 어댑터 연결
        val onRecruitingAdapter = ItemOnRecruitingAdapter(requireContext())
        binding.recyclerOnGoing.adapter = onRecruitingAdapter
        binding.recyclerOnGoing.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewModel.getStudyPosts(
            onRecruitingAdapter,
            false,
            0,
            5,
            null,
            titleaAndMajor = false,
            null
        )

        // 북마크 삭제 저장 api연결- 북마크 뷰모델 공유
        onRecruitingAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(tagId: String?, postId: Int?) {
                bookmarkViewModel.saveDeleteBookmarkItem(postId)
            }
        })
        // 뷰클릭시
        onRecruitingAdapter.setViewClickListener(object : OnViewClickListener {
            override fun onViewClick(postId: Int?) {
                val action = MainHomeFragmentDirections.actionGlobalStudyContentFragment(postId!!)
                findNavController().navigate(action)
            }
        })

        // 마감임박 스터디 어댑터 연결
        val deadlineAdapter = ItemCloseDeadlineAdapter(requireContext())
        binding.recyclerApproaching.adapter = deadlineAdapter
        binding.recyclerApproaching.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getStudyPosts(
            deadlineAdapter,
            isHot = true,
            0,
            4,
            null,
            titleaAndMajor = false,
            null
        )

        deadlineAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(tagId: String?, postId: Int?) {
                bookmarkViewModel.saveDeleteBookmarkItem(postId)
            }
        })
        // 뷰자체 클릭시 스터디 컨텐츠 글로 이동
        deadlineAdapter.setViewClickListener(object : OnViewClickListener {
            override fun onViewClick(postId: Int?) {
                val action = MainHomeFragmentDirections.actionGlobalStudyContentFragment(postId!!)
                findNavController().navigate(action)
            }
        })
    }


}
