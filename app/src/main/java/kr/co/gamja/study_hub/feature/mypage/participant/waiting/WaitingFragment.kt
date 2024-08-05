package kr.co.gamja.study_hub.feature.mypage.participant.waiting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentWaitingBinding
import kr.co.gamja.study_hub.feature.mypage.participant.BottomSheet
import kr.co.gamja.study_hub.global.RcvDecoration

class WaitingFragment : Fragment() {

    private lateinit var binding : FragmentWaitingBinding
    private lateinit var adapter : WaitingContentAdapter

    private val viewModel : WaitingViewModel by activityViewModels()

    private var studyId = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_waiting, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        studyId = arguments?.getInt("studyId") ?: -1

        initRecyclerView(studyId)

        if (studyId != -1) {
            viewModel.fetchData(studyId = studyId)
        }

        //observing
        viewModel.participantWaitingList.observe(viewLifecycleOwner, Observer{
            adapter.submitList(it, 0)
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
        viewModel.fetchData(studyId)
        super.onResume()
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
                viewModel.fetchData(studyId)
                refreshFragment(this@WaitingFragment, parentFragmentManager)
            }

            //거절 선택 >> BottomFragment
            override fun onRefusalClick(userId : Int) {
                val bottomSheetFragment = BottomSheet()

                val bundle = Bundle()
                bundle.putInt("userId", userId)
                bundle.putInt("studyId", studyId)
                bundle.putInt("page", 0)
                bottomSheetFragment.arguments = bundle
                bottomSheetFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
                bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
                refreshFragment(this@WaitingFragment, parentFragmentManager)

            }
        }

        adapter.setOnClickListener(listener)
        binding.rcvContent.adapter = adapter
        binding.rcvContent.layoutManager = LinearLayoutManager(requireContext())
        val itemSpace = resources.getDimensionPixelSize(R.dimen.thirty)
        val deco = RcvDecoration(itemSpace)
        binding.rcvContent.addItemDecoration(deco)
    }

    //fragment refresh
    private fun refreshFragment(fragment : Fragment, fragmentManager : FragmentManager){
        val ft = fragmentManager.beginTransaction()
        ft.detach(fragment).attach(fragment).commit()
    }
}