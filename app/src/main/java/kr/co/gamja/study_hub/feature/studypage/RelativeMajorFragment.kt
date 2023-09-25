package kr.co.gamja.study_hub.feature.studypage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentRelativeMajorBinding

class RelativeMajorFragment : Fragment() {
    private val tag= this.javaClass.simpleName
    private lateinit var binding: FragmentRelativeMajorBinding
    private val viewModel: RelativeMajorFragmentViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_relative_major, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 툴바 설정
        val toolbar = binding.relativeMajorToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        binding.iconBack.setOnClickListener {
            val navcontroller = findNavController()
            navcontroller.navigateUp() // 뒤로 가기
        }
        binding.autoMajor.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()) {
                binding.txtSubRelativeMajor.isVisible = false
                binding.btnSearch.isVisible = false
                binding.btnDelete.isVisible = true
            } else {
                binding.txtSubRelativeMajor.isVisible = !binding.chipMajor.isVisible
                binding.btnSearch.isVisible = true
                binding.btnDelete.isVisible = false
            }
        }
        selectMajor()
    }

    fun selectMajor() {
        val editTxt_major = binding.autoMajor
        val array_major: Array<String> = resources.getStringArray(R.array.array_majors)
        val adapter_array =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, array_major)
        editTxt_major.setAdapter(adapter_array)

        binding.btnDelete.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                binding.autoMajor.text.clear()
            }
        })

        binding.autoMajor.setOnItemClickListener { parent, _, position, _ ->
            binding.autoMajor.isEnabled = true // 추가로 텍스트 변경 가능
            var selectedItem = parent.adapter.getItem(position) as String
            viewModel.setUserMajor(selectedItem)
            binding.chipMajor.apply {
                text=selectedItem
                isVisible = true
                setChipBackgroundColorResource(R.color.BG_30)
            }
        }

        binding.chipMajor.setOnCloseIconClickListener {
            it.isVisible = false
            binding.txtSubRelativeMajor.isVisible = true
            viewModel.setUserMajor("else로 빠짐")
            Log.d(tag, CreateStudyInfo.relativeMajor.toString())
            // TODO("뷰모델에 이 else인지확인값 받고 버튼 누를 수 있게")
        }
        // 드랍다운 배경셋팅
        binding.autoMajor.let {
            it.setDropDownBackgroundResource(R.drawable.background_bg_20_round_10)
            it.dropDownVerticalOffset = 16 // TODO("위치 물어봐야 함")
        }

    }
}