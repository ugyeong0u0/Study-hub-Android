package kr.co.gamja.study_hub.feature.toolbar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.datastore.App
import kr.co.gamja.study_hub.databinding.FragmentBookmarkBinding
import kr.co.gamja.study_hub.global.CustomDialog
import kr.co.gamja.study_hub.global.OnDialogClickListener

class BookmarkFragment : Fragment() {
    private lateinit var binding : FragmentBookmarkBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(layoutInflater, R.layout.fragment_bookmark,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 툴바 설정
        val toolbar = binding.bookMarkToolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        binding.iconBack.setOnClickListener{
            val navcontroller = findNavController()
            navcontroller.navigateUp() // 뒤로 가기
        }
        // TODO("리스트 값이 있을 때만 삭제 버튼 가능하게")
        binding.btnDeleteAll.setOnClickListener{
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