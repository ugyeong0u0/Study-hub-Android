package kr.co.gamja.study_hub.feature.mypage.engagedStudy

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.gamja.study_hub.data.model.ContentX
import kr.co.gamja.study_hub.data.repository.CallBackStringListener
import kr.co.gamja.study_hub.data.repository.OnItemsClickListener
import kr.co.gamja.study_hub.databinding.EngagedstudyItemBinding

class EngagedStudyAdapter(private val context: Context) :
    PagingDataAdapter<ContentX, EngagedStudyAdapter.EngagedStudyHolder>(
        DIFF_CALLBACK
    ) {
    val tag: String = this.javaClass.simpleName

    val whatItem = mapOf("delete" to 1)

    private lateinit var mOnItemsClickListener: OnItemsClickListener
    private lateinit var mCallBackStringListener : CallBackStringListener
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ContentX>() {
            override fun areItemsTheSame(oldItem: ContentX, newItem: ContentX): Boolean {
                return oldItem.postId == newItem.postId
            }

            override fun areContentsTheSame(oldItem: ContentX, newItem: ContentX): Boolean {
                return oldItem == newItem
            }
        }
    }
    // x 눌렀을 때
    fun setOnItemsClickListener(listener: OnItemsClickListener) {
        mOnItemsClickListener = listener
    }
    // 챗으로 갈때 리스너
    fun setCallBackStringListener(listener: CallBackStringListener) {
        mCallBackStringListener = listener
    }

    override fun onBindViewHolder(holder: EngagedStudyHolder, position: Int) {
        val content = getItem(position)
        content?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EngagedStudyHolder {
        val binding = EngagedstudyItemBinding.inflate( LayoutInflater.from(parent.context),
            parent,
            false)
        return EngagedStudyHolder(binding)
    }

    inner class EngagedStudyHolder(val binding: EngagedstudyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ContentX?) {
            val chatLink = item?.chatUrl
            val postId= item?.postId
            item?.let {
                binding.txtMajor.text = it.major
                binding.txtTitle.text=it.title
                binding.txtContent.text =it.content
            }
            // threeDot이 아니라 x 임
            binding.btnThreeDot.setOnClickListener{
                mOnItemsClickListener.getItemValue(whatItem["delete"]!!, postId!!)
            }
            // 채팅방으로 가기
            binding.goChatRoom.setOnClickListener{
                mCallBackStringListener.isSuccess(chatLink!!)
            }
        }
    }
}