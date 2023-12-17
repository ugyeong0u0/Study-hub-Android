package kr.co.gamja.study_hub.feature.mypage.participant

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.co.gamja.study_hub.feature.mypage.participant.participation.ParticipationFragment
import kr.co.gamja.study_hub.feature.mypage.participant.refusal.RefusalFragment
import kr.co.gamja.study_hub.feature.mypage.participant.waiting.WaitingFragment

class ParticipantPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment){
    private val fragmentList = listOf(
        WaitingFragment(),
        ParticipationFragment(),
        RefusalFragment()
    )
    override fun getItemCount(): Int = fragmentList.size // fragment 개수

    override fun createFragment(position: Int): Fragment {
       return fragmentList[position]
    }
}