package kr.co.gamja.study_hub.feature.studypage.studyHome

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.model.ContentX
import kr.co.gamja.study_hub.data.model.FindStudyResponse
import kr.co.gamja.study_hub.databinding.StudyItemOnRecruitingBinding
import kr.co.gamja.study_hub.feature.toolbar.bookmark.OnItemClickListener

class StudyMainAdapter(private val context:Context) : RecyclerView.Adapter<StudyMainAdapter.StudyMainHolder>() {
    var studyPosts: FindStudyResponse? = null
    private lateinit var mOnItemClickListener: OnItemClickListener // 북마크 viewModel에 interface 선언
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
        fun setPosts(studyItem: ContentX?) {
            val postId:Int? = studyItem?.postId
            studyItem?.let {
                binding.txtCategory.text=it.major
                binding.txtTitle.text=it.title
                binding.txtAvailable.text=context.getString(R.string.txt_left_number,it.leftover)
                //TODO("기간")
                val available=it.studyPerson-it.leftover
                binding.txtPeopleNumber.text=context.getString(R.string.txt_people_number,available,it.studyPerson)
                //TODO("요금")
                //TODO("작성자, 생성날짜,사진")

                //북마크 추가
                binding.btnBookmark.setOnClickListener (object : View.OnClickListener {
                    override fun onClick(p0: View?) {
                        // 북마크 추가
                        if (binding.btnBookmark.tag.toString() == "0") {
                            binding.btnBookmark.tag = "1"
                            binding.btnBookmark.setBackgroundResource(R.drawable.baseline_bookmark_24_selected)
                            val tagId =binding.btnBookmark.tag.toString()
                            mOnItemClickListener.onItemClick(tagId,postId)

                        } else { // 북마크 삭제
                            binding.btnBookmark.setTag("0")
                            binding.btnBookmark.setBackgroundResource(R.drawable.baseline_bookmark_border_24_unselected)
                            val tagId =binding.btnBookmark.tag.toString()
                            mOnItemClickListener.onItemClick(tagId,postId)
                        }
                    }
                })
            }
        }
    }
}

