package kr.co.gamja.study_hub.feature.mypage.participant.refusal

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kr.co.gamja.study_hub.data.model.RegisterListContent
import kr.co.gamja.study_hub.databinding.RefusalItemBinding
import kr.co.gamja.study_hub.databinding.WaitingItemBinding
import kr.co.gamja.study_hub.global.Functions

class RefusalAdapter(val context : Context) : RecyclerView.Adapter<RefusalAdapter.ContentHolder>() {

    private val itemList = mutableListOf<RegisterListContent>()

    fun submitList(itemList : List<RegisterListContent>){
        this.itemList.clear()
        this.itemList.addAll(itemList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentHolder {
        val binding = RefusalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContentHolder(binding)
    }

    override fun onBindViewHolder(holder: ContentHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = itemList.size

    inner class ContentHolder(val binding: RefusalItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : RegisterListContent){

            binding.apply {

                Glide.with(context)
                    .load(item.imageUrl)
                    .apply(
                        RequestOptions().override(
                            binding.imgProfile.width,
                            binding.imgProfile.height
                        ).circleCrop()
                    ).into(binding.imgProfile)

                /** 여기도 마찬가지로 거절된 날짜를 보여 줘야 할 것 같은데 현재는 신청을 보낸 날짜로 되어 있습니다. */
                val date = "${item.createdDate[0]}년 ${item.createdDate[1]}월 ${item.createdDate[2]}일"
                createdDate.text = date
                userMajor.text = Functions().convertToKoreanMajor(item.major)
                userNickname.text = item.nickname
            }
        }
    }
}