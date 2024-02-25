package kr.co.gamja.study_hub.feature.studypage.studyContent

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.repository.CallBackListener
import kr.co.gamja.study_hub.data.repository.OnCommentClickListener
import kr.co.gamja.study_hub.data.repository.OnViewClickListener
import kr.co.gamja.study_hub.databinding.FragmentContentBinding
import kr.co.gamja.study_hub.feature.studypage.allcomments.bottomsheet.CommentBottomSheetFragment
import kr.co.gamja.study_hub.feature.studypage.studyContent.correctStudy.BottomSheetFragment
import kr.co.gamja.study_hub.global.CustomDialog
import kr.co.gamja.study_hub.global.CustomSnackBar
import kr.co.gamja.study_hub.global.OnDialogClickListener

// 스터디 상세 보기 관련
class ContentFragment : Fragment() {
    val msgTag = this.javaClass.simpleName
    private lateinit var binding: FragmentContentBinding
    private val args: ContentFragmentArgs by navArgs()
    private val viewModel: ContentViewModel by activityViewModels() // CommentBottomSheetFragment 모달에서 수정, 삭제인지 확인 필요해서 activity~로 함
    private var nowCommentId: Int = -1 // 현재 보고 있는 댓글 id -> 댓글 수정 삭제 모달에서 할 때 필요

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_content, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.isUserLogin.value = args.isUser
        val contentAdapter = ContentAdapter(requireContext())

        getContent(contentAdapter, args.postId)

//        Log.e(msgTag,args.postId.toString())
        // 툴바 설정
        val toolbar = binding.contentToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        binding.iconBack.setOnClickListener {
            findNavController().navigateUp() // 뒤로 가기
        }
        binding.iconThreeDot.setOnClickListener {
            getModal(args.postId)
        }
        // 스터디 생성한 사람 프로필 이미지
        viewModel.userImg.observe(viewLifecycleOwner) {
            Glide.with(this)
                .load(it)
                .apply(
                    RequestOptions().override(
                        binding.iconProfile.width,
                        binding.iconProfile.height
                    ).circleCrop()
                )
                .into(binding.iconProfile)
        }


        // 북마크 저장
        binding.bookmark.setOnClickListener {
            if (viewModel.isUserLogin.value!!) {
                if (binding.bookmark.tag == "1") {
                    binding.bookmark.tag = "0"
                    binding.bookmark.setBackgroundResource(R.drawable.baseline_bookmark_border_24_unselected)
                } else {
                    binding.bookmark.tag = "1"
                    binding.bookmark.setBackgroundResource(R.drawable.baseline_bookmark_24_selected)
                }
                viewModel.saveBookmark()
            } else {
                needLogin() // 비회원으로
            }
        }
        // 신청하기 페이지로 이동
        binding.btnNext.setOnClickListener {
            if (viewModel.isUserLogin.value!!) {
                val bundle = Bundle()
                bundle.putString("whatPage", "content") // 스터디 컨테츠 페이지에서 왔음을 알림, 신청후 백스택제거 때문
                bundle.putInt("studyId", viewModel.studyId.value?.toInt() ?: 0)
                bundle.putInt("postId", args.postId)
                findNavController().navigate(R.id.action_global_applicationFragment, bundle)
            } else {
                needLogin()
            }

        }

        binding.recyclerRecommend.adapter = contentAdapter
        binding.recyclerRecommend.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        contentAdapter.setViewClickListener(object : OnViewClickListener {
            override fun onViewClick(postId: Int?) {
//                todo
//                val bundle = Bundle()
//                bundle.putInt("postId", postId!!)

                val action = ContentFragmentDirections.actionGlobalStudyContentFragment(
                    viewModel.isUserLogin.value!!,
                    postId!!
                )
                findNavController().navigate(action)
            }
        })

        // 댓글 조회
        val commentAdapter = CommentAdapter(requireContext())
//        Log.e(msgTag, "getCommentsList위"+args.postId)
        viewModel.getCommentsList(adapter = commentAdapter, args.postId)

        binding.recyclerComment.adapter = commentAdapter
        binding.recyclerComment.layoutManager =
            LinearLayoutManager(requireContext())

        // 댓글 세개의 점 아이콘 누를 시 (수정, 삭제, 닫기 모달있는)
        commentAdapter.setOnItemClickListener(object : OnCommentClickListener {
            override fun getCommentValue(whatItem: Int, itemValue: Int, comment: String) {
//                Log.e(msgTag,"1. 댓글미리보기에서 댓글모달 눌림 ")
                when (whatItem) {
                    1 -> {
                        nowCommentId = itemValue // 댓 id 저장
                        goCommentModal(comment) // 수정 삭제 모달로 이동
                    }
                }
            }
        })

