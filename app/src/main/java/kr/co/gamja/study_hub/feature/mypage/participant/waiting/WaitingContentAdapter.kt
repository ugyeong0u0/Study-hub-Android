package kr.co.gamja.study_hub.feature.mypage.participant.waiting

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kr.co.gamja.study_hub.data.model.RegisterListContent
import kr.co.gamja.study_hub.databinding.WaitingItemBinding
import kr.co.gamja.study_hub.global.Functions

class WaitingContentAdapter(val context : Context) : RecyclerView.Adapter<WaitingContentAdapter.ContentHolder>() {

    private lateinit var onClickListener : OnClickListener

    private val itemList = mutableListOf<RegisterListContent>()

    fun submitList(itemList : List<RegisterListContent>){
        this.itemList.clear()
        this.itemList.addAll(itemList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentHolder {
        val binding = WaitingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContentHolder(binding)
    }

    override fun onBindViewHolder(holder: ContentHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = itemList.size

    fun setOnClickListener(listener: OnClickListener){
        this.onClickListener =  listener
    }

    interface OnClickListener {
        fun onAcceptClick(userId : Int)
        fun onRefusalClick(userId : Int)
    }

    inner class ContentHolder(val binding: WaitingItemBinding) : RecyclerView.ViewHolder(binding.root) {

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

                val date = "${item.createdDate[0]}년 ${item.createdDate[1]}월 ${item.createdDate[2]}일"
                createdDate.text = date
                userMajor.text = Functions().convertToKoreanMajor(item.major)
                userNickname.text = item.nickname

                application.text = item.introduce

                /** item.id가 참여 신청한 user의 id가 맞을까요? */

                btnRefusal.setOnClickListener{
                    onClickListener.onAcceptClick(item.id)
                }

                btnAccept.setOnClickListener{
                    onClickListener.onRefusalClick(item.id)
                }
            }
        }

    }
}