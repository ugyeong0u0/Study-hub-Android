package kr.co.gamja.study_hub.feature.studypage.studyContent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.repository.OnViewClickListener
import kr.co.gamja.study_hub.databinding.FragmentContentBinding
import kr.co.gamja.study_hub.feature.studypage.studyContent.correctStudy.BottomSheetFragment


class ContentFragment : Fragment() {
    private lateinit var binding:FragmentContentBinding
    private val args: ContentFragmentArgs by navArgs()
    private val viewModel : ContentViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_content,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val contentAdapter = ContentAdapter(requireContext())
        getContent(contentAdapter,args.postId)
        // 툴바 설정
        val toolbar = binding.contentToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        binding.iconBack.setOnClickListener {
            val navcontroller = findNavController()
            navcontroller.navigateUp() // 뒤로 가기
        }


        binding.iconThreeDot.setOnClickListener {
            getModal(args.postId)
        }

        binding.recyclerRecommend.adapter=contentAdapter
        binding.recyclerRecommend.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
        contentAdapter.setViewClickListener(object: OnViewClickListener{
            override fun onViewClick(postId: Int?) {
                val action = ContentFragmentDirections.actionGlobalStudyContentFragment(postId!!)
                findNavController().navigate(action)
            }
        })
    }

    private fun getContent(adapter : ContentAdapter, postId :Int){
        viewModel.getStudyContent(adapter, postId)
    }
    private fun getModal(postId: Int){
        val bundle = Bundle()
        bundle.putInt("postId", postId)
        val modal = BottomSheetFragment()
        modal.arguments = bundle
        modal.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
        modal.show(parentFragmentManager, modal.tag)
    }

}