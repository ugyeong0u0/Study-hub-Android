package kr.co.gamja.study_hub.feature.studypage.createStudy

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentCreateStudyBinding


class CreateStudyFragment : Fragment() {
    private lateinit var binding: FragmentCreateStudyBinding
    private val viewModel: CreateStudyViewModel by activityViewModels()
    private val tag = this.javaClass.simpleName
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_create_study,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // 툴바 설정
        val toolbar = binding.createStudyToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        binding.iconBack.setOnClickListener {
            val navcontroller = findNavController()
            navcontroller.navigateUp() // 뒤로 가기
        }

        // TODO("번들로 받을지?")
        binding.btnSelectMajor.setOnClickListener {
            findNavController().navigate(
                R.id.action_createStudyFragment_to_relativeMajorFragment,
                null
            )
        }

        // 링크 전체 삭제 이벤트 보이게 여부
        binding.editChatLink.doOnTextChanged { text, _, _, _ ->
            binding.btnDelete.isVisible = !text.isNullOrEmpty()
        }
        // 링크 전체 지우기 TODO(양방향으로 변경)
        binding.btnDelete.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                binding.editChatLink.text.clear()
            }
        })
        // chip 지울 시
        binding.chipMajor.setOnCloseIconClickListener {
            viewModel.setUserMajor("else로 빠짐") // 통신시 값 "null"로
            viewModel.setIsRelativeMajor(false) // chip 안보이게
            Log.d(tag, "chip 보이는지 isRelativeMajor" + viewModel.isRelativeMajor.value.toString())
        }
        viewModel.isRelativeMajor.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.chipMajor.setChipBackgroundColorResource(R.color.BG_30)
            }
        }
        // 성별선택
        binding.btnRegardlessOfGender.setOnClickListener {
            viewModel.setRegardlessOfGender(true)
        }
        binding.btnGenderMale.setOnClickListener {
            viewModel.setMale(true)
        }
        binding.btnGenderFemale.setOnClickListener {
            viewModel.setFemale(true)
        }
        // 스터디방식 : 혼합/대면/비대면
        binding.btnMix.setOnClickListener {
            viewModel.setMix(true)
        }
        binding.btnOffline.setOnClickListener {
            viewModel.setOffline(true)
        }
        binding.btnOnline.setOnClickListener {
            viewModel.setOnline(true)
        }
        // TODO("날짜 입력 및 선택/ 달력 커스텀")
        // 시작 날짜 선택하기
        val black = ContextCompat.getColor(requireContext(), R.color.sysblack1)
        binding.btnStartDay.setOnClickListener {
            getModalsheet("0")

        }
        // 종료 날짜 선택하기
        binding.btnEndDay.setOnClickListener {
            getModalsheet("1")
        }
        binding.radioGroup.setOnCheckedChangeListener { _, p1 ->
            if (p1 == R.id.radio_yes) {
                viewModel.setSelectedFee(true)
                Log.d(tag, viewModel.selectedFee.value.toString())
            } else viewModel.setSelectedFee(false)
        }

        with(viewModel) {
            // 스터디 생성 레트로핏 통신 가능 여부 확인
            urlEditText.observe(viewLifecycleOwner) {
                viewModel.setButtonEnable()
            }
            studyTitle.observe(viewLifecycleOwner) {
                viewModel.setButtonEnable()
            }
            studyContent.observe(viewLifecycleOwner) {
                viewModel.setButtonEnable()
            }
            relativeMajor.observe(viewLifecycleOwner) {
                viewModel.setButtonEnable()
            }
            persons.observe(viewLifecycleOwner) {
                viewModel.setButtonEnable()
                if(it.toInt()==0){
                    viewModel.setErrorPersons(true)
                }else  viewModel.setErrorPersons(false)
            }
            gender.observe(viewLifecycleOwner) {
                viewModel.setButtonEnable()
            }
            meetMethod.observe(viewLifecycleOwner) {
                viewModel.setButtonEnable()
            }
            howMuch.observe(viewLifecycleOwner) {
                viewModel.setButtonEnable()
            }
            // TODO("벌금종류")
            editStartDay.observe(viewLifecycleOwner) {
                viewModel.setButtonEnable()
            }
            editEndDay.observe(viewLifecycleOwner) {
                viewModel.setButtonEnable()
            }
        }
        binding.btnComplete.setOnClickListener {
            viewModel.createStudy()
            val navcontroller = findNavController()
            navcontroller.popBackStack() // TODO("스터디 글 상세 보기 페이지로 넘어가는걸로 수정 필요")
        }

    }

    fun getModalsheet(whatDay: String) {
        val bundle = Bundle()
        bundle.putString("whatDayKey", whatDay)
        val modal = CalendarFragment()
        modal.arguments = bundle
        modal.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
        modal.show(parentFragmentManager, modal.tag)
    }
}