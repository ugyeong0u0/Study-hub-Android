package kr.co.gamja.study_hub

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.fragment.findNavController
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
        // edit text 이메일 error
        textWatcher()
        binding.btnRegistration.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_createAccount, null)
        }

    }
    // 처음 로그인 버튼 누를 시
    // TODO("자동로그인 구현 ")

    fun textWatcher() {
        // edit email 부분
        binding.editlayoutEmail.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                var id = binding.editId.text.toString()
                if (!(id.contains(INU_EMAIL)) or (id.contains(" "))) {
                    binding.editlayoutEmail.error = getString(R.string.txterror_email)
                } else {
                    binding.editlayoutEmail.error = null
                }
            }
        })

        // edit password부분
        binding.editlayoutPassword.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                var password = binding.editPassword.text.toString()
                if (!(password.length < 10) or !(password.matches("/^(?=.*[a-z])(?=.*[0-9])[0-9A-Za-z$&+,:;=?@#|'<>.^*()%!-]{8,16}$/".toRegex()))) { // todo("확인필요")
                    binding.editlayoutPassword.error = getString(R.string.txterror_password)
                } else {
                    binding.editlayoutPassword.error = null
                }
            }
        })
    }

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