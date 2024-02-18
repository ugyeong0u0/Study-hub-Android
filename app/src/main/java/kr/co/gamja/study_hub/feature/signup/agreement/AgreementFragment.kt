package kr.co.gamja.study_hub.feature.signup.agreement

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentAgreementBinding

class AgreementFragment : Fragment() {
    private lateinit var binding: FragmentAgreementBinding
    private val viewModel: AgreementViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_agreement, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_global_loginFragment, null)
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.txtPageNumber.text = getString(R.string.txt_pagenumber, 1)

        // 툴바 설정
        val toolbar = binding.agreementToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        binding.iconBack.setOnClickListener {
            val navcontroller = findNavController()
            navcontroller.navigateUp() // 뒤로 가기
        }
        // 이메일 페이지로
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_agreementFragment_to_emailFragment, null)
        }

        // todo("약관 상세보기 가기")

        // 전체 동의 눌렸을 때
        binding.layoutAllAgree.setOnClickListener {
            if (it.tag == "0") {
                viewModel.updateAllConsent(true)
                it.tag = "1"
                binding.layoutService.tag = "1"
                binding.layoutPersonalInfo.tag = "1"
            } else {
                viewModel.updateAllConsent(false)
                it.tag = "0"
                binding.layoutService.tag = "0"
                binding.layoutPersonalInfo.tag = "0"
            }
        }
        // 서비스 동의 눌렸을 때
        binding.layoutService.setOnClickListener {
            if (it.tag == "0") {
                viewModel.updateServiceContent(true)
                it.tag = "1"
            } else {
                viewModel.updateServiceContent(false)
                it.tag = "0"
            }
        }
        // 개인정보 동의 눌렸을 때
        binding.layoutPersonalInfo.setOnClickListener {
            if (it.tag == "0") {
                viewModel.updateInfoConsent(true)
                it.tag = "1"
            } else {
                viewModel.updateInfoConsent(false)
                it.tag = "0"
            }
        }


        // 개인정보 동의 체크 변화시 전체 동의 체크 변경
        viewModel.infoConsent.observe(viewLifecycleOwner) { state ->
            if (state && viewModel.serviceContent.value == true) {
                viewModel.updateOnlyAllConsent(true)
                binding.layoutAllAgree.tag = "1"
            } else {
                viewModel.updateOnlyAllConsent(false)
                binding.layoutAllAgree.tag = "0"
            }
        }
        // 서비스 동의 체크 변경시 전체 동의 체크 변경
        viewModel.serviceContent.observe(viewLifecycleOwner) { state ->
            if (state && viewModel.infoConsent.value == true) {
                viewModel.updateOnlyAllConsent(true)
                binding.layoutAllAgree.tag = "1"
            } else {
                viewModel.updateOnlyAllConsent(false)
                binding.layoutAllAgree.tag = "0"
            }
        }
        // 서비스 이용약관 클릭시
        binding.layoutService.setOnClickListener {
            findNavController().navigate(R.id.action_global_serviceUseTermFragment, null)
        }
        // 개인정보 이용약관
        binding.layoutPersonalInfo.setOnClickListener {
            findNavController().navigate(R.id.action_global_personalInfoTermFragment, null)
        }

    }


}