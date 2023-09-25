package kr.co.gamja.study_hub.feature.studypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentCreateStudyBinding


class CreateStudyFragment : Fragment() {
    private lateinit var binding: FragmentCreateStudyBinding

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
        // 툴바 설정
        val toolbar = binding.createStudyToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        binding.iconBack.setOnClickListener {
            val navcontroller = findNavController()
            navcontroller.navigateUp() // 뒤로 가기
        }
        binding.btnSelectMajor.setOnClickListener {
            findNavController().navigate(
                R.id.action_createStudyFragment_to_relativeMajorFragment,
                null
            )
        }
        binding.editChatLink.doOnTextChanged { text, _, _, _ ->
            binding.btnDelete.isVisible = !text.isNullOrEmpty()
        }
        // 링크 전체 지우기
        binding.btnDelete.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                binding.editChatLink.text.clear()
            }
        })
    }

}