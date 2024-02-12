package kr.co.gamja.study_hub.feature.home

import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.repository.CallBackListener
import kr.co.gamja.study_hub.data.repository.OnBookmarkClickListener
import kr.co.gamja.study_hub.data.repository.OnViewClickListener
import kr.co.gamja.study_hub.databinding.FragmentMainHomeBinding
import kr.co.gamja.study_hub.global.CustomDialog
import kr.co.gamja.study_hub.global.CustomSnackBar
import kr.co.gamja.study_hub.global.OnDialogClickListener


class MainHomeFragment : Fragment() {
    val tagMsg = this.javaClass.simpleName
    private lateinit var binding: FragmentMainHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private var doubleBackPressed = false
    private lateinit var deadlineAdapter: ItemCloseDeadlineAdapter
    private lateinit var onRecruitingAdapter: ItemOnRecruitingAdapter
    var isUser: Boolean = true // 로그인 유저(true)인지 아니면 둘러보기 유저인지(false)
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

        // 프로그래스바 제어, 데이터 받아오면 true, 프로그래스바는 false
        viewModel.progressRecruiting.value = false
        viewModel.progressDeadLine.value = false

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // 둘러보기인지 누른 페이지 bundle로 받음
        val receiveBundle = arguments
        if (receiveBundle != null) {
//            Log.e(tagMsg, "값 받기 전 : 유저인지$isUser")
            isUser = receiveBundle.getBoolean("isUser")
//            Log.e(tagMsg, "값 받은 후 : 유저인지$isUser")
        } else Log.e(
            tagMsg,
            "둘러보기 아님 "
        )
        viewModel.isUserLogin.value = isUser


        // 툴바 설정
        val toolbar = binding.mainToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        // 모집중 스터디 글자 span
        var spannableString = SpannableString(binding.txtOnGoingStudy.text)
        val color = requireContext().getColor(R.color.O_50)
        spannableString.setSpan(
            ForegroundColorSpan(color),
            0,
            4,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        binding.txtOnGoingStudy.text = spannableString

        // 마감이 임박한 스터디 글자 span
        spannableString = SpannableString(binding.txtApproachingStudy.text)
        spannableString.setSpan(
            ForegroundColorSpan(color),
            0,
            2,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        binding.txtApproachingStudy.text = spannableString


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
            val bundle = Bundle()
            bundle.putBoolean("isUser", isUser)
            findNavController().navigate(
                R.id.action_global_mainBookmarkFragment,
                bundle
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
        onRecruitingAdapter = ItemOnRecruitingAdapter(requireContext())
        binding.recyclerOnGoing.adapter = onRecruitingAdapter
        binding.recyclerOnGoing.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        onRecruitingAdapter.isUserLogin = isUser // 북마크 로그인 유저만 되게 하기위함
        // 리스트 업데이트
        updateRecruitingList()

        // 북마크 삭제 저장 api연결- 북마크 뷰모델 공유
        onRecruitingAdapter.setOnItemClickListener(object : OnBookmarkClickListener {
            override fun onItemClick(tagId: String?, postId: Int?) {
                if (isUser) {
                    viewModel.saveDeleteBookmarkItem(postId)
                    updateDeadlineList()
                } else {
                    Log.d(tagMsg, "비회원이 북마크 누름")
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
        })
        // 뷰클릭시
        onRecruitingAdapter.setViewClickListener(object : OnViewClickListener {
            override fun onViewClick(postId: Int?) {
                val action = MainHomeFragmentDirections.actionGlobalStudyContentFragment(postId!!)
                findNavController().navigate(action)
            }
        })

        // 마감임박 스터디 어댑터 연결
        deadlineAdapter = ItemCloseDeadlineAdapter(requireContext())
        binding.recyclerApproaching.adapter = deadlineAdapter
        binding.recyclerApproaching.layoutManager = LinearLayoutManager(requireContext())
        deadlineAdapter.isUserLogin = isUser // 로그인 여부 -> 북마크 색 변경 때문

        // 리스트 업데이트
        updateDeadlineList()

        deadlineAdapter.setOnItemClickListener(object : OnBookmarkClickListener {
            override fun onItemClick(tagId: String?, postId: Int?) {
                if (isUser) {
                    viewModel.saveDeleteBookmarkItem(postId)
                    updateRecruitingList()

                } else {
                    Log.d(tagMsg, "비회원이 북마크 누름")
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
        })
        // 뷰자체 클릭시 스터디 컨텐츠 글로 이동
        deadlineAdapter.setViewClickListener(object : OnViewClickListener {
            override fun onViewClick(postId: Int?) {
                val action = MainHomeFragmentDirections.actionGlobalStudyContentFragment(postId!!)
                findNavController().navigate(action)
            }
        })

        // 모집중 옆에있는 '전체'버튼 누를시 study탭으로 이동
        binding.btnAll.setOnClickListener {
            val bottomNavView: BottomNavigationView =
                requireActivity().findViewById(R.id.bottom_nav)
            bottomNavView.selectedItemId = R.id.studyMainFragment
        }

        // 프로그래스바 상태 관찰 및 업데이트
        viewModel.progressRecruiting.observe(viewLifecycleOwner) {
            if (it && viewModel.progressDeadLine.value == true) {
                viewModel.updateProgressBar(false)
            } else {
                viewModel.updateProgressBar(true)
            }
        }
        viewModel.progressDeadLine.observe(viewLifecycleOwner) {
            if (it && viewModel.progressRecruiting.value == true) {
                viewModel.updateProgressBar(false) // 프로그래스 바 안보이게
            } else {
                viewModel.updateProgressBar(true)
            }
        }
        viewModel.visibleProgress.observe(viewLifecycleOwner) {
            if (!it) {
                binding.mainProgressBar.visibility = View.GONE
            } else {
                binding.mainProgressBar.visibility = View.VISIBLE
            }
        }

    }

    // 뒤로가기 누를 시 혹은 뷰 생성시 리스트 데이터 업데이트
    private fun updateRecruitingList() {
        Log.d("MainHomeFragment.kt : pdateRecruitingList시작", "")
        viewModel.getRecruitingStudy(
            onRecruitingAdapter,
            false,
            0,
            5,
            null,
            titleAndMajor = false,
            object : CallBackListener {
                override fun isSuccess(result: Boolean) {
                    Log.d("MainHomeFragment.kt : updateRecruitingList", "")
                    viewModel.progressRecruiting.value = true // 로딩이 다됨을 표시
                }
            }
        )
    }

    private fun updateDeadlineList() {
        viewModel.getStudyPosts(
            deadlineAdapter,
            isHot = true,
            0,
            4,
            null,
            titleaAndMajor = false,
            object : CallBackListener {
                override fun isSuccess(result: Boolean) {
                    Log.d("MainHomeFragment.kt : updateDeadlineList callback", "")
                    viewModel.progressDeadLine.value = true // 로딩이 다 됨을 표시
                }
            }
        )
    }
}
