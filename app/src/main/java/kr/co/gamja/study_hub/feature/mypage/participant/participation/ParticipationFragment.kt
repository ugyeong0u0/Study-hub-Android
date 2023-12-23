package kr.co.gamja.study_hub.feature.mypage.participant.participation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentParticipantBinding
import kr.co.gamja.study_hub.databinding.FragmentParticipationBinding

class ParticipationFragment : Fragment() {
    private lateinit var binding:FragmentParticipationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            layoutInflater,R.layout.fragment_participation, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}