package kr.co.gamja.study_hub.feature.mypage.participant.refusal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentParticipationBinding
import kr.co.gamja.study_hub.databinding.FragmentRefusalBinding
import kr.co.gamja.study_hub.feature.mypage.participant.ParticipantViewModel
import kr.co.gamja.study_hub.feature.mypage.participant.participation.ParticipationAdapter

class RefusalFragment : Fragment() {

    private lateinit var binding : FragmentRefusalBinding
    private lateinit var adapter : RefusalAdapter
    private val viewModel : ParticipantViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            layoutInflater,R.layout.fragment_refusal, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        //observing
        viewModel.refuseList.observe(viewLifecycleOwner, Observer{
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
    }

    //RecyclerView 초기화
    private fun initRecyclerView(){
        adapter = RefusalAdapter(requireContext())
        binding.rcvContent.adapter = adapter
        binding.rcvContent.layoutManager = LinearLayoutManager(requireContext())
    }
}