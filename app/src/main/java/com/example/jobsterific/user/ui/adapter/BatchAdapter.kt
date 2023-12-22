
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsterific.data.response.BatchesItem
import com.example.jobsterific.databinding.ItemBatchUserBinding


class BatchAdapter(private val dataList: List<BatchesItem?>?, var context : Context): ListAdapter<BatchesItem,BatchAdapter.MyViewHolder>(BatchAdapter.DIFF_CALLBACK) {
    var onItemClick: ((BatchesItem?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BatchAdapter.MyViewHolder {
        val binding = ItemBatchUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val login = getItem(position)
        holder.bind(login)
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(login)
        }
    }
    class MyViewHolder(val binding: ItemBatchUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: BatchesItem){
            binding.nameBatch
                .text = " ${review.campaignName}"
            binding.period
                .text = "${review.startDate} - ${review.endDate}"
            binding.description
                .text = " ${review.campaignDesc}"
            binding.job
                .text = " ${review.campaignKeyword}"


        }
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BatchesItem>() {
            override fun areItemsTheSame(oldItem: BatchesItem, newItem:BatchesItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem:BatchesItem, newItem: BatchesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}


