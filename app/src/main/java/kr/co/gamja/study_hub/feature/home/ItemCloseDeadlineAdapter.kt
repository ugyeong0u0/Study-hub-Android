package kr.co.gamja.study_hub.feature.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.model.ContentX
import kr.co.gamja.study_hub.data.model.FindStudyResponse
import kr.co.gamja.study_hub.data.repository.OnViewClickListener
import kr.co.gamja.study_hub.databinding.HomeItemCloseDeadlineBinding
import kr.co.gamja.study_hub.feature.toolbar.bookmark.OnItemClickListener

class ItemCloseDeadlineAdapter(private val context: Context) :
    RecyclerView.Adapter<ItemCloseDeadlineAdapter.ItemCloseDeadlineHolder>() {
    var studyPosts: FindStudyResponse? = null
    private lateinit var mOnItemClickListener: OnItemClickListener // 북마크 viewModel에 interface 선언
    private lateinit var mOnViewClickListener: OnViewClickListener // 뷰자체 클릭
    fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener
    }

    fun setViewClickListener(listener: OnViewClickListener) {
        mOnViewClickListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemCloseDeadlineAdapter.ItemCloseDeadlineHolder {
        val binding =
            HomeItemCloseDeadlineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemCloseDeadlineHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ItemCloseDeadlineAdapter.ItemCloseDeadlineHolder,
        position: Int
    ) {
        val studyPostsItem = studyPosts?.content?.get(position)
        holder.setPosts(studyPostsItem)
    }

    override fun getItemCount(): Int {
        return studyPosts?.content?.size ?: 0
    }

    inner class ItemCloseDeadlineHolder(val binding: HomeItemCloseDeadlineBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val itemPosition = bindingAdapterPosition
                if (itemPosition != RecyclerView.NO_POSITION) {
                    studyPosts?.content?.get(itemPosition).let {
                        val bindingPostId = it?.postId
                        mOnViewClickListener.onViewClick(bindingPostId)
                    }
                }
            }
        }

        fun setPosts(studyItem: ContentX?) {
            val postId: Int? = studyItem?.postId
            studyItem?.let {
                binding.txtHead.text = it.title
                // 남은 자리
                val full = it.studyPerson-it.remainingSeat // 채워진 인원
                binding.sentencePeople.text =
                    context.getString(R.string.sentence_people, it.remainingSeat) // %d 자리 남았어요
                binding.txtPeople.text =
                    context.getString(R.string.txt_people, full, it.studyPerson) // 채워진자리/전체인원
                // 스터디 생성한 사람 프로필 이미지
                Glide.with(context)
                    .load(it.userData.imageUrl)
                    .apply(
                        RequestOptions().override(
                            binding.iconProfile.width,
                            binding.iconProfile.height
                        )
                    )
                    .into(binding.iconProfile)
                // 북마크 여부에 따른 색 변경
                if (it.bookmarked) {
                    binding.btnBookmark.setBackgroundResource(R.drawable.baseline_bookmark_24_selected)
                } else {
                    binding.btnBookmark.setBackgroundResource(R.drawable.baseline_bookmark_border_24_unselected)
                }

                //북마크 추가
                binding.btnBookmark.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(p0: View?) {
                        // 북마크 추가
                        if (binding.btnBookmark.tag.toString() == "0") {
                            binding.btnBookmark.tag = "1"
                            binding.btnBookmark.setBackgroundResource(R.drawable.baseline_bookmark_24_selected)
                            val tagId = binding.btnBookmark.tag.toString()
                            mOnItemClickListener.onItemClick(tagId, postId)

                        } else { // 북마크 삭제
                            binding.btnBookmark.setTag("0")
                            binding.btnBookmark.setBackgroundResource(R.drawable.baseline_bookmark_border_24_unselected)
                            val tagId = binding.btnBookmark.tag.toString()
                            mOnItemClickListener.onItemClick(tagId, postId)
                        }
                    }
                })
            }
        }
    }
}