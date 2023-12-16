package kr.co.gamja.study_hub.feature.toolbar.bookmark

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.model.ContentXX
import kr.co.gamja.study_hub.data.model.GetBookmarkResponse
import kr.co.gamja.study_hub.data.repository.OnItemsClickListener
import kr.co.gamja.study_hub.data.repository.OnViewClickListener
import kr.co.gamja.study_hub.databinding.BookmarkItemBinding
import kr.co.gamja.study_hub.global.Functions

class BookmarkAdapter(val context:Context) : PagingDataAdapter<ContentXX, BookmarkAdapter.BookmarkHolder>(DIFF_CALLBACK) {
    val whatItem = mapOf("detailStudyContent" to 1, "participating" to 2)
    private lateinit var mOnItemClickListener: OnItemClickListener // 북마크 클릭
    private lateinit var mOnItemsClickListener: OnItemsClickListener // 1: 뷰 클릭, 2. 신청하기

    companion object{
        private val DIFF_CALLBACK = object :DiffUtil.ItemCallback<ContentXX>(){
            override fun areItemsTheSame(oldItem: ContentXX, newItem: ContentXX): Boolean {
                return oldItem.postId == newItem.postId
            }

            override fun areContentsTheSame(oldItem: ContentXX, newItem: ContentXX): Boolean {
                return oldItem == newItem
            }
        }
    }
    // 스터디 자세히 보기 및 신청하기 Listener
    fun setOnItemsClickListener(listener: OnItemsClickListener) {
        mOnItemsClickListener = listener
    }
    /*// 리사이클러뷰 재활용 문제
    override fun getItemViewType(position: Int): Int {
        return position
    }*/
    // 북마크 이미지 클릭용
    fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener
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
        init{
            // 뷰클릭이벤트
            itemView.setOnClickListener{
                val itemPositon = bindingAdapterPosition
                if (itemPositon != RecyclerView.NO_POSITION){
                    val clickedItem = getItem(itemPositon)
                    clickedItem.let {
                        mOnItemsClickListener.getItemValue(whatItem["detailStudyContent"]!!, itemPositon)
                    }
                }
            }
        }
        fun setBookmarkList(bookmarkItem: ContentXX?) {
            val postId: Int? = bookmarkItem?.postId
            bookmarkItem?.let {
                val functions = Functions()
                val koreanMajor = functions.convertToKoreanMajor(it.major)
                binding.txtRelativeMajor.text = koreanMajor
                binding.txtTitle.text = it.title
                binding.txtSubTitle.text = it.content
                binding.txtAvailable.text = it.remainingSeat.toString()
                // 스터디 마감 여부 todo("테스트 해보기 ")
                if(it.close){
                    binding.btnJoin.setBackgroundColor(ContextCompat.getColor(context,R.color.BG_40))
                    binding.btnJoin.text=context.getString(R.string.btn_end)
                    binding.btnJoin.setTextColor(ContextCompat.getColor(context,R.color.BG_60))
                }else{
                    binding.btnJoin.setOnClickListener(object:View.OnClickListener{
                        // 신청하기 클릭시
                        override fun onClick(v: View?) {
                            mOnItemsClickListener.getItemValue(whatItem["participating"]!!, postId!!)
                        }
                    })
                }
                binding.btnBookmark.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(p0: View?) {
                        // 북마크 추가
                        if (binding.btnBookmark.tag.toString() == "0") {
                            binding.btnBookmark.tag = "1"
                            binding.btnBookmark.setBackgroundResource(R.drawable.baseline_bookmark_24_selected)
                            Log.d("북마크 그림 채워진걸로 바뀜", it.title)
                            val tagId = binding.btnBookmark.tag.toString()
                            mOnItemClickListener.onItemClick(tagId, postId)

                        } else { // 북마크 삭제
                            binding.btnBookmark.setTag("0")
                            binding.btnBookmark.setBackgroundResource(R.drawable.baseline_bookmark_border_24_unselected)
                            Log.d("북마크 그림 빈걸로 바뀜", it.title)
                            val tagId = binding.btnBookmark.tag.toString()
                            mOnItemClickListener.onItemClick(tagId, postId)
                        }
                    }
                })

            }
        }
    }
}


interface OnItemClickListener {
    fun onItemClick(tagId: String?, postId: Int? = 0)
}
