package kr.co.gamja.study_hub.feature.mypage.participant.participation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kr.co.gamja.study_hub.data.model.RegisterListContent
import kr.co.gamja.study_hub.databinding.ParticipationItemBinding
import kr.co.gamja.study_hub.global.Functions

class ParticipationAdapter(val context : Context) : RecyclerView.Adapter<ParticipationAdapter.ContentHolder>() {

    private val itemList = mutableListOf<RegisterListContent>()

    fun submitList(itemList : List<RegisterListContent>, page : Int){
        this.itemList.clear()
        this.itemList.addAll(itemList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentHolder {
        val binding = ParticipationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContentHolder(binding)
    }

    override fun onBindViewHolder(holder: ContentHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = itemList.size

    inner class ContentHolder(val binding: ParticipationItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : RegisterListContent){

            binding.apply{

                Glide.with(context)
                    .load(item.imageUrl)
                    .apply(
                        RequestOptions().override(
                            binding.imgProfile.width,
                            binding.imgProfile.height
                        ).circleCrop()
                    ).into(binding.imgProfile)

                /** study에 참여한 날짜로 해야 하지만 받을 수 있는 수단이 있을까요? 현재는 신청한 날짜로 했습니다 */
                val date = "${item.createdDate[0]}년 ${item.createdDate[1]}월 ${item.createdDate[2]}일"
                createdDate.text = date
                userMajor.text = Functions().convertToKoreanMajor(item.major)
                userNickname.text = item.nickname
            }
        }
    }
}