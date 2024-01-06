package kr.co.gamja.study_hub.feature.studypage.studyContent

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kr.co.gamja.study_hub.data.model.Content
import kr.co.gamja.study_hub.databinding.CommentItemBinding

// 스터디내용 조회 페이지에서 댓글 8개 보는 adapter
class CommentAdapter(private val context: Context) :
    RecyclerView.Adapter<CommentAdapter.CommentHolder>() {
    var commentsList: List<Content>? = null

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {
        val binding = CommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        val commentsListItem = commentsList?.get(position)
        holder.bind(commentsListItem)
    }

    override fun getItemCount(): Int {
        return commentsList?.size ?: 0
    }

    inner class CommentHolder(val binding: CommentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Content?) {
            comment?.let {
                binding.userNickname.text = it.commentedUserData.nickname
                val st = StringBuilder()
                st.append(it.createdDate[0])
                    .append(".")
                    .append(it.createdDate[1])
                    .append(".")
                    .append(it.createdDate[2])
                binding.createdDate.text = st.toString()
                binding.userComment.text = it.content
                Glide.with(context)
                    .load(it.commentedUserData.imageUrl)
                    .apply(
                        RequestOptions().override(
                            binding.imgProfile.width,
                            binding.imgProfile.height
                        )
                    )
                    .into(binding.imgProfile)
            }
        }
    }
}