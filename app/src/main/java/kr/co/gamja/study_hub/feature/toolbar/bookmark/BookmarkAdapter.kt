package kr.co.gamja.study_hub.feature.toolbar.bookmark

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.model.ContentXX
import kr.co.gamja.study_hub.data.repository.OnBookmarkClickListener
import kr.co.gamja.study_hub.data.repository.OnPostingIdClickListener
import kr.co.gamja.study_hub.databinding.BookmarkItemBinding
import kr.co.gamja.study_hub.global.Functions

class BookmarkAdapter(val context: Context) :
    PagingDataAdapter<ContentXX, BookmarkAdapter.BookmarkHolder>(DIFF_CALLBACK) {
    val whatItem = mapOf("detailStudyContent" to 1, "participating" to 2)
    private lateinit var mOnBookmarkClickListener: OnBookmarkClickListener // 북마크 클릭
    private lateinit var mOnItemsClickListener: OnPostingIdClickListener // 1: 뷰 클릭, 2. 신청하기

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ContentXX>() {
            override fun areItemsTheSame(oldItem: ContentXX, newItem: ContentXX): Boolean {
                return oldItem.postId == newItem.postId
            }

            override fun areContentsTheSame(oldItem: ContentXX, newItem: ContentXX): Boolean {
                return oldItem == newItem
            }
        }
    }

    // 스터디 자세히 보기 및 신청하기 Listener
    fun setOnItemsClickListener(listener: OnPostingIdClickListener) {
        mOnItemsClickListener = listener
    }

    /*// 리사이클러뷰 재활용 문제
    override fun getItemViewType(position: Int): Int {
        return position
    }*/
    // 북마크 이미지 클릭용
    fun setOnBookmarkClickListener(listener: OnBookmarkClickListener) {
        mOnBookmarkClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkHolder {
        val binding =
            BookmarkItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookmarkHolder(binding)
    }

    override fun onBindViewHolder(holder: BookmarkHolder, position: Int) {
        val content = getItem(position)
        content?.let { holder.setBookmarkList(it) }
    }

    inner class BookmarkHolder(val binding: BookmarkItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setBookmarkList(bookmarkItem: ContentXX?) {
            bookmarkItem?.let {
                val postId: Int = it.postId
                val studyId: Int = it.studyId
                val functions = Functions()
                val koreanMajor = functions.convertToKoreanMajor(it.major)
                binding.txtRelativeMajor.text = koreanMajor
                binding.txtTitle.text = it.title
                binding.txtSubTitle.text = it.content
                binding.txtAvailable.text = it.remainingSeat.toString()
                // 스터디 마감 여부 todo("테스트 해보기 ")
                if (it.close) {
                    binding.btnJoin.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.BG_40
                        )
                    )
                    binding.btnJoin.text = context.getString(R.string.btn_end)
                    binding.btnJoin.setTextColor(ContextCompat.getColor(context, R.color.BG_60))
                } else {
                    binding.btnJoin.setOnClickListener {
                        // 신청하기 클릭시
                        mOnItemsClickListener.getItemValue(
                            whatItem["participating"]!!,
                            PostingId(postId, studyId)
                        )
                    }
                }

                binding.btnBookmark.setOnClickListener {
                    val isBookmarked = binding.btnBookmark.tag == "1"
                    binding.btnBookmark.tag = if (isBookmarked) "0" else "1"
                    val backgroundResource = if (!isBookmarked) {
                        R.drawable.baseline_bookmark_24_selected
                    } else {
                        R.drawable.baseline_bookmark_border_24_unselected
                    }
                    binding.btnBookmark.setBackgroundResource(backgroundResource)
                    mOnBookmarkClickListener.onItemClick(binding.btnBookmark.tag.toString(), postId)
                }
                // 뷰클릭이벤트
                itemView.setOnClickListener {
                    mOnItemsClickListener.getItemValue(
                        whatItem["detailStudyContent"]!!,
                        PostingId(postId, studyId)
                    )
                }
            }
        }
    }
}


