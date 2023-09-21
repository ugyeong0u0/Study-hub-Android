package kr.co.gamja.study_hub.feature.toolbar.bookmark

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.databinding.FragmentBookmarkBinding
import kr.co.gamja.study_hub.global.CustomDialog
import kr.co.gamja.study_hub.global.OnDialogClickListener
import okhttp3.internal.wait

class BookmarkFragment : Fragment() {
    private lateinit var binding: FragmentBookmarkBinding
    private val viewModel: BookmarkViewModel by viewModels()
    private val tag= this.javaClass.simpleName
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_bookmark, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        // 툴바 설정
        val toolbar = binding.bookMarkToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        // 북마크 조회 리사이클러뷰 연결
        val adapter = BookmarkAdapter()
        binding.recylerBookmark.adapter = adapter
        binding.recylerBookmark.layoutManager = LinearLayoutManager(requireContext())
        // 북마크 조회 api통신
        viewModel.getBookmarkList(adapter)

        adapter.setOnItemClickListener(object:OnItemClickListener{
            override fun onItemClick(tagId: String, postId: Int?) {
               when(tagId){
                   "0"->Toast.makeText(requireContext(),"삭제인 경우",Toast.LENGTH_SHORT).show()
                   "1"->{viewModel.saveBookmarkItem(postId)
                       Log.d(tag,postId.toString())
                   }
               }
            }
        })



        binding.iconBack.setOnClickListener {
            val navcontroller = findNavController()
            navcontroller.navigateUp() // 뒤로 가기
        }
        // 전체 삭제
        // TODO("리스트 값이 있을 때만 삭제 버튼 가능하게")
        binding.btnDeleteAll.setOnClickListener {
            val head = requireContext().resources.getString(R.string.q_deleteBookmark)
            val no = requireContext().resources.getString(R.string.btn_cancel)
            val yes = requireContext().resources.getString(R.string.btn_delete)
            val dialog = CustomDialog(requireContext(), head, null, no, yes)
            dialog.showDialog()
            dialog.setOnClickListener(object : OnDialogClickListener {
                override fun onclickResult() {
                    // TODO("삭제 누를 시 통신")

                }
            })
        }

    }
}