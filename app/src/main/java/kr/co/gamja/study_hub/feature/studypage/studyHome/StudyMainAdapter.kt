package kr.co.gamja.study_hub.feature.studypage.studyHome

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.model.ContentXXXX
import kr.co.gamja.study_hub.data.repository.OnBookmarkClickListener
import kr.co.gamja.study_hub.data.repository.OnViewClickListener
import kr.co.gamja.study_hub.databinding.StudyItemOnRecruitingBinding
import kr.co.gamja.study_hub.global.Functions

class StudyMainAdapter(private val context: Context) :
    PagingDataAdapter<ContentXXXX, StudyMainAdapter.StudyMainHolder>(
        DIFF_CALLBACK
    ) {
    private lateinit var mOnBookmarkClickListener: OnBookmarkClickListener // 북마크 viewModel에 interface 선언
    private lateinit var mOnViewClickListener: OnViewClickListener // 뷰 자체 클릭

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ContentXXXX>() {
            override fun areItemsTheSame(oldItem: ContentXXXX, newItem: ContentXXXX): Boolean {
                return oldItem.postId == newItem.postId
            }

            override fun areContentsTheSame(oldItem: ContentXXXX, newItem: ContentXXXX): Boolean {
                return oldItem == newItem
            }
        }
    }

    fun setViewClickListener(listener: OnViewClickListener) {
        mOnViewClickListener = listener
    }

    fun setOnBookmarkClickListener(listener: OnBookmarkClickListener) {
        mOnBookmarkClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudyMainHolder {
        val binding =
            StudyItemOnRecruitingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudyMainHolder(binding)
    }

    override fun onBindViewHolder(holder: StudyMainHolder, position: Int) {
        val content = getItem(position)
        content?.let { holder.setPosts(it) }
    }


    inner class StudyMainHolder(val binding: StudyItemOnRecruitingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setPosts(studyItem: ContentXXXX?) {
            val postId: Int? = studyItem?.postId
            studyItem?.let {
                val functions = Functions()
                val koreanMajor = functions.convertToKoreanMajor(it.major)
                binding.txtCategory.text = koreanMajor
                binding.txtTitle.text = it.title

                val unAvailableSeat = it.studyPerson - it.remainingSeat
                binding.txtAvailable.text =
                    context.getString(R.string.txt_leftNumber, it.remainingSeat)
                binding.txtPeopleNumber.text =
                    context.getString(R.string.txt_people_number, unAvailableSeat, it.studyPerson)

                val sb = StringBuilder()
                sb.append(it.studyStartDate[1])
                    .append("월 ")
                    .append(it.studyStartDate[2])
                    .append("일 ~ ")
                    .append(it.studyEndDate[1])
                    .append("월 ")
                    .append(it.studyEndDate[2])
                    .append("일")
                binding.txtPeriod.text = sb.toString()

                if (it.penalty == 0) {
                    binding.txtFee.text = "없어요"
                } else {
                    sb.clear()
                    sb.append(it.penalty)
                        .append("원")
                    binding.txtFee.text = sb.toString()
                }
                binding.txtGender.text = it.filteredGender
                binding.txtNickname.text = it.userData.nickname

                sb.clear()
                sb.append(it.createdDate[0])
                    .append(".")
                    .append(it.createdDate[1])
                    .append(".")
                    .append(it.createdDate[2])

                binding.createdDate.text = sb.toString()
                Glide.with(context)
                    .load(it.userData.imageUrl)
                    .apply(
                        RequestOptions().override(
                            binding.iconProfile.width,
                            binding.iconProfile.height
                        )
                    ).into(binding.iconProfile)

                // 북마크인지 아닌지 표시
                binding.isBookmark = it.bookmarked
            }
            //북마크 추가
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

                mOnBookmarkClickListener.onItemClick(binding.btnBookmark.tag.toString(), postId)
                Log.i("북마크3 Listener눌림 : ", "")
            }

            itemView.setOnClickListener {
                mOnViewClickListener.onViewClick(postId)
            }
        }

    }
}

