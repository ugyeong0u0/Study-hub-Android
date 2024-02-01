package kr.co.gamja.study_hub.feature.mypage.participant.waiting

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentWaitingBinding
import kr.co.gamja.study_hub.feature.mypage.participant.ParticipantViewModel

class WaitingFragment : Fragment() {

    private lateinit var binding : FragmentWaitingBinding
    private lateinit var adapter : WaitingContentAdapter

    private val viewModel : ParticipantViewModel by viewModels()

    private var pageNum = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_waiting, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val studyId = arguments?.getInt("studyId") ?: -1
        val postId = arguments?.getInt("postId") ?: -1
        Log.d("ParticipantFragment", "studyId : ${studyId}")
        initRecyclerView(studyId)

        //observing
        viewModel.participantWaitingList.observe(viewLifecycleOwner, Observer{
            adapter.submitList(it)
            if (it.isEmpty()){
                binding.imgEmptyParticipation.visibility = View.VISIBLE
                binding.tvComment.visibility = View.VISIBLE
                binding.rcvContent.visibility = View.GONE
            } else {
                binding.imgEmptyParticipation.visibility = View.GONE
                binding.tvComment.visibility = View.GONE
                binding.rcvContent.visibility = View.VISIBLE
            }
        })

        viewModel.fetchData(studyId, pageNum)
    }

    //RecyclerView 초기화
    private fun initRecyclerView(studyId : Int){

        if (studyId == -1) {
            Log.d("Participant", "studyId is NULL")
            return
        }

        adapter = WaitingContentAdapter(requireContext())
        val listener = object : WaitingContentAdapter.OnClickListener{
            override fun onAcceptClick(userId : Int) {
                viewModel.accept(studyId, userId)
                viewModel.fetchData(studyId, pageNum)
            }

            override fun onRefusalClick(userId : Int) {
                viewModel.refusal(studyId, userId)
                viewModel.fetchData(studyId, pageNum)
            }
        }

        adapter.setOnClickListener(listener)
        binding.rcvContent.adapter = adapter
        binding.rcvContent.layoutManager = LinearLayoutManager(requireContext())
    }
}