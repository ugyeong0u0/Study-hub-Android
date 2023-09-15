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
import android.widget.Toast
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.datastore.App
import kr.co.gamja.study_hub.databinding.FragmentLoginBinding
import kotlin.properties.Delegates


class LoginFragment : Fragment() {
    private val tag = this.javaClass.simpleName
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
// 자동로그인 : 리프레쉬 토큰 x 경우 아예 통신 보내면 안됨 500에러 남.
        autoLogin()
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
                            "로그인 토큰(accessToken // refreshToken) 저장",
                            accessToken + "//" + refreshToken
                        )
                        findNavController().navigate(R.id.action_login_to_nav_graph02_main, null)

                        CoroutineScope(Dispatchers.Main).launch {
                            val dataStoreInstance = App.getInstance().getDataStore()

                            dataStoreInstance.clearDataStore() // 초기화 후
                            dataStoreInstance.setAccessToken(accessToken)
                            dataStoreInstance.setRefreshToken(refreshToken)
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

    // TODO("리프레시 토큰 조회 api로 변경하기 ")
    fun autoLogin() {
        var refreshToken: String? = null

        CoroutineScope(Dispatchers.Main).launch() {
            launch {
                refreshToken = App.getInstance().getDataStore().refreshToken.first()
                Log.d(tag, "데이터스토어에서 불러온 리프레시토큰 " + refreshToken)
            }.join()
            launch {
                if (refreshToken != null) {
                    Log.d(tag, "데이터스토어에서 불러온 리프레시토큰 통신실행? " + refreshToken)
                    viewModel.autoLogin(refreshToken!!, object : LoginCallback {
                        override fun onSuccess(
                            isBoolean: Boolean,
                            accessToken: String,
                            refreshToken: String
                        ) {
                            if (isBoolean) {
                                Log.d(tag, "리프레시토큰 유효하고 자동로그인 성공")
                                findNavController().navigate(
                                    R.id.action_login_to_nav_graph02_main,
                                    null
                                )
                                // 리프레시 토큰이 유효하다면, 리프레시 액세스토큰 새롭게 저장
                                CoroutineScope(Dispatchers.Main).launch {
                                    val dataStoreInstance = App.getInstance().getDataStore()

                                    dataStoreInstance.clearDataStore() // 초기화 후
                                    dataStoreInstance.setAccessToken(accessToken)
                                    dataStoreInstance.setRefreshToken(refreshToken)
                                    Log.d(tag, "새리프레시 토큰 확인 $refreshToken")
                                }
                            }
                        }

                        override fun onfail(isBoolean: Boolean) {
                            if (isBoolean) {
                                Log.e(tag, "리프레시토큰 유효x")
                                Toast.makeText(requireContext(), "리프레쉬 토큰 만료", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                    })
                } else
                    Log.d(tag, "리프레시 토큰 null로 넘어감 ")
            }
        }
    }

}
