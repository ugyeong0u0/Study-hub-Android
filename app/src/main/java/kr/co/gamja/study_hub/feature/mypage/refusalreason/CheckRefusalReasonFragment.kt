package kr.co.gamja.study_hub.feature.mypage.refusalreason

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentCheckRefusalReasonBinding

class CheckRefusalReasonFragment : Fragment() {
    val msg = this.javaClass.simpleName
    private lateinit var binding: FragmentCheckRefusalReasonBinding
    private val viewModel: CheckRefusalReasonViewModel by viewModels()
    private var studyId :Int =0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_check_refusal_reason, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // 툴바 설정
        val toolbar = binding.checkRefusalFragmentToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        binding.iconBack.setOnClickListener {
            findNavController().navigateUp() // 뒤로 가기
        }

        val receiveBundle = arguments
        if (receiveBundle != null) {
            studyId = receiveBundle.getInt("studyId")
            Log.i(msg, "단일 거절 내역 볼 studyId : {$studyId}")
        } else Log.e(
            msg,
            "FindPassByAuthFragment's receiveBundle is Null in goToCorrectStudy()"
        )

        // 완료 누르기
        binding.btnDone.setOnClickListener{
            findNavController().navigateUp() // 뒤로 가기
        }

        viewModel.getRefusalReasonAboutStudy(studyId = studyId) // 거절 이유 보기

    }

}