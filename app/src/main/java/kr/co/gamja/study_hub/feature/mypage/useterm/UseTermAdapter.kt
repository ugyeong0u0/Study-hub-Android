package kr.co.gamja.study_hub.feature.mypage.useterm

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.gamja.study_hub.data.model.UseTermResponse
import kr.co.gamja.study_hub.data.model.UseTermResponseItem
import kr.co.gamja.study_hub.databinding.UsetermItemBinding

class UseTermAdapter(private val context: Context) :
    RecyclerView.Adapter<UseTermAdapter.UseTermHolder>() {
    val tag: String = this.javaClass.simpleName
    var useTerms: UseTermResponse? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UseTermHolder {
        val binding = UsetermItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UseTermHolder(binding)
    }

    override fun onBindViewHolder(holder: UseTermHolder, position: Int) {
        val termItem = useTerms?.get(position)
        holder.bind(termItem)
    }

    override fun getItemCount(): Int {
        return useTerms?.size ?: 0
    }

    inner class UseTermHolder(val binding: UsetermItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UseTermResponseItem?) {
            binding.article.text = item!!.article
            binding.content.text = item!!.content
        }
    }
}
