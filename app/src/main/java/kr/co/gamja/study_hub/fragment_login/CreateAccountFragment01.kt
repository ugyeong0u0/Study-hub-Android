package kr.co.gamja.study_hub.fragment_login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.RetrofitManager
import kr.co.gamja.study_hub.User
import kr.co.gamja.study_hub.databinding.FragmentCreateAccount01Binding
import kr.co.gamja.study_hub.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 회원가입- 이메일 인증
class CreateAccountFragment01 : Fragment() {
    private var _binding: FragmentCreateAccount01Binding? = null
    private val binding get() = _binding!!
    private val viewModel :RegisterViewModel by activityViewModels()
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

        binding.fcaTxtPagenumber.text = getString(R.string.txt_pagenumber, 1)


        // 이메일 인증 버튼 누름
        binding.btnAuth.setOnClickListener{
            val txt_email = binding.fcaEditId.text.toString()
            Log.d("회원가입 btnAuth눌렀을때",txt_email)
            viewModel.emailSend(txt_email)
            Log.d("회원가입 viewmodel 후 ","")
            binding.btnAuth.isVisible = false
            binding.btnResend.isVisible = true
            binding.fcaTxtWordauthcode.isVisible = true
            binding.fcaEditauthcode.isVisible = true
        }
        // 인증번호 재전송
        binding.btnResend.setOnClickListener{
            val txt_email = binding.fcaEditId.text.toString()
            viewModel.emailSend(txt_email)
        }
        // 인증코드확인
        binding.fcaBtnNext.setOnClickListener{
            val txt_email = binding.fcaEditId.text.toString()
            val authNumber=binding.fcaEditauthcode.text.toString()
            viewModel.emailAuthcheck(authNumber,txt_email)
            if(viewModel.emailValidation.value==true){
                User.email=txt_email
                findNavController().navigate(R.id.action_createAccountFragment01_to_createAccountFragment02)
            }
            else{
                Toast.makeText(requireContext(),"인증코드 틀림",Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
