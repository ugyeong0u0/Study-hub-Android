package kr.co.gamja.study_hub.feature.mypage.participant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentParticipantBinding
import kr.co.gamja.study_hub.feature.mypage.participant.waiting.WaitingContentAdapter

class ParticipantFragment : Fragment() {
    private val logMessage = this.javaClass.simpleName
    private lateinit var binding: FragmentParticipantBinding
    private lateinit var participantPagerAdapter: ParticipantPagerAdapter
    private lateinit var viewPager :ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_participant, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // todo("대기,거절,참여 연결해야함 ")
        // postId, studyId 작성한글 페이지(WrittenStudyFragment.kt)에서 받음
        val studyId = arguments?.getInt("studyId")
        val postId = arguments?.getInt("postId")

        // viewPager2 Adapter설정
        participantPagerAdapter = ParticipantPagerAdapter(this, studyId, postId)
        viewPager= binding.pager
        viewPager.adapter=participantPagerAdapter
        // tab과 viewPager2 연결
        TabLayoutMediator(binding.tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = requireContext().getString(R.string.waiting)
                }
                1 -> {
                    tab.text = requireContext().getString(R.string.participation)
                }
                2 -> {
                    tab.text = requireContext().getString(R.string.refusal)
                }
            }
        }.attach()

        // 툴바 설정
        val toolbar = binding.participantToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        binding.iconBack.setOnClickListener {
            findNavController().navigateUp() // 뒤로 가기
        }


    }
}