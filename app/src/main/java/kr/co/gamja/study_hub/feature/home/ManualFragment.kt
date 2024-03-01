package kr.co.gamja.study_hub.feature.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentManualBinding
import kr.co.gamja.study_hub.global.CustomDialog
import kr.co.gamja.study_hub.global.OnDialogClickListener


class ManualFragment : Fragment() {
    val tagMsg = this.javaClass.simpleName
    private lateinit var binding: FragmentManualBinding
    private val viewModel: HomeViewModel by activityViewModels()
    var isUser: Boolean = true // 로그인 유저(true)인지 아니면 둘러보기 유저인지(false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_manual, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 툴바 설정
        val toolbar = binding.maunalToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""


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

        binding.iconBack.setOnClickListener {
            val navcontroller = findNavController()
            navcontroller.navigateUp() // 뒤로 가기
        }
        binding.btnNext.setOnClickListener {
            if (viewModel.isUserLogin.value == true) { // todo 뷰모델 공유해도 되는지
                findNavController().navigate(R.id.action_global_createStudyFragment, null)
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
    }
}