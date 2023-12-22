package kr.co.gamja.study_hub.feature.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.model.ContentX
import kr.co.gamja.study_hub.data.model.FindStudyResponse
import kr.co.gamja.study_hub.data.repository.OnBookmarkClickListener
import kr.co.gamja.study_hub.data.repository.OnViewClickListener
import kr.co.gamja.study_hub.databinding.HomeItemOnRecruitingBinding
import kr.co.gamja.study_hub.global.Functions

class ItemOnRecruitingAdapter(private val context: Context) :
    RecyclerView.Adapter<ItemOnRecruitingAdapter.MainHolder>() {
    var studyPosts: FindStudyResponse? = null
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
        val studyPostsItem = studyPosts?.content?.get(position)
        holder.setPosts(studyPostsItem)
    }

    override fun getItemCount(): Int {
        return studyPosts?.content?.size ?: 0
    }

    inner class MainHolder(val binding: HomeItemOnRecruitingBinding) :
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
                val functions = Functions()
                val koreanMajor = functions.convertToKoreanMajor(it.major) // 학과명 한글로 변경
                binding.txtMajor.text = koreanMajor
                binding.txtHead.text = it.title // 제목
                val vacancy = it.studyPerson - it.remainingSeat // 공석자리
                binding.txtPeople.text =
                    context.getString(R.string.txt_people, vacancy, it.studyPerson) // 참여인원/참여최대인원
                binding.txtRemainingSeats.text =
                    context.getString(R.string.txt_RemainingSeats, vacancy) // %d자리 남았어요
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
                    binding.txtFee.text = feeStBuilder.toString()
                }
                // 북마크 여부
                if (it.bookmarked) {
                    binding.btnBookmark.setBackgroundResource(R.drawable.baseline_bookmark_24_selected)
                } else {
                    binding.btnBookmark.setBackgroundResource(R.drawable.baseline_bookmark_border_24_unselected)
                }
                //북마크 추가 버튼 클릭 리스너
                binding.btnBookmark.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(p0: View?) {
                        // 북마크 추가
                        if (binding.btnBookmark.tag.toString() == "0") {
                            binding.btnBookmark.tag = "1"
                            binding.btnBookmark.setBackgroundResource(R.drawable.baseline_bookmark_24_selected)
                            val tagId = binding.btnBookmark.tag.toString()
                            mOnBookmarkClickListener.onItemClick(tagId, postId)

                        } else { // 북마크 삭제
                            binding.btnBookmark.setTag("0")
                            binding.btnBookmark.setBackgroundResource(R.drawable.baseline_bookmark_border_24_unselected)
                            val tagId = binding.btnBookmark.tag.toString()
                            mOnBookmarkClickListener.onItemClick(tagId, postId)
                        }
                    }
                })
            }
        }
    }
}
