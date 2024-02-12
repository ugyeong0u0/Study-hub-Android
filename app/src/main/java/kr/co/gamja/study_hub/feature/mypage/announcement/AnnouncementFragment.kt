package kr.co.gamja.study_hub.feature.mypage.announcement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.gamja.study_hub.databinding.FragmentAnnoucementBinding

class AnnouncementFragment : Fragment() {

    private lateinit var binding : FragmentAnnoucementBinding
    private lateinit var adapter : AnnouncementAdapter

    private val viewModel : AnnounceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnnoucementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        //err 관찰 observer
        viewModel.errMsg.observe(viewLifecycleOwner, Observer{
            if (it != ""){
                /** 오류 발생 :: dialog 생성 후 뒤로가기? */
                
                //오류 생성 로직 후 errMsg 초기화
                viewModel.resetErr()
            }
        })

        //공지사항 list 관찰 observer
        viewModel.announcementList.observe(viewLifecycleOwner, Observer{
            adapter.submitList(it)
        })

        viewModel.initList()

        //recyclerView 설정
        initRecyclerView()
    }

    //RecyclerView 초기화
    private fun initRecyclerView(){
        adapter = AnnouncementAdapter()
        binding.rcvContent.adapter = adapter
        binding.rcvContent.layoutManager = LinearLayoutManager(requireContext())
    }
}