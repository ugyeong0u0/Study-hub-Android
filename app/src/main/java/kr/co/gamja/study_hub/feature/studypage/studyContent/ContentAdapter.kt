package kr.co.gamja.study_hub.feature.studypage.studyContent

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.model.RelatedPostX
import kr.co.gamja.study_hub.data.repository.OnViewClickListener
import kr.co.gamja.study_hub.databinding.StudyItemRecommendstudyBinding
import kr.co.gamja.study_hub.global.Functions

// 스터디모집 글 상세보기 안 하단 추천 스터디( 리사이클러뷰)
class ContentAdapter(private val context: Context) :
    RecyclerView.Adapter<ContentAdapter.ItemRecommendHolder>() {
    var studyPosts: List<RelatedPostX>? = null
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

        fun setPosts(studyItem: RelatedPostX?) {
            val functions = Functions()
            studyItem?.let {
                binding.txtMajor.text = functions.convertToKoreanMajor(it.major)
                binding.txtHead.text = it.title
                binding.txtLeftNumber.text =
                    context.getString(R.string.sentence_people, it.remainingSeat)
                binding.userMajor.text = functions.convertToKoreanMajor(it.userData.major)
                binding.userNickname.text = it.userData.nickname

                // 스터디 생성한 사람 프로필 이미지
                Glide.with(context)
                    .load(it.userData.imageUrl)
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