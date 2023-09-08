package kr.co.gamja.study_hub.feature.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentMainHomeBinding


class MainHomeFragment : Fragment() {
    private lateinit var binding: FragmentMainHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =DataBindingUtil.inflate(layoutInflater,R.layout.fragment_main_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 툴바 설정
        val toolbar = binding.mainToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""
        binding.iconH.setOnClickListener {
            findNavController().navigate(
                R.id.action_mainFragment01_self,
                null
            )
        }
        binding.iconStudyHub.setOnClickListener {
            findNavController().navigate(
                R.id.action_mainFragment01_self,
                null
            )
        }
        binding.iconBookmark.setOnClickListener {
            findNavController().navigate(
                R.id.action_global_mainBookmarkFragment,
                null
            )
        }
        binding.iconAlarm.setOnClickListener {
            findNavController().navigate(
                R.id.action_global_mainAlarmFragment,
                null
            )
        }

        // 검색창으로 넘어감
        binding.btnSearch.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment01_to_mainFragment02, null)
        }
        // 설명으로 넘어감
        binding.btnGoGuide.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment01_to_mainFragment03, null)
        }

    }


}