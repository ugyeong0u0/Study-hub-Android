package kr.co.gamja.study_hub.feature.mypage.changeMajor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentChangeMajorBinding

class ChangeMajorFragment : Fragment() {
    private lateinit var binding: FragmentChangeMajorBinding
    private val viewModel: ChangeMajorViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_change_major, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        // 툴바 설정
        val toolbar = binding.changeMajorToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        binding.iconBack.setOnClickListener {
            val navcontroller = findNavController()
            navcontroller.navigateUp() // 뒤로 가기
        }
        // 돋보기랑 x버튼 enable여부
        binding.autoMajor.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()) {
                binding.btnSearch.isVisible = false
                binding.btnDelete.isVisible = true
            } else {
                binding.btnSearch.isVisible = true
                binding.btnDelete.isVisible = false
            }
        }
        selectMajor()

        binding.btnDelete.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                binding.autoMajor.text.clear()
            }
        })
        binding.btnComplete.setOnClickListener{
            viewModel.changeMajor() // 학과 수정 api연결
            val navcontroller = findNavController()
            navcontroller.navigateUp() // 뒤로 가기
            // TODO(" 기존학과랑 같을 경우 스낵바 -> api 수정되면하기", api성공여부 받고 뒤로가기 처리)
        }
    }

    private fun selectMajor() {
        val editTxt_major = binding.autoMajor
        val array_major: Array<String> = resources.getStringArray(R.array.array_majors)
        val adapter_array =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, array_major)
        editTxt_major.setAdapter(adapter_array)
        // 드랍다운 배경셋팅
        binding.autoMajor.let {
            it.setDropDownBackgroundResource(R.drawable.background_bg_20_round_10)
            it.dropDownVerticalOffset = 16 // TODO("위치 물어봐야 함")
        }
        binding.autoMajor.setOnItemClickListener { parent, _, position, _ ->
            binding.autoMajor.isEnabled = true
            val selectedItem = parent.adapter.getItem(position) as String
            viewModel.setUserMajor(selectedItem)

        }
    }
}