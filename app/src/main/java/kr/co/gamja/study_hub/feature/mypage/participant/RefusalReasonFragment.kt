package kr.co.gamja.study_hub.feature.mypage.participant

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentRefusalReasonBinding

class RefusalReasonFragment : Fragment() {

    private lateinit var binding : FragmentRefusalReasonBinding

    private val viewModel : ParticipantViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_refusal_reason, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val studyId = arguments?.getInt("studyId") ?: -1
        val userId = arguments?.getInt("userId") ?: -1

        binding.apply{

            //textCount의 초기값
            textCount = "0"

            //toolbar 뒤로가기 버튼 클릭 시
            iconBack.setOnClickListener{
                findNavController().navigateUp()
            }

            //EdtiText 내용 변경 시 >> 숫자 변경 & EditText의 text가 0이 아니라면 button enable
            etRefusalReason.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    textCount = etRefusalReason.text.toString().length.toString()
                }

                override fun afterTextChanged(s: Editable?) {
                    btnDone.isEnabled = etRefusalReason.text.isNotEmpty()
                }
            })
            
            //거절 선택 시 대기 Fragment로 이동 && success dialog 띄우기
            btnDone.setOnClickListener {
                viewModel.reject(
                    rejectReason = etRefusalReason.text.toString(),
                    studyId = studyId,
                    userId = userId,
                )

                //dialog 띄우기
                val customToast = CustomToast()

                customToast.show(requireActivity().supportFragmentManager, "Toast")

                val bundle = Bundle()
                bundle.putInt("studyId", studyId)
                arguments = bundle

                // Navigation back stack에서 현재 프래그먼트를 제거
                findNavController().popBackStack(R.id.participantFragment, false)

                findNavController().navigate(
                    R.id.action_global_to_participantFragment,
                    arguments
                )
            }
        }
    }
}