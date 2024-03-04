package kr.co.gamja.study_hub.feature.home.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.gamja.study_hub.databinding.SearchRecommendItemBinding

class RecommendItemAdapter : RecyclerView.Adapter<RecommendItemAdapter.RecommendViewHolder>(){

    private lateinit var listener : OnRecommendClick
    private val recommendList = mutableListOf<String>()

    fun setOnClickListener(listener : OnRecommendClick) {
        this.listener = listener
    }

    interface OnRecommendClick {
        fun onClick(value : String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendViewHolder {
        val binding = SearchRecommendItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendViewHolder, position: Int) {
        holder.bind(recommendList[position])
    }

    override fun getItemCount(): Int = recommendList.size

    fun submitList(list : List<String>) {
        recommendList.clear()
        recommendList.addAll(list)
        notifyDataSetChanged()
    }

    inner class RecommendViewHolder(
        val binding : SearchRecommendItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recommend : String) {
            binding.tvRecommend.text = recommend
            binding.lyRecommend.setOnClickListener{
                listener.onClick(recommend)
            }
        }

    }
}