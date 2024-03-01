package kr.co.gamja.study_hub.feature.mypage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.datastore.App
import kr.co.gamja.study_hub.data.repository.SecondCallBackListener
import kr.co.gamja.study_hub.databinding.FragmentMyInfoBinding
import kr.co.gamja.study_hub.feature.mypage.uploadImg.UploadImageFragment
import kr.co.gamja.study_hub.global.CustomDialog
import kr.co.gamja.study_hub.global.CustomSnackBar
import kr.co.gamja.study_hub.global.OnDialogClickListener

class MyInfoFragment : Fragment(), SecondCallBackListener {
    private lateinit var binding: FragmentMyInfoBinding
    private val msgTag = this.javaClass.simpleName
    private val viewModel: MyInfoViewModel by activityViewModels()

    override fun isSuccess(result: Boolean) { // 사진 변경 완료시 snackBar띄움
        if (result) {
            CustomSnackBar.make(
                binding.layoutLinear,
                getString(R.string.txt_changeUserImg)
            ).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_my_info, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // 툴바 설정
        val toolbar = binding.myPageMainToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        viewModel.getUsers()
        viewModel.imgData.observe(viewLifecycleOwner, Observer { img ->
            Glide.with(this).load(viewModel.imgData.value)
                .apply(
                    RequestOptions().override(
                        binding.imgProfile.width,
                        binding.imgProfile.height
                    ).circleCrop()
                )
                .into(binding.imgProfile)
        })

        viewModel.mSetOnClickListener(object : MyInfoCallbackListener {
            override fun myInfoCallbackResult(isSuccess: Boolean) {
                if (!isSuccess)
                    deleteLoginData(true)
            }
        })

        binding.iconBack.setOnClickListener {
            val navcontroller = findNavController()
            navcontroller.navigateUp() // 뒤로 가기
        }
        // 로그아웃 누를 시 Dialog
        binding.btnLogout.setOnClickListener {
            val head = requireContext().resources.getString(R.string.q_logout)
            val no = requireContext().resources.getString(R.string.btn_no)
            val yes = requireContext().resources.getString(R.string.btn_yes)
            val dialog = CustomDialog(requireContext(), head, null, no, yes)
            dialog.showDialog()
            dialog.setOnClickListener(object : OnDialogClickListener {
                override fun onclickResult() { // 로그아웃 "네" 누르면
                    deleteLoginData(true) // 로그인 요청 페이지로 이동
                    viewModel.init() // 회원정보 초기화
                }
            })
        }
        // 닉네임 변경페이지로 이동
        binding.btnTxtNickname.setOnClickListener {
            findNavController().navigate(R.id.action_myInfoFragment_to_changeNicknameFragment, null)
        }
        // 학과 변경페이지로 이동
        binding.btnTxtMajor.setOnClickListener {
            findNavController().navigate(R.id.action_myInfoFragment_to_changeMajorFragment, null)
        }
        // 비번 변경 페이지로 이동
        binding.btnTxtPassword.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("userEmail", viewModel.emailData.value)
            findNavController().navigate(
                R.id.action_myInfoFragment_to_currentPasswordFragment,
                bundle
            )
        }
        // 회원 탈퇴 페이지로 이동
        binding.btnLeave.setOnClickListener {
            findNavController().navigate(
                R.id.action_myInfoFragment_to_withdrawalFragment,
                null
            )
        }
        // 사진 수정 프레그먼트로
        binding.btnModifyImg.setOnClickListener {
            getModal()
        }
        // 사진 삭제
        binding.btnDeleteImg.setOnClickListener {
            viewModel.deleteImg()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUsers() // 데이터 변경 시
    }

    fun deleteLoginData(goLogin: Boolean) {
        // 데이터스토어 초기화
        CoroutineScope(Dispatchers.IO).launch {
            App.getInstance().getDataStore().clearDataStore()
            Log.d(tag, "초기화")
            if (goLogin) {
                withContext(Dispatchers.Main) {
                    // 로그인 프래그먼트로 이동
                    findNavController().navigate(R.id.action_global_loginFragment)
                }
            }

        }
    }

    private fun getModal() {
        val modal = UploadImageFragment()
        modal.snackBarListener = this
        modal.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
        modal.show(parentFragmentManager, modal.tag)
    }

}