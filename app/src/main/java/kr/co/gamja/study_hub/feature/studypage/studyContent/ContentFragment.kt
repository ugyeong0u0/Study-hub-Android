package kr.co.gamja.study_hub.feature.studypage.studyContent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentContentBinding


class ContentFragment : Fragment() {
    private lateinit var binding:FragmentContentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_content,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 툴바 설정
        val toolbar = binding.contentToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""
        binding.iconBack.setOnClickListener {
            val navcontroller = findNavController()
            navcontroller.navigateUp() // 뒤로 가기
        }
    // TODO("작성자인지 확인하는 API")
        binding.iconThreeDot.setOnClickListener {
            findNavController().navigate(
                R.id.action_studyContentFragment_to_bottomSheetFragment,
                null
            )
        }
    }


}