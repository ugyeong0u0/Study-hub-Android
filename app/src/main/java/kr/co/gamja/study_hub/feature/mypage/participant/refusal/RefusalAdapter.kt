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

    private var page = 0

    fun submitList(itemList : List<RegisterListContent>, page : Int){
        if (this.page < page){
            this.page += 1
        } else {
            this.itemList.clear()
        }
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

                val date = "${item.createdDate[0]}년 ${item.createdDate[1]}월 ${item.createdDate[2]}일"
                createdDate.text = date
                userMajor.text = Functions().convertToKoreanMajor(item.major)
                userNickname.text = item.nickname

                /** 여기는 거절 사유가 더 적절하지 않을까나 */
                reasonOfRefusal.text = item.introduce
            }
        }
    }
}