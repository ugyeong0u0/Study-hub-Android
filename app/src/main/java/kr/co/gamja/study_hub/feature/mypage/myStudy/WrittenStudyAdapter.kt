package kr.co.gamja.study_hub.feature.mypage.myStudy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.model.ContentXX
import kr.co.gamja.study_hub.data.repository.OnPostingIdClickListener
import kr.co.gamja.study_hub.databinding.WrittenstudyItemBinding
import kr.co.gamja.study_hub.feature.toolbar.bookmark.PostingId
import kr.co.gamja.study_hub.global.Functions
// todo("인터페이스 클래스담기는것으로 변경하기 ")
class WrittenStudyAdapter(private val context: Context) :
    PagingDataAdapter<ContentXX, WrittenStudyAdapter.WrittenStudyHolder>(DIFF_CALLBACK) {
    private lateinit var mOnItemsClickListener: OnPostingIdClickListener// item 내부요소 구별 클릭리스너
    val whatItem = mapOf("shutdownStudy" to 1, "goParticipant" to 2, "goStudyPage" to 3)
    fun setOnItemClickListener(listener: OnPostingIdClickListener) {
        mOnItemsClickListener = listener
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ContentXX>() {
            // 리스트 아이템이 같은지
            override fun areItemsTheSame(oldItem: ContentXX, newItem: ContentXX): Boolean {
                return oldItem.postId == newItem.postId
            }

            // 리스트 아이템 안 객체의 내용이 같은지 - 내용 변경 확인
            override fun areContentsTheSame(oldItem: ContentXX, newItem: ContentXX): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WrittenStudyHolder {
        val binding =
            WrittenstudyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WrittenStudyHolder(binding)
    }

    override fun onBindViewHolder(holder: WrittenStudyHolder, position: Int) {
        val content = getItem(position)
        content?.let { holder.bind(it) }
    }

    inner class WrittenStudyHolder(val binding: WrittenstudyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(content: ContentXX) {
            val functions = Functions()
            val postId: Int = content.postId
            val studyId= content.studyId
            binding.txtMajor.text = functions.convertToKoreanMajor(content.major)
            binding.txtTitle.text = content.title
            binding.txtSub.text = content.content
            binding.txtRemainingSeats.text =
                context.getString(R.string.txt_RemainingSeats, content.remainingSeat)
            // 마감 색 여부
            binding.isCloseStudy=content.close

            binding.btnSetEnd.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    // 마감 클릭시
                    mOnItemsClickListener.getItemValue(whatItem["shutdownStudy"]!!, PostingId(postId, studyId))
                }
            })
            binding.btnParticipant.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    // 참여자 클릭시
                    mOnItemsClickListener.getItemValue(whatItem["goParticipant"]!!, PostingId(postId, studyId))
                }
            })
            binding.btnThreeDot.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    // 스터디 수정 클릭시
                    mOnItemsClickListener.getItemValue(whatItem["goStudyPage"]!!, PostingId(postId, studyId))
                }
            })
        }
    }
}