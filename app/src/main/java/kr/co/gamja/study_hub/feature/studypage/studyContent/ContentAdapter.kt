package kr.co.gamja.study_hub.feature.studypage.studyContent

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.model.RelatedPost
import kr.co.gamja.study_hub.data.repository.OnViewClickListener
import kr.co.gamja.study_hub.databinding.StudyItemRecommendstudyBinding

class ContentAdapter(private val context: Context) :
    RecyclerView.Adapter<ContentAdapter.ItemRecommendHolder>() {
    var studyPosts: List<RelatedPost>? = null
    private lateinit var mOnViewClickListener: OnViewClickListener // 뷰자체 클릭
    fun setViewClickListener(listener: OnViewClickListener) {
        mOnViewClickListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemRecommendHolder {
        val binding = StudyItemRecommendstudyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemRecommendHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemRecommendHolder, position: Int) {
        val studyPostsItem = studyPosts?.get(position)
        holder.setPosts(studyPostsItem)
    }

    override fun getItemCount(): Int {
        return studyPosts?.size ?: 0
    }

    inner class ItemRecommendHolder(val binding: StudyItemRecommendstudyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val itemPosition = bindingAdapterPosition
                if (itemPosition != RecyclerView.NO_POSITION) {
                    studyPosts?.get(itemPosition).let {
                        val bindingPostId = it?.postId
                        mOnViewClickListener.onViewClick(bindingPostId)
                    }
                }
            }
        }

        fun setPosts(studyItem: RelatedPost?) {

            studyItem?.let {
                binding.txtMajor.text = it.major
                binding.txtHead.text = it.title
                binding.txtLeftNumber.text =
                    context.getString(R.string.sentence_people, it.remainingSeat)
                binding.userMajor.text = it.postedUser.major
                binding.userNickname.text = it.postedUser.nickname
                //TODO("사진추가")
            }
        }
    }
}