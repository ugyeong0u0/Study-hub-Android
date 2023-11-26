package kr.co.gamja.study_hub.feature.studypage.studyHome

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.model.ContentX
import kr.co.gamja.study_hub.data.model.FindStudyResponse
import kr.co.gamja.study_hub.data.repository.OnViewClickListener
import kr.co.gamja.study_hub.databinding.StudyItemOnRecruitingBinding
import kr.co.gamja.study_hub.feature.toolbar.bookmark.OnItemClickListener
import kr.co.gamja.study_hub.global.Functions

class StudyMainAdapter(private val context: Context) :
    RecyclerView.Adapter<StudyMainAdapter.StudyMainHolder>() {
    var studyPosts: FindStudyResponse? = null
    private lateinit var mOnItemClickListener: OnItemClickListener // 북마크 viewModel에 interface 선언
    private lateinit var mOnViewClickListener: OnViewClickListener // 뷰 자체 클릭
    fun setViewClickListener(listener: OnViewClickListener) {
        mOnViewClickListener = listener
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudyMainHolder {
        val binding =
            StudyItemOnRecruitingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudyMainHolder(binding)
    }

    override fun onBindViewHolder(holder: StudyMainHolder, position: Int) {
        val studyPostsItem = studyPosts?.content?.get(position)
        holder.setPosts(studyPostsItem)
    }

    override fun getItemCount(): Int {
        return studyPosts?.content?.size ?: 0
    }

    inner class StudyMainHolder(val binding: StudyItemOnRecruitingBinding) :
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
                val koreanMajor = functions.convertToKoreanMajor(it.major)
                binding.txtCategory.text = koreanMajor
                binding.txtTitle.text = it.title
//                binding.txtAvailable.text=context.getString(R.string.txt_leftNumber,it.leftover)
                //TODO("남은 인원")
                val startDate =
                    it.studyStartDate[1].toString() + "월 " + it.studyStartDate[2] + "일 ~ "
                val endDate = it.studyEndDate[1].toString() + "월 " + it.studyEndDate[2] + "일"
                binding.txtPeriod.text = startDate + endDate
//                val available=it.studyPerson-it.leftover
//                binding.txtPeopleNumber.text=context.getString(R.string.txt_people_number,available,it.studyPerson)
                if (it.penalty == 0) {
                    binding.txtFee.text = "없어요"
                } else {
                    binding.txtFee.text = it.penalty.toString() + "원"
                }
                binding.txtGender.text = it.filteredGender
                binding.txtNickname.text = it.userData.nickname
                val createDate =
                    it.createdDate[0].toString() + "." + it.createdDate[1] + "." + it.createdDate[2]
                binding.createdDate.text = createDate
                //TODO("작성자사진")


                //북마크 추가
                binding.btnBookmark.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(p0: View?) {
                        // 북마크 추가
                        if (binding.btnBookmark.tag.toString() == "0") {
                            binding.btnBookmark.tag = "1"
                            Log.e("북마크 안된거 되게", binding.btnBookmark.tag.toString())
                            binding.btnBookmark.setBackgroundResource(R.drawable.baseline_bookmark_24_selected)
                            val tagId = binding.btnBookmark.tag.toString()
                            mOnItemClickListener.onItemClick(tagId, postId)

                        } else { // 북마크 삭제
                            binding.btnBookmark.setTag("0")
                            Log.e("북마크 안된거 되게", binding.btnBookmark.tag.toString())
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

