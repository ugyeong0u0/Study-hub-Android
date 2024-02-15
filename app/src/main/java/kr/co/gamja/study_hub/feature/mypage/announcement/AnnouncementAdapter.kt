package kr.co.gamja.study_hub.feature.mypage.announcement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.gamja.study_hub.data.model.AnnouncementContentDto
import kr.co.gamja.study_hub.databinding.AnnouncementItemBinding

class AnnouncementAdapter : RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder>() {

    private val itemList = mutableListOf<AnnouncementContentDto>()

    //click을 확인하는 변수
    private var isClicked = false

    fun submitList(itemList : List<AnnouncementContentDto>){
        this.itemList.clear()
        this.itemList.addAll(itemList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        val binding = AnnouncementItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnnouncementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = itemList.size

    inner class AnnouncementViewHolder(val binding: AnnouncementItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : AnnouncementContentDto){

            binding.apply{

                title = item.title
                description = item.content

                /** date에 대한 값이 전해지는 것이 없음 */
                date = "${item.createdDate[0]}. ${item.createdDate[1]}. ${item.createdDate[2]}"
                
                //layout 클릭 시 확장
                lyItem.setOnClickListener{
                    tvDescription.visibility = if (isClicked) View.VISIBLE else View.GONE
                    isClicked = !isClicked
                }
            }
        }
    }
}