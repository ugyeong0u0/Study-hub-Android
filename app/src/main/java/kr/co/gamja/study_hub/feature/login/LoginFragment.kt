package kr.co.gamja.study_hub.feature.login


import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
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
import kotlinx.coroutines.withContext
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.datastore.App
import kr.co.gamja.study_hub.databinding.FragmentLoginBinding
import kotlin.properties.Delegates


class LoginFragment : Fragment() {
    private val msgTag = this.javaClass.simpleName
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private var grayColor by Delegates.notNull<Int>() // G_80 : 에딧텍스트 값 정답
    private lateinit var grayStateList: ColorStateList
    private var redColor by Delegates.notNull<Int>() // R_50 : 에딧텟스트 값 오류
    private lateinit var redStateList: ColorStateList

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
    }

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
        grayColor = ContextCompat.getColor(requireContext(), R.color.G_80)
        redColor = ContextCompat.getColor(requireContext(), R.color.R_50)
        grayStateList = ColorStateList.valueOf(grayColor)
        redStateList = ColorStateList.valueOf(redColor)

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
                override fun onfail(isBoolean: Boolean) {
                    if (isBoolean) {
                        binding.errorEmail.apply {
                            text = getString(R.string.txterror_email)
                            setTextColor(redColor)
                            isVisible = true
                        }
                        binding.editEmail.backgroundTintList = redStateList

                        binding.errorPassword.apply {
                            text = getString(R.string.txterror_password)
                            setTextColor(redColor)
                            isVisible = true
                        }
                        binding.editPassword.backgroundTintList = redStateList

                    }
                }

                override fun onSuccess(
                    isBoolean: Boolean,
                    accessToken: String,
                    refreshToken: String
                ) {
                    if (isBoolean) {
                        Log.d(
                            "로그인 토큰(accessToken // refreshToken):",
                            accessToken + "//" + refreshToken
                        )
                        CoroutineScope(Dispatchers.IO).launch {
                            val dataStoreInstance = App.getInstance().getDataStore()

                            dataStoreInstance.clearDataStore() // 초기화 후
                            dataStoreInstance.setAccessToken(accessToken)
                            dataStoreInstance.setRefreshToken(refreshToken)
                            withContext(Dispatchers.Main) {
                                findNavController().navigate(
                                    R.id.action_login_to_nav_graph02_main,
                                    null
                                )
                            }
                        }

                    }
                }
            })
        }

        // 둘러보기 버튼 누름
        binding.btnTour.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_nav_graph02_main, null)
        }

        // 회원가입페이지로 연결
        binding.btnRegistration.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_agreementFragment, null)
        }
        // 로그인에서 비번 찾기 누를 시
        binding.btnForgetPassword.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("page", "login")

            // 네비게이션 그래프 변경
            findNavController().setGraph(R.navigation.nav_graph_from_home)
            // 로그인 프래그먼트로 이동
            findNavController().navigate(R.id.findPassByEmailFragment, bundle)
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
                binding.errorEmail.apply {
                    text = getString(R.string.txterror_email)
                    setTextColor(redColor)
                    isVisible = true
                }
                binding.editEmail.backgroundTintList = redStateList
            } else {
                binding.errorEmail.isVisible = false
                binding.editEmail.backgroundTintList = grayStateList
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
                binding.errorPassword.apply {
                    text = getString(R.string.txterror_password)
                    setTextColor(redColor)
                    isVisible = true
                }
                binding.editPassword.backgroundTintList = redStateList
            } else {
                binding.errorPassword.isVisible = false
                binding.editPassword.backgroundTintList = grayStateList
            }
        })
    }


}