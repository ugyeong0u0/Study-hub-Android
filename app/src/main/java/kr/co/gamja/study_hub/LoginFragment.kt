package kr.co.gamja.study_hub

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.custom.FunctionLogin
import kr.co.gamja.study_hub.data.Auth
import kr.co.gamja.study_hub.data.LoginResponse

import kr.co.gamja.study_hub.databinding.FragmentLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val INU_EMAIL: String = "@inu.ac.kr"

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!


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
        val functionLogin: FunctionLogin = FunctionLogin(requireContext())

        // editText- 이메일 and 패스워드처리
        val login_email = binding.editlayoutEmail
        val login_password = binding.editlayoutPassword
        val flag_btn: Boolean = functionLogin.loginTextWatcher(login_email, login_password)


        binding.btnRegistration.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_createAccount, null)
        }

    }
    // 처음 로그인 버튼 누를 시
    // TODO("자동로그인 구현 ")


    fun firstLogin() {
        val id = binding.editId.text.toString().trim()
        val password = binding.editPassword.text.toString().trim()

        val userData = Auth(id, password)
        val api = AuthApi.create()

        api.login(userData).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                TODO("data store로 토큰저장")

            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "통신실패", Toast.LENGTH_LONG).show()
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}