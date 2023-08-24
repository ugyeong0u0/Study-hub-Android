package kr.co.gamja.study_hub.fragment_login

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.RetrofitManager
import kr.co.gamja.study_hub.StudyHubApi
import kr.co.gamja.study_hub.custom.FunctionLogin
import kr.co.gamja.study_hub.data.LoginRequest
import kr.co.gamja.study_hub.data.LoginResponse

import kr.co.gamja.study_hub.databinding.FragmentLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

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
        val functionLogin = FunctionLogin(requireContext())

        // editText- 이메일 and 패스워드처리
        val login_email = binding.editlayoutEmail
        val login_password = binding.editlayoutPassword
        val flag_btn: Boolean = functionLogin.loginTextWatcher(login_email, login_password,0)

        var login_email_txt = login_email.editText.toString()
        var login_password_txt = login_password.editText.toString()
        if (flag_btn == true) {
            var loginData = LoginRequest(login_email_txt, login_password_txt)

            RetrofitManager.api.login(loginData).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    var result = response.body() as LoginResponse
//                    TODO("토큰 데이터 스토어에 저장")
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(requireContext(),"로그인서버에서 불가능",Toast.LENGTH_LONG).show()
                }
            })


            // main페이지로 연결
            binding.btnLogin.setOnClickListener {
                findNavController().navigate(R.id.action_login_to_nav_graph02_main)
            }
        }
        // 메인페이지로 이동
        binding.btnTour.setOnClickListener{
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