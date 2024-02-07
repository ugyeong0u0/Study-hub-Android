package kr.co.gamja.study_hub.feature.mypage.participant

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentBottomSheetListDialogBinding

class BottomSheet(userId : Int) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetListDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetListDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.apply{

            //초기 거절 비활성화
            btnRefusal.isEnabled = false

            //거절 선택 가능
            var flag = false
            isChecked = flag

            //선택 사유
            var selectedReason = ""

            //radio group 내 button의 check 상태 변경 listener
            rgSelect.setOnCheckedChangeListener { group, checkedId ->
                selectedReason = if (checkedId == R.id.chb1) tvR1.text.toString()
                else if (checkedId == R.id.chb2) tvR2.text.toString()
                else if (checkedId == R.id.chb3) tvR3.text.toString()
                else tvR4.text.toString()

                flag = true
                btnRefusal.isEnabled = true
            }

            //거절 버튼
            btnRefusal.setOnClickListener{
                //tvR4 텍스트 메세지라면? 거절 사유 작성 화면으로 navigation
                if (selectedReason == tvR4.text.toString()) {
                    //RefusalFragment로 이동
                } else {
                    //거절 api 사용
                }
            }

            //bottom sheet dialog 닫기
            btnClose.setOnClickListener{
                dismiss()
            }
        }
    }
}