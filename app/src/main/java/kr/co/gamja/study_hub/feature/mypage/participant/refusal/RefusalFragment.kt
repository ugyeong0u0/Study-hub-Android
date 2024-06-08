package kr.co.gamja.study_hub.feature.mypage.participant.refusal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentRefusalBinding

class RefusalFragment : Fragment() {

    private lateinit var binding : FragmentRefusalBinding
    private lateinit var adapter : RefusalAdapter
    private val viewModel : RefusalViewModel by viewModels()

    private var studyId = -1
    private var page = 0

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

        studyId = arguments?.getInt("studyId") ?: throw NullPointerException("Arguments is NULL")

        initRecyclerView()

        //data fetch
        viewModel.fetchData(false, studyId, page)

        //observing
        viewModel.refuseList.observe(viewLifecycleOwner, Observer{
            adapter.submitList(it, page)
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

    override fun onResume() {
        if(studyId != -1){
            viewModel.fetchData(false, studyId, 0)
        } else {
            Log.e("Participant", "Refusal Study ID is NOT FOUND")
        }
        super.onResume()
    }

    //RecyclerView 초기화
    private fun initRecyclerView(){
        adapter = RefusalAdapter(requireContext())
        binding.rcvContent.adapter = adapter
        binding.rcvContent.layoutManager = LinearLayoutManager(requireContext())
    }
}