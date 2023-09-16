package kr.co.gamja.study_hub.feature.mypage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.datastore.App
import kr.co.gamja.study_hub.databinding.FragmentMyInfoBinding
import kr.co.gamja.study_hub.global.CustomDialog
import kr.co.gamja.study_hub.global.OnDialogClickListener

class MyInfoFragment : Fragment() {
    private lateinit var binding: FragmentMyInfoBinding
    private val tag = this.javaClass.simpleName
    private val viewModel: MyInfoViewModel by viewModels()
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
                    // 데이터스토어 초기화
                    CoroutineScope(Dispatchers.Main).launch {
                        App.getInstance().getDataStore().clearDataStore()
                    }
                    findNavController().navigate(
                        R.id.action_global_loginFragment,
                        null
                    )
                }
            })
        }
    }

}