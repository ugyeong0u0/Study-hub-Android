package kr.co.gamja.study_hub.feature.mypage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private var _binding: FragmentMyInfoBinding? = null
    private val binding get() = _binding!!
    private val tag = this.javaClass.simpleName
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyInfoBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 로그아웃 누를 시 Dialog
        isLogin()
        binding.btnLogout.setOnClickListener {
            var head = requireContext().resources.getString(R.string.q_logout)
            var no = requireContext().resources.getString(R.string.btn_no)
            var yes = requireContext().resources.getString(R.string.btn_yes)
            val dialog = CustomDialog(requireContext(), head, null, no, yes)
            dialog.showDialog()
            dialog.setOnClickListener(object : OnDialogClickListener {
                override fun onclickResult() { // 로그아웃 "네" 누르면
                    findNavController().navigate(
                        R.id.action_myInfoFragment_to_loginFragment2,
                        null
                    )
                }
            })
        }
    }

    fun isLogin(){
        CoroutineScope(Dispatchers.Main).launch {
            val accessToken = App.getInstance().getDataStore().accessToken.first()
            Log.d(tag, "데이터스토어에서 불러온 액세스토큰 "+accessToken)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}