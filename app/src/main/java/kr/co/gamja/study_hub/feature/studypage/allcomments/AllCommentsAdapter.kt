package kr.co.gamja.study_hub.feature.studypage.allcomments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kr.co.gamja.study_hub.data.model.Content
import kr.co.gamja.study_hub.data.repository.OnCommentClickListener
import kr.co.gamja.study_hub.databinding.CommentItemBinding

class AllCommentsAdapter(val context: Context) :
    PagingDataAdapter<Content, AllCommentsAdapter.AllCommentHolder>(DIFF_CALLBACK) {
    val whatItem = mapOf("threeDot" to 1)
    private lateinit var mOnCommentClickListener: OnCommentClickListener

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Content>() {
            override fun areItemsTheSame(oldItem: Content, newItem: Content): Boolean {
                return oldItem.commentId == newItem.commentId
            }

            override fun areContentsTheSame(oldItem: Content, newItem: Content): Boolean {
                return oldItem == newItem
            }
        }

    }

    // 세개 닷 누르기 버튼
    fun setOnItemClickListener(listener: OnCommentClickListener) {
        mOnCommentClickListener = listener
    }

    override fun onBindViewHolder(holder: AllCommentHolder, position: Int) {
        val content = getItem(position)
        content?.let { holder.bind(it) }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllCommentHolder {
        val binding = CommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllCommentHolder(binding)
    }


    inner class AllCommentHolder(val binding: CommentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Content) {
            item.let {
                val postId: Int = it.commentId
                val comment: String = it.content
                binding.userNickname.text = it.commentedUserData.nickname
                val st = StringBuilder()
                st.append(it.createdDate[0])
                    .append(".")
                    .append(it.createdDate[1])
                    .append(".")
                    .append(it.createdDate[2])
                binding.createdDate.text = st.toString()
                binding.userComment.text = comment
                Glide.with(context)
                    .load(it.commentedUserData.imageUrl)
                    .apply(
                        RequestOptions().override(
                            binding.imgProfile.width,
                            binding.imgProfile.height
                        )
                    )
                    .into(binding.imgProfile)
                // 세개 점 클릭
                binding.iconThreeDot.setOnClickListener {
                    mOnCommentClickListener.getCommentValue(whatItem["threeDot"]!!, postId, comment)
                }
                // 댓 쓴 유저인지 확인-> 새개 점 노출 여부 결정
                if (!it.usersComment) {
                    binding.iconThreeDot.visibility = View.INVISIBLE
                } else binding.iconThreeDot.visibility = View.VISIBLE
            }
        }
    }

}