package kr.co.gamja.study_hub.feature.studypage.createStudy

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.repository.CallBackIntegerListener
import kr.co.gamja.study_hub.databinding.FragmentCreateStudyBinding
import kr.co.gamja.study_hub.feature.home.MainHomeFragmentDirections
import kr.co.gamja.study_hub.global.CustomDialog
import kr.co.gamja.study_hub.global.OnDialogClickListener


class CreateStudyFragment : Fragment() {
    private val tagMessage = this.javaClass.simpleName
    private lateinit var binding: FragmentCreateStudyBinding
    private val viewModel: CreateStudyViewModel by activityViewModels()
    var newStartDate = StartDate(null, null) // 시작 날짜 < 끝 날짜
    var isCorrectStudyRequest: Boolean = false // true - 스터디 수정페이지에서 넘어옴, false - 스터디 생성
    var currentPostId :Int =-1
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
        //*** 스터디 생성이나 수정이 완료될 때 viewModel 값 초기화 함
        goToCorrectStudy() // 수정이라면 기존에 게시했던 글 가져옴
        // 툴바 설정
        val toolbar = binding.createStudyToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""
        // 뒤로가기 아이콘 누를 시 알림 메시지
        binding.iconBack.setOnClickListener {
            if (viewModel.goBack()) { // 하나라도 입력 된 경우 뒤로가기 시 알림띄움
                val head = requireContext().resources.getString(R.string.q_cancelCreatingStudy)
                val sub = requireContext().resources.getString(R.string.q_sub_cancelCreatingStudy)
                val no = requireContext().resources.getString(R.string.btn_no)
                val yes = requireContext().resources.getString(R.string.btn_yes)
                val dialog = CustomDialog(requireContext(), head, sub, no, yes)
                dialog.showDialog()
                dialog.setOnClickListener(object : OnDialogClickListener {
                    override fun onclickResult() {
                        viewModel.setInit() // 초기화
                        val navcontroller = findNavController()
                        navcontroller.navigateUp() // 뒤로 가기
                    }
                })
            } else { // 입력된게 없는 경우
                viewModel.setInit() // 초기화
                val navcontroller = findNavController()
                navcontroller.navigateUp() // 뒤로 가기
            }
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
            Log.d(
                tagMessage,
                "chip 보이는지 isRelativeMajor" + viewModel.isRelativeMajor.value.toString()
            )
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

        // 시작 날짜 선택하기
        binding.btnStartDay.setOnClickListener {
            if (!newStartDate.selectedYearMonth.isNullOrEmpty() && !newStartDate.selectedDay.isNullOrEmpty()) {
                viewModel.initDay()
            }
            getModalsheet("0")
        }
        // 종료 날짜 선택하기
        binding.btnEndDay.setOnClickListener {
            getModalsheet("1")
        }
        binding.radioGroup.setOnCheckedChangeListener { _, p1 ->
            if (p1 == R.id.radio_yes) {
                viewModel.setSelectedFee(true)
                Log.d(tagMessage, viewModel.selectedFee.value.toString())
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
                if (!viewModel.persons.value.isNullOrEmpty())
                    if (viewModel.persons.value.toString().toInt() == 0) {
                        viewModel.setErrorPersons(true)
                    } else
                        viewModel.setErrorPersons(false)
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
                if (!it.isNullOrEmpty()) {
                    val startYearMonth = viewModel.convertYYYYMM(it)
                    val startday = viewModel.convertDay(it)
                    newStartDate = StartDate(startYearMonth, startday)
                    binding.btnEndDay.isEnabled = true // 시작날짜 선택o면 끝날짜 선택가능
                } else {
                    newStartDate = StartDate(null, null)
                    binding.btnEndDay.isEnabled = false // 시작날짜 선택x면 끝날짜 선택불가
                }
            }
            editEndDay.observe(viewLifecycleOwner) {
                viewModel.setButtonEnable()
            }
        }
        binding.btnComplete.setOnClickListener {
            if (isCorrectStudyRequest) { // 스터디 수정 api 호출
                viewModel.correctStudy(currentPostId,object: CallBackIntegerListener{
                    override fun isSuccess(result: Int) {
                        val action =
                            MainHomeFragmentDirections.actionGlobalStudyContentFragment(result)
                        findNavController().navigate(action)
                    }
                })
            } else { // 스터디 생성 api 호출
                viewModel.createStudy(object : CallBackIntegerListener {
                    override fun isSuccess(result: Int) {
                        val action =
                            MainHomeFragmentDirections.actionGlobalStudyContentFragment(result)
                        findNavController().navigate(action)
                    }
                })
            }
        }
    }

    fun getModalsheet(whatDay: String) {
        val bundle = Bundle()
        bundle.putString("whatDayKey", whatDay)
        bundle.putString("startYearMonth", newStartDate.selectedYearMonth) // 시작 날짜 캘린터fr로
        bundle.putString("startDay", newStartDate.selectedDay)
        val modal = CalendarFragment()
        modal.arguments = bundle
        modal.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
        modal.show(parentFragmentManager, modal.tag)
    }

    fun goToCorrectStudy() {
        val receiveBundle = arguments
        if (receiveBundle != null) {
            isCorrectStudyRequest = receiveBundle.getBoolean("isCorrectStudy")
            currentPostId = receiveBundle.getInt("postId")
            Log.d(tag, " value: $isCorrectStudyRequest, postId: $currentPostId")
        } else Log.e(tag, "receiveBundle is Null")
        if (isCorrectStudyRequest) {
            viewModel.getMyCreatedStudy(currentPostId)
        }
    }

}