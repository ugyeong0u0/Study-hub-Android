package kr.co.gamja.study_hub.feature.mypage.participant

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.co.gamja.study_hub.feature.mypage.participant.participation.ParticipationFragment
import kr.co.gamja.study_hub.feature.mypage.participant.refusal.RefusalFragment
import kr.co.gamja.study_hub.feature.mypage.participant.waiting.WaitingFragment

class ParticipantPagerAdapter(fragment: Fragment, studyId : Int?, postId : Int?): FragmentStateAdapter(fragment){
    private val fragmentList = listOf(
        WaitingFragment().apply{
            arguments = Bundle().apply{
                putInt("studyId", studyId ?: -1)
                putInt("postId", postId ?: -1)
            }
        },
        ParticipationFragment().apply{
            arguments = Bundle().apply{
                putInt("studyId", studyId ?: -1)
            }
        },
        RefusalFragment().apply{
            arguments = Bundle().apply{
                putInt("studyId", studyId ?: -1)
            }
        }
    )
    override fun getItemCount(): Int = fragmentList.size // fragment 개수

    override fun createFragment(position: Int): Fragment {
       return fragmentList[position]
    }
}