package kr.co.gamja.study_hub.feature.mypage.applicationhistory

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.gamja.study_hub.data.model.ContentXXXXX
import kr.co.gamja.study_hub.data.repository.OnItemsClickListener
import kr.co.gamja.study_hub.databinding.ApplicationHistoryItemBinding

class ApplicationHistoryAdapter(private val context: Context) :
    PagingDataAdapter<ContentXXXXX, ApplicationHistoryAdapter.ApplicationHistoryHolder>(
        DIFF_CALLBACK
    ) {

    val whatItem = mapOf("delete" to 1, "rejectReason" to 2, "seeContent" to 3)
    private lateinit var mOnItemsClickListener: OnItemsClickListener

    val tag: String = this.javaClass.simpleName

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ContentXXXXX>() {
            override fun areItemsTheSame(oldItem: ContentXXXXX, newItem: ContentXXXXX): Boolean {
                return oldItem.studyId == newItem.studyId
            }

            override fun areContentsTheSame(oldItem: ContentXXXXX, newItem: ContentXXXXX): Boolean {
                return oldItem == newItem
            }
        }
    }

    fun setOnItemsClickListener(listener: OnItemsClickListener) {
        mOnItemsClickListener = listener
    }

    override fun onBindViewHolder(holder: ApplicationHistoryHolder, position: Int) {
        val content = getItem(position)
        content?.let { holder.setData(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationHistoryHolder {
        val binding = ApplicationHistoryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ApplicationHistoryHolder(binding)
    }

    inner class ApplicationHistoryHolder(val binding: ApplicationHistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(item: ContentXXXXX?) {
            val itemStudyId: Int? = item?.studyId
            item?.let {
                when (it.inspection) {
                    "STANDBY" -> {
                        binding.isDeny = false
                    }
                    "REJECT" -> {
                        binding.isDeny = true
                    }
                }
                binding.txtTitle.text = it.studyTitle
                binding.txtContentSub.text = it.introduce

            }

            // 삭제 버튼
            binding.btnDelete.setOnClickListener {
                mOnItemsClickListener.getItemValue(whatItem["delete"]!!, itemStudyId!!)
            }
            // 거절 사유 보기
            binding.btnRejectReason.setOnClickListener {
                mOnItemsClickListener.getItemValue(whatItem["rejectReason"]!!, itemStudyId!!)
            }
            // 뷰자체 클릭
            // todo("postId로 변경필수 ")
            itemView.setOnClickListener {
                mOnItemsClickListener.getItemValue(whatItem["seeContent"]!!, itemStudyId!!)
            }
        }
    }
}