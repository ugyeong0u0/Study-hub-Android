package kr.co.gamja.study_hub.feature.home.search

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kr.co.gamja.study_hub.R
import kr.co.gamja.study_hub.data.model.ContentXXXX
import kr.co.gamja.study_hub.data.model.FindStudyResponseM
import kr.co.gamja.study_hub.data.repository.OnBookmarkClickListener
import kr.co.gamja.study_hub.data.repository.OnViewClickListener
import kr.co.gamja.study_hub.databinding.HomeItemCloseDeadlineBinding
import kr.co.gamja.study_hub.databinding.SearchItemBinding
import kr.co.gamja.study_hub.feature.home.ItemCloseDeadlineAdapter

class SearchItemAdapter(private val context : Context, private val contentList : MutableList<ContentXXXX>) :
    RecyclerView.Adapter<SearchItemAdapter.SearchItemViewHolder>(){
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
            binding.item = item

            binding.duration = "${item.studyStartDate[1]}월 ${item.studyStartDate[2]}일 ~ ${item.studyEndDate[1]}월 ${item.studyEndDate[2]}일"

            binding.restMsg = "${item.remainingSeat}자리 남았습니다"

            binding.totalMem = "/${item.studyPerson}명"
            binding.remainMem = "${item.remainingSeat}"

            binding.payment = if (item.penalty == 0) "없어요" else "${item.penalty}원"

            binding.gender = if (item.filteredGender == "FEMAIL") "여자" else if (item.filteredGender == "MAIL") "남자" else "무관"
            binding.genderImg = if (item.filteredGender == "FEMAIL") R.drawable.icon_gender_girl_m else if (item.filteredGender == "MAIL") R.drawable.icon_gender_boy_m else R.drawable.icon_gender_gray

            Glide.with(context)
                .load(item.userData.imageUrl)
                .into(binding.userProfile)

            binding.nickname = item.userData.nickname

            binding.createDate = "${item.createdDate[0]}. ${item.createdDate[1]}. ${item.createdDate[2]}"
        }
    }
}