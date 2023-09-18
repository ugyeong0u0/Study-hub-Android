package kr.co.gamja.study_hub.feature.toolbar.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.gamja.study_hub.data.model.BookmarkResponse
import kr.co.gamja.study_hub.data.model.BookmarkResponseItem
import kr.co.gamja.study_hub.databinding.BookmarkItemBinding

class BookmarkAdapter : RecyclerView.Adapter<BookmarkHolder>() {
    var bookmarkList: BookmarkResponse? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkHolder {
        val binding =
            BookmarkItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookmarkHolder(binding)
    }

    override fun onBindViewHolder(holder: BookmarkHolder, position: Int) {
        val bookmarkItem = bookmarkList?.get(position)
        holder.setBookmarkList(bookmarkItem)
    }

    override fun getItemCount(): Int {
        return bookmarkList?.size ?: 0
    }
}

class BookmarkHolder(val binding: BookmarkItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun setBookmarkList(bookmarkItem: BookmarkResponseItem?) {
        bookmarkItem?.let {
            binding.txtTitle.text = it.title
            binding.txtSubTitle.text = it.content
            binding.txtAvailable.text = it.leftover.toString()
        }
    }
}