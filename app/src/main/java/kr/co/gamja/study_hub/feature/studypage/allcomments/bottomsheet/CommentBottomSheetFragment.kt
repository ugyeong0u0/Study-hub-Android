package kr.co.gamja.study_hub.feature.studypage.allcomments.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.repository.CallBackListener
import kr.co.gamja.study_hub.databinding.FragmentCommentBottomSheetBinding
import kr.co.gamja.study_hub.global.CustomDialog
import kr.co.gamja.study_hub.global.OnDialogClickListener


class CommentBottomSheetFragment : BottomSheetDialogFragment() {
    private val msg = this.javaClass.simpleName
    private lateinit var binding: FragmentCommentBottomSheetBinding
    private val viewModel: CommentBottomSheetViewModel by viewModels()
    private var nowPostId: Int = -1 // 현재 보고 있는 포스팅 id
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_comment_bottom_sheet, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        val receiveBundle = arguments
        if (receiveBundle != null) {
            nowPostId = receiveBundle.getInt("postId")
            Log.d(msg, "postId : $nowPostId")
        }

        // 스터디 글 삭제 누를 시
        binding.btnDelete.setOnClickListener {
            val head = requireContext().resources.getString(R.string.commentBottomsheetHead)
            val no = requireContext().resources.getString(R.string.btn_no)
            val yes = requireContext().resources.getString(R.string.btn_delete)
            val dialog = CustomDialog(requireContext(), head, null, no, yes)
            dialog.showDialog()
            dialog.setOnClickListener(object : OnDialogClickListener {
                override fun onclickResult() {
                    viewModel.deleteComment(postId = nowPostId, object : CallBackListener{
                        override fun isSuccess(result: Boolean) { // todo("화면이동 확인 필요!!!, paging 업뎃확인도 필요")
                            findNavController().navigateUp() // 뒤로 가기
                            dismiss()
                        }
                    })
                }
            })
        }
        // 닫기 누를 시
        binding.btnClose.setOnClickListener {
            dismiss()
        }

        // todo("수정하기 ")

    }
}