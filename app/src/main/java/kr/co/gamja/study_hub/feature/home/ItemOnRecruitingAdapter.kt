package kr.co.gamja.study_hub.feature.home

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.model.ContentXXXX
import kr.co.gamja.study_hub.data.model.FindStudyResponseM
import kr.co.gamja.study_hub.data.repository.OnBookmarkClickListener
import kr.co.gamja.study_hub.data.repository.OnViewClickListener
import kr.co.gamja.study_hub.databinding.HomeItemOnRecruitingBinding
import kr.co.gamja.study_hub.global.Functions

class ItemOnRecruitingAdapter(private val context: Context) :
    RecyclerView.Adapter<ItemOnRecruitingAdapter.MainHolder>() {
    val tag: String =this.javaClass.simpleName
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding =
            HomeItemOnRecruitingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val studyPostsItem = studyPosts?.postDataByInquiries?.content?.get(position)
        holder.setPosts(studyPostsItem)
    }

    override fun getItemCount(): Int {
        return studyPosts?.postDataByInquiries?.content?.size ?: 0
    }

    inner class MainHolder(val binding: HomeItemOnRecruitingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val itemPosition = bindingAdapterPosition
                if (itemPosition != RecyclerView.NO_POSITION) {
                    studyPosts?.postDataByInquiries?.content?.get(itemPosition).let {
                        val bindingPostId = it?.postId
//                        Log.e(tag,bindingPostId.toString())
                        mOnViewClickListener.onViewClick(bindingPostId)
                    }
                }
            }
        }

        fun setPosts(studyItem: ContentXXXX?) {
            val postId: Int? = studyItem?.postId
            studyItem?.let {
                val functions = Functions()
                val koreanMajor = functions.convertToKoreanMajor(it.major) // 학과명 한글로 변경
                binding.txtMajor.text = koreanMajor
                binding.txtHead.text = it.title // 제목
                val vacancy = it.studyPerson - it.remainingSeat // 공석자리

                val spannableString =
                    SpannableString(context.getString(R.string.txt_people, vacancy, it.studyPerson))
                val color = context.getColor(R.color.O_50)
                val unAvailableSeatLength = vacancy.toString().length
                spannableString.setSpan(
                    ForegroundColorSpan(color),
                    0,
                    unAvailableSeatLength,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                )
                binding.txtPeople.text = spannableString// 참여인원/참여최대인원

                binding.txtRemainingSeats.text =
                    context.getString(R.string.txt_RemainingSeats, it.remainingSeat) // %d자리 남았어요
                // 요금
                val feeStBuilder = StringBuilder()
                if (it.penalty == 0) {
                    feeStBuilder.clear()
                    feeStBuilder.append("없어요")
                    binding.txtFee.text = feeStBuilder.toString()
                } else {
                    feeStBuilder.clear()
                    feeStBuilder.append(it.penalty)
                        .append("원")

                    val penaltySpannableString =
                        SpannableString(feeStBuilder.toString())
                    val penaltyLength = feeStBuilder.toString().length-1
                    penaltySpannableString.setSpan(
                        ForegroundColorSpan(color),
                        0,
                        penaltyLength,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                    )

                    binding.txtFee.text = penaltySpannableString
                }
                // 북마크 여부
                binding.isBookmark = it.bookmarked

                //북마크 추가 버튼 클릭 리스너
                binding.btnBookmark.setOnClickListener {
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
                    val tagId = binding.btnBookmark.tag.toString()
                    mOnBookmarkClickListener.onItemClick(tagId, postId)
                }
            }
        }
    }
}
