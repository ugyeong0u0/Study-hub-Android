package kr.co.gamja.study_hub.fragment_login


import android.hardware.camera2.CameraExtensionSession.StillCaptureLatency
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.RetrofitManager
import kr.co.gamja.study_hub.StudyHubApi
import kr.co.gamja.study_hub.custom.FunctionLogin
import kr.co.gamja.study_hub.data.ApiRequest
import kr.co.gamja.study_hub.data.ApiResponse
import kr.co.gamja.study_hub.data.EmailValidRequest
import kr.co.gamja.study_hub.data.EmailValidResponse
import kr.co.gamja.study_hub.databinding.FragmentCreateAccount01Binding
import kr.co.gamja.study_hub.model.UserViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create


class CreateAccountFragment01 : Fragment() {
    private var _binding: FragmentCreateAccount01Binding? = null
    private val binding get() = _binding!!
    private val sharedViewModel : UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateAccount01Binding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // editText
        val functionLogin: FunctionLogin = FunctionLogin(requireContext())

        binding.fcaTxtPagenumber.text = getString(R.string.txt_pagenumber, 1)
        binding.fcaBtnNext.setOnClickListener {
            findNavController().navigate(R.id.action_createAccountFragment01_to_createAccountFragment02)
        }

        // 이메일 확인
        val fcaLayout_email = binding.fcaEditlayoutEmail
        val caf01_flag_email: Boolean = functionLogin.loginTextWatcher(fcaLayout_email, null, 1)

        // 인증코드확인
        val fcaLayout_authcode = binding.fcaEditlayoutAuthcode
        val caf01_flag_authcode: Boolean = functionLogin.authTextWatcher(fcaLayout_authcode)
        val fca_email = fcaLayout_email.editText.toString()
        val fca_authcode = fcaLayout_email.editText.toString()

        // ...1
        Log.d("이메일불", "$caf01_flag_email")
        if (caf01_flag_email == true) {
            binding.btnAuth.isVisible = true
            binding.btnAuth.setOnClickListener {

                CoroutineScope(Dispatchers.IO).launch {
                    clickSendingAuth(fca_email) // 이메일 인증 코드 전송
                }
                binding.btnAuth.isVisible = false
                binding.btnResend.isVisible = true
                binding.fcaTxtWordauthcode.isVisible = true
                binding.fcaEditauthcode.isVisible = true
                binding.btnResend.setOnClickListener {
                    clickSendingAuth(fca_email)
                }
            }
            // if(인증코드 양식 확인)
            if (caf01_flag_authcode == true) {
                CoroutineScope(Dispatchers.IO).launch {
                    val emailValidData = EmailValidRequest(fca_authcode, fca_email)

                    RetrofitManager.api.emailValid(emailValidData)
                        .enqueue(object : Callback<EmailValidResponse> {
                            override fun onResponse(
                                call: Call<EmailValidResponse>,
                                response: Response<EmailValidResponse>
                            ) {
                                Log.d("RetrofitManager", "success : ${response.body() as EmailValidResponse}")
                            }

                            override fun onFailure(call: Call<EmailValidResponse>, t: Throwable) {
                                Log.d("RetrofitManager", "fail : ${t}")
                            }
                        })
                }
                binding.fcaBtnNext.isEnabled = true
            }
        }
    }

    // 인증 버튼 눌렀을 때 이메일 인증 코드 전송
    fun clickSendingAuth(fca_email: String) {
        val emailData = ApiRequest(fca_email)
            RetrofitManager.api.email(emailData).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                Log.d("RetrofitManager", "success : ${response.body()}")
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d("RetrofitManager", "fail : ${t}")
            }

        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
interface ResultAuthCode{
    fun onSuccess(bool:Boolean)
}