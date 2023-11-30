package kr.co.gamja.study_hub.feature.studypage.studyContent.correctStudy

import android.icu.util.BuddhistCalendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentBottomSheetBinding
import kr.co.gamja.study_hub.global.CustomDialog
import kr.co.gamja.study_hub.global.OnDialogClickListener


class BottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomSheetBinding
    private val viewModel: BottomSheetViewModel by viewModels()
    private var nowPostId: Int = -1 // 현재 보고 있는 포스팅 id
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_sheet, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val receiveBundle = arguments
        if (receiveBundle != null) {
            nowPostId = receiveBundle.getInt("postId")
        }
        // 스터디 글 삭제 누를 시 (todo("다이어로그 사라지는 순서 확인필요"))
        binding.btnDelete.setOnClickListener {
            val head = requireContext().resources.getString(R.string.q_deleteStudy)
            val sub = requireContext().resources.getString(R.string.q_deleteStudySub)
            val no = requireContext().resources.getString(R.string.btn_no)
            val yes = requireContext().resources.getString(R.string.btn_delete)
            val dialog = CustomDialog(requireContext(), head, sub, no, yes)
            dialog.showDialog()
            dialog.setOnClickListener(object : OnDialogClickListener {
                override fun onclickResult() {
                    viewModel.deleteStudy(postId = nowPostId)
                    findNavController().navigateUp() // 뒤로 가기
                    dismiss()
                }
            })
        }
        // todo("nav컴포넌트로 보내는거 왜 안되는지")
        binding.btnModify.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean("isCorrectStudy",true)
            bundle.putInt("postId",nowPostId)
            findNavController().navigate(R.id.action_global_createStudyFragment, bundle)
        }

        // 닫기 누를 시
        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }


}