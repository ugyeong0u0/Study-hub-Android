package kr.co.gamja.study_hub.feature.studypage.studyContent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.repository.OnViewClickListener
import kr.co.gamja.study_hub.databinding.FragmentContentBinding
import kr.co.gamja.study_hub.feature.studypage.studyContent.correctStudy.BottomSheetFragment

// 스터디 상세 보기 관련
class ContentFragment : Fragment() {
    private lateinit var binding: FragmentContentBinding
    private val args: ContentFragmentArgs by navArgs()
    private val viewModel: ContentViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_content, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val contentAdapter = ContentAdapter(requireContext())
        getContent(contentAdapter, args.postId)
        // 툴바 설정
        val toolbar = binding.contentToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        binding.iconBack.setOnClickListener {
            findNavController().navigateUp() // 뒤로 가기
        }
        binding.iconThreeDot.setOnClickListener {
            getModal(args.postId)
        }
        // 스터디 생성한 사람 프로필 이미지
        Glide.with(this)
            .load(viewModel.userImg.value)
            .apply(
                RequestOptions().override(
                    binding.iconProfile.width,
                    binding.iconProfile.height
                )
            )
            .into(binding.iconProfile)

        binding.recyclerRecommend.adapter = contentAdapter
        binding.recyclerRecommend.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        contentAdapter.setViewClickListener(object : OnViewClickListener {
            override fun onViewClick(postId: Int?) {
                val action = ContentFragmentDirections.actionGlobalStudyContentFragment(postId!!)
                findNavController().navigate(action)
            }
        })
    }

    private fun getContent(adapter: ContentAdapter, postId: Int) {
        viewModel.getStudyContent(adapter, postId)
    }

    private fun getModal(postId: Int) {
        val bundle = Bundle()
        bundle.putInt("postId", postId)
        val modal = BottomSheetFragment()
        modal.arguments = bundle
        modal.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
        modal.show(parentFragmentManager, modal.tag)
    }

}