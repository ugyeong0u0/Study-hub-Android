package kr.co.gamja.study_hub.feature.home

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.model.ContentXXXX
import kr.co.gamja.study_hub.data.model.FindStudyResponseM
import kr.co.gamja.study_hub.data.repository.OnBookmarkClickListener
import kr.co.gamja.study_hub.data.repository.OnViewClickListener
import kr.co.gamja.study_hub.databinding.HomeItemCloseDeadlineBinding

class ItemCloseDeadlineAdapter(private val context: Context) :
    RecyclerView.Adapter<ItemCloseDeadlineAdapter.ItemCloseDeadlineHolder>() {
    var isUserLogin : Boolean =true // 유저인지 아닌지
    var studyPosts: FindStudyResponseM? = null
    private lateinit var mOnBookmarkClickListener: OnBookmarkClickListener // 북마크 viewModel에 interface 선언
    private lateinit var mOnViewClickListener: OnViewClickListener // 뷰자체 클릭
    fun setOnItemClickListener(listener: OnBookmarkClickListener) {
        mOnBookmarkClickListener = listener
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
        val studyPostsItem = studyPosts?.postDataByInquiries?.content?.get(position)
        holder.setPosts(studyPostsItem)
    }

    override fun getItemCount(): Int {
        return studyPosts?.postDataByInquiries?.content?.size ?: 0
    }

    inner class ItemCloseDeadlineHolder(val binding: HomeItemCloseDeadlineBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val itemPosition = bindingAdapterPosition
                if (itemPosition != RecyclerView.NO_POSITION) {
                    studyPosts?.postDataByInquiries?.content?.get(itemPosition).let {
                        val bindingPostId = it?.postId
                        mOnViewClickListener.onViewClick(bindingPostId)
                    }
                }
            }
        }

        fun setPosts(studyItem: ContentXXXX?) {
            val postId: Int? = studyItem?.postId
            studyItem?.let {
                binding.txtHead.text = it.title
                // 남은 자리
                val full = it.studyPerson - it.remainingSeat // 채워진 인원
                binding.sentencePeople.text =
                    context.getString(R.string.sentence_people, it.remainingSeat) // %d 자리 남았어요

                val spannableString =
                    SpannableString( context.getString(R.string.txt_people, full, it.studyPerson))
                val color = context.getColor(R.color.O_50)
                val unAvailableSeatLength = full.toString().length
                spannableString.setSpan(
                    ForegroundColorSpan(color),
                    0,
                    unAvailableSeatLength,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                )
                binding.txtPeople.text =spannableString // 채워진자리/전체인원

                // 스터디 생성한 사람 프로필 이미지
                Glide.with(context)
                    .load(it.userData.imageUrl)
                    .apply(
                        RequestOptions().override(
                            binding.iconProfile.width,
                            binding.iconProfile.height
                        ).circleCrop()
                    )
                    .into(binding.iconProfile)
                // 북마크 여부에 따른 색 변경
                binding.isBookmark = it.bookmarked

                    //북마크 추가
                    binding.btnBookmark.setOnClickListener {
                        // 로그인 된 유저만 되도록
                        if(isUserLogin){
                            when (binding.isBookmark) {
                                true -> {
                                    binding.isBookmark = false
                                    binding.btnBookmark.tag = "0"
                                }
                                false -> {
                                    binding.isBookmark = true
                                    binding.btnBookmark.tag = "1"
                                }
                            }
                        }
                        val tagId = binding.btnBookmark.tag.toString()
                        mOnBookmarkClickListener.onItemClick(tagId, postId) // fragment로 callback부분
                    }

            }
        }
    }
}