package kr.co.gamja.study_hub.feature.home.search

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.model.ContentXXXX
import kr.co.gamja.study_hub.databinding.SearchItemBinding
import kr.co.gamja.study_hub.global.Functions

class SearchItemAdapter(private val context : Context) :
    RecyclerView.Adapter<SearchItemAdapter.SearchItemViewHolder>(){

    private lateinit var listener : OnViewClickListener

    private var contentList : MutableList<ContentXXXX> = mutableListOf()

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun updateList(itemList : List<ContentXXXX>) {
        contentList.clear()
        contentList.addAll(itemList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchItemAdapter.SearchItemViewHolder {
        val binding = SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SearchItemViewHolder,
        position: Int
    ) {
        Log.d("Adapter","itemlist : ${contentList}")
        holder.bind(contentList[position])
    }

    override fun getItemCount(): Int = contentList.size

    inner class SearchItemViewHolder(val binding: SearchItemBinding) :

        RecyclerView.ViewHolder(binding.root) {

        fun bind(item : ContentXXXX){

            val function = Functions()

            binding.lyItem.setOnClickListener {
                listener.onClick(item.postId)
            }

            binding.item = item

            binding.duration = "${item.studyStartDate[1]}월 ${item.studyStartDate[2]}일 ~ ${item.studyEndDate[1]}월 ${item.studyEndDate[2]}일"

            binding.restMsg = "${item.remainingSeat}자리 남았습니다"

            binding.major = function.convertToKoreanMajor(item.major)

            binding.totalMem = "/${item.studyPerson}명"
            binding.remainMem = "${item.studyPerson - item.remainingSeat}"

            binding.payment = if (item.penalty == 0) "없어요" else "${item.penalty}원"

            binding.gender = function.convertToKoreanGender(item.filteredGender)

            if (item.filteredGender == "FEMALE"){
                binding.imgGender.setImageResource(R.drawable.icon_gender_girl_m)
            } else if (item.filteredGender == "MALE") {
                binding.imgGender.setImageResource(R.drawable.icon_gender_boy_m)
            } else {
                binding.imgGender.setImageResource(R.drawable.resized_icon_gender_gray_24)
            }

            Glide.with(context)
                .load(item.userData.imageUrl)
                .into(binding.userProfile)

            binding.nickname = item.userData.nickname

            binding.createDate = "${item.createdDate[0]}. ${item.createdDate[1]}. ${item.createdDate[2]}"
        }
    }

    interface OnViewClickListener{
        fun onClick(action : Int)
    }

    fun setOnClickListener(onClickListener : OnViewClickListener){
        this.listener = onClickListener
    }
}