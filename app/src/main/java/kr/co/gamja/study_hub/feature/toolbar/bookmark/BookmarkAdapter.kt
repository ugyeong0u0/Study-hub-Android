package kr.co.gamja.study_hub.feature.toolbar.bookmark

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.model.Content
import kr.co.gamja.study_hub.data.model.GetBookmarkResponse
import kr.co.gamja.study_hub.databinding.BookmarkItemBinding

class BookmarkAdapter : RecyclerView.Adapter<BookmarkAdapter.BookmarkHolder>() {
    var bookmarkList: GetBookmarkResponse? = null
    private lateinit var mOnItemClickListener: OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkHolder {
        val binding =
            BookmarkItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookmarkHolder(binding)
    }

    override fun onBindViewHolder(holder: BookmarkHolder, position: Int) {
        val bookmarkItem = bookmarkList?.content?.get(position)
        holder.setBookmarkList(bookmarkItem)
    }

    override fun getItemCount(): Int {
        return bookmarkList?.content?.size ?: 0
    }
    inner class BookmarkHolder(val binding: BookmarkItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setBookmarkList(bookmarkItem: Content?) {
            val postId: Int? = bookmarkItem?.postId
            bookmarkItem?.let {
                binding.txtRelativeMajor.text=it.major
                binding.txtTitle.text = it.title
                binding.txtSubTitle.text = it.content
                binding.txtAvailable.text = it.remainingSeat.toString()
                binding.btnBookmark.setOnClickListener(object : View.OnClickListener {
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


interface OnItemClickListener {
    fun onItemClick( tagId: String, postId: Int?=0)
}
