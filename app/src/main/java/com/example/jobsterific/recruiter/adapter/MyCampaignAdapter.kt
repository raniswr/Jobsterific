
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsterific.data.response.BatchItem
import com.example.jobsterific.databinding.ItemBatchUserBinding

class MyCampaignAdapter(private val dataList: List<BatchItem?>?, var context : Context): ListAdapter<BatchItem, MyCampaignAdapter.MyViewHolder>(MyCampaignAdapter.DIFF_CALLBACK) {
    var onItemClick: ((BatchItem?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCampaignAdapter.MyViewHolder {
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
        fun bind(review: BatchItem){
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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BatchItem>() {
            override fun areItemsTheSame(oldItem: BatchItem, newItem: BatchItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: BatchItem, newItem: BatchItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}


