package kr.co.gamja.study_hub.feature.login


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentLoginBinding
import kr.co.gamja.study_hub.data.datastore.App


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 로그인하기
        binding.btnLogin.setOnClickListener {

            val emailTxt = binding.editId.text.toString()
            val passwordTxt = binding.editPassword.text.toString()

            viewModel.goLogin(emailTxt, passwordTxt, object : LoginCallback {
                override fun onSuccess(
                    isBoolean: Boolean,
                    accessToken: String,
                    refreshToken: String
                ) {
                    if (isBoolean) {
                        Log.d(
                            "로그인 토큰(accessToken // refreshToken) 저장",
                            accessToken + "//" + refreshToken
                        )
                        findNavController().navigate(R.id.action_login_to_nav_graph02_main)

                        CoroutineScope(Dispatchers.Main).launch {
                            App.getInstance().getDataStore().setAccessToken(accessToken)
                        }
                        CoroutineScope(Dispatchers.Main).launch {
                            App.getInstance().getDataStore().setRefreshToken(refreshToken)
                        }
                    }
                }
            })
        }

        // 둘러보기 버튼 누름
        binding.btnTour.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_nav_graph02_main)
        }
        // 회원가입페이지로 연결
        binding.btnRegistration.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_createAccount, null)
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}