        // 댓글 전체 조회 페이지로
        binding.btnAllComment.setOnClickListener {
            if (viewModel.isUserLogin.value == true) {
                val bundle = Bundle()
                bundle.putInt("postId", args.postId) // 현재 포스팅 id 전달
                findNavController().navigate(
                    R.id.action_studyContentFragment_to_allCommentsFragment,
                    bundle
                )
            } else {
                needLogin()
            }

        }

        // 댓글 관찰 -> 버튼 활성화 여부 결정
        viewModel.studyComment.observe(viewLifecycleOwner) { comment ->
            val isButtonEnabled = comment.isNotEmpty()
            binding.btnCommentOk.isEnabled = isButtonEnabled
        }
        // 댓글 추가
        binding.btnCommentOk.setOnClickListener {
            if (viewModel.isUserLogin.value == true) {
//            Log.e(msgTag, "댓글 수정 눌림1 ${viewModel.isModify.value}")
                // 댓글 수정인 경우
                if (viewModel.isModify.value == true) {
                    Log.d(msgTag, "댓글 수정 눌림2")
                    viewModel.modifyComment(nowCommentId, object : CallBackListener {
                        override fun isSuccess(result: Boolean) {
                            if (result) {
                                Log.i(msgTag, "댓글 수정 성공 callback")
                                CustomSnackBar.make(
                                    binding.layoutRelative,
                                    getString(R.string.modifyComment), binding.btnNext, false
                                ).show()
                                // 키보드 내리기
                                hideKeyboardForResend()
                                // editText에 입력된 글 지우기
                                viewModel.studyComment.value = ""
                                // 수정 후 false
                                viewModel.setModify(false)
                                // 댓글 재로딩
                                Log.i(msgTag, "댓 미리보기 댓수정 콜백 안 " + args.postId)
                                viewModel.getCommentsList(adapter = commentAdapter, args.postId)
                            }
                        }
                    })
                } else { // 댓글 등록 경우
                    viewModel.setUserComment(object : CallBackListener {
                        override fun isSuccess(result: Boolean) {
                            if (result) {
                                CustomSnackBar.make(
                                    binding.layoutRelative,
                                    getString(R.string.setComment),
                                    binding.btnNext,
                                    true,
                                    R.drawable.icon_check_green
                                ).show()
                                // 키보드 내리기
                                hideKeyboardForResend()
                                // editText에 입력된 글 지우기
                                viewModel.studyComment.value = ""
                                // 어댑터 리스트 재로딩
//                            Log.e(msgTag,"댓미리보기 댓 추가 콜백 안 "+args.postId)
                                viewModel.getCommentsList(adapter = commentAdapter, args.postId)
                            }
                        }
                    })
                }
            } else {
                needLogin()
            }

        }

        // bottomsheet에서 삭제 눌러서 왔을 경우
        viewModel.isDelete.observe(viewLifecycleOwner) {
            if (it) {
//                Log.e(msgTag,"댓글 미리보기 댓 삭제 ")
                viewModel.deleteComment(postId = nowCommentId, object : CallBackListener {
                    override fun isSuccess(result: Boolean) {
                        if (result) {
                            viewModel.setDelete(false) // 삭제 후 false로 원상복귀
                            viewModel.getCommentsList(
                                adapter = commentAdapter,
                                args.postId
                            ) // 댓글 재로딩
                        }
                    }
                })
            }
        }
    }

    // 컨텐츠 내용 가져오기
    private fun getContent(adapter: ContentAdapter, postId: Int) {
        viewModel.getStudyContent(adapter, postId, object : CallBackListener {
            override fun isSuccess(result: Boolean) {
                if (result) {
                    if (viewModel.isBookmarked.value == true) {
                        binding.bookmark.setBackgroundResource(R.drawable.baseline_bookmark_24_selected)
                        binding.bookmark.tag = "1"
                    } else {
                        binding.bookmark.setBackgroundResource(R.drawable.baseline_bookmark_border_24_unselected)
                        binding.bookmark.tag = "0"
                    }
                }
            }
        })
    }

    // 컨텐츠 수정 모달
    private fun getModal(postId: Int) {
        val bundle = Bundle()
        bundle.putInt("postId", postId)
        val modal = BottomSheetFragment()
        modal.arguments = bundle
        modal.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
        modal.show(parentFragmentManager, modal.tag)
    }

    // 댓글 미리보기 삭제 수정 모달싯트로
    private fun goCommentModal(comment: String) {
        val modal = CommentBottomSheetFragment()
        val bundle = Bundle()
        bundle.putString("comment", comment)
        bundle.putString("page", "contentFragment")
        modal.arguments = bundle
        modal.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
        modal.show(parentFragmentManager, modal.tag)
    }

    private fun hideKeyboardForResend() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusView = requireActivity().currentFocus
        if (currentFocusView != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocusView.windowToken, 0)
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