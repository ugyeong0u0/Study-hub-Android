package kr.co.gamja.study_hub.feature.login


import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.datastore.App
import kr.co.gamja.study_hub.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setUpLoginEmail()
        setUpLoginPassword()

        binding.viewPassword.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // 비밀번호가 안보이고 있는데 보이게
                if (binding.viewPassword.getTag().toString() == "0") {
                    binding.viewPassword.setTag("1")
                    binding.editPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    binding.viewPassword.setImageResource(R.drawable.img_toggle_eye)
                } else { // 비밀번호가 보일때 안보이게
                    binding.viewPassword.setTag("0")
                    binding.editPassword.inputType =
                        InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
                    binding.viewPassword.setImageResource(R.drawable.img_toggle_closedeye)
                }
                binding.editPassword.setSelection(binding.editPassword.text.toString().length)
            }
        })

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
        // 이메일 에딧텍스트
        viewModel.validEmail.observe(viewLifecycleOwner, Observer { it ->
            if (!it) {
                val errorColor = ContextCompat.getColor(requireContext(), R.color.R_50)
                val stateList = ColorStateList.valueOf(errorColor)
                binding.errorEmail.apply {
                    text = getString(R.string.txterror_email)
                    setTextColor(errorColor)
                }
                binding.editEmail.backgroundTintList = stateList
            } else {
                binding.errorEmail.isVisible = false
                val Color = ContextCompat.getColor(requireContext(), R.color.G_80)
                val stateList = ColorStateList.valueOf(Color)
                binding.editEmail.backgroundTintList = stateList
            }
        })
    }

    fun setUpLoginPassword() {
        binding.editPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.updateLoginPassword(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.updateLoginPassword(s.toString())
            }
        })

        // 패스워드 에딧텍스트
        viewModel.validPassword.observe(viewLifecycleOwner, Observer {
            if (!it) {
                val errorColor = ContextCompat.getColor(requireContext(), R.color.R_50)
                val stateList = ColorStateList.valueOf(errorColor)
                binding.errorPassword.apply {
                    text = getString(R.string.txterror_password)
                    setTextColor(errorColor)
                }
                binding.editPassword.backgroundTintList = stateList
            } else {
                binding.errorPassword.isVisible = false
                val Color = ContextCompat.getColor(requireContext(), R.color.G_80)
                val stateList = ColorStateList.valueOf(Color)
                binding.editPassword.backgroundTintList = stateList
            }
        })
    }
}
