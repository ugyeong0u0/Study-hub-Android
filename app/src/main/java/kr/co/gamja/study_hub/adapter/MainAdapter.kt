package kr.co.gamja.study_hub.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.gamja.study_hub.databinding.HomeItemOnRecruitingBinding

/*
 fragment_main01 리사이클러뷰 어댑터
*/
class MainAdapter:RecyclerView.Adapter<MainAdapter.Main01Holder>() {
    // 임시 아이템
    val array= arrayOf("ddd")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Main01Holder{
        val binding = HomeItemOnRecruitingBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Main01Holder(binding)
    }

    override fun onBindViewHolder(holder: Main01Holder, position: Int) {
        val p= position
        return holder.set(p)
    }

    override fun getItemCount(): Int {
        return array.size // 빌드 못해서 임시로 넣어둠
    }
    class Main01Holder(val binding: HomeItemOnRecruitingBinding):RecyclerView.ViewHolder(binding.root){
        fun set(position:Int){

        }
    }
}
