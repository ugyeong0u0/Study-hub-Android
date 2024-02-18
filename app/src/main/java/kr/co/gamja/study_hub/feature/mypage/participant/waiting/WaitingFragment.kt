package kr.co.gamja.study_hub.feature.mypage.participant.waiting

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentWaitingBinding
import kr.co.gamja.study_hub.feature.mypage.participant.BottomSheet
import kr.co.gamja.study_hub.feature.mypage.participant.ParticipantViewModel
import kr.co.gamja.study_hub.global.RcvDecoration

class WaitingFragment : Fragment() {

    private lateinit var binding : FragmentWaitingBinding
    private lateinit var adapter : WaitingContentAdapter

    private val viewModel : ParticipantViewModel by viewModels()

    private var page = 0

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

        viewModel.fetchData("STANDBY", studyId, page)
    }

    //RecyclerView 초기화
    private fun initRecyclerView(studyId : Int){

        if (studyId == -1) {
            Log.d("Participant", "studyId is NULL")
            return
        }

        adapter = WaitingContentAdapter(requireContext())
        val listener = object : WaitingContentAdapter.OnClickListener{

            /** 수락 선택 >> Dialog 띄워야 함 */
            override fun onAcceptClick(userId : Int) {
                viewModel.accept(studyId, userId)
                viewModel.fetchData("STANDBY", studyId, page)
            }

            //거절 선택 >> BottomFragment
            override fun onRefusalClick(userId : Int) {
                val bottomSheetFragment = BottomSheet()

                val bundle = Bundle()
                bundle.putInt("userId", userId)
                bundle.putInt("studyId", studyId)
                bundle.putInt("page", page)
                bottomSheetFragment.arguments = bundle
                bottomSheetFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
                bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
            }
        }

        adapter.setOnClickListener(listener)
        binding.rcvContent.adapter = adapter
        binding.rcvContent.layoutManager = LinearLayoutManager(requireContext())
        val itemSpace = resources.getDimensionPixelSize(R.dimen.thirty)
        val deco = RcvDecoration(itemSpace)
        binding.rcvContent.addItemDecoration(deco)
    }
}