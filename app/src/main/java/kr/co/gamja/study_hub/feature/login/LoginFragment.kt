package kr.co.gamja.study_hub.feature.login


import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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

        setUpLoginEmail()
        setUpLoginPassword()

        // 로그인하기
        binding.btnLogin.setOnClickListener {

            val emailTxt = binding.editEmail.text.toString()
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

    fun setUpLoginEmail() {
        binding.editEmail.doOnTextChanged { text, _, _, _ ->
            if (text.toString() != viewModel.loginEmail.value) {
                viewModel.updateLoginEmail(text.toString())
            }
        }
        viewModel.loginEmail.observe(viewLifecycleOwner, Observer { it ->
            if (it != binding.editEmail.text.toString())
                binding.editEmail.setText(it)
        })
        viewModel.validEmail.observe(viewLifecycleOwner, Observer { it ->
            if (it == true) {
                binding.editLayoutEmail.error = "정답"
                val errorColor = ContextCompat.getColor(requireContext(), R.color.GN_10)
                val stateList = ColorStateList.valueOf(errorColor)
                binding.editLayoutEmail.setErrorTextColor(stateList)

                binding.editLayoutEmail.boxStrokeErrorColor = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.G_10
                    )
                )
            } else {
                binding.editLayoutEmail.error = "오류"
                val errorColor = ContextCompat.getColor(requireContext(), R.color.R_50)
                val stateList = ColorStateList.valueOf(errorColor)
                binding.editLayoutEmail.setErrorTextColor(stateList)

                binding.editLayoutEmail.boxStrokeErrorColor = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.R_50
                    )
                )

            }
        })
    }

    fun setUpLoginPassword() {
        binding.editPassword.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.updateLoginPassword(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.updateLoginPassword(s.toString())
            }
        })


        viewModel.validPassword.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding.editLayoutPassword.error = "정답"
                val errorColor = ContextCompat.getColor(requireContext(), R.color.GN_10)
                val stateList = ColorStateList.valueOf(errorColor)
                binding.editLayoutPassword.setErrorTextColor(stateList)

                binding.editLayoutPassword.boxStrokeErrorColor = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.G_10
                    )
                )
            }
            else {
                binding.editLayoutPassword.error = "오류"
                val errorColor = ContextCompat.getColor(requireContext(), R.color.R_50)
                val stateList = ColorStateList.valueOf(errorColor)
                binding.editLayoutPassword.setErrorTextColor(stateList)

                binding.editLayoutPassword.boxStrokeErrorColor = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.R_50
                    )
                )
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}