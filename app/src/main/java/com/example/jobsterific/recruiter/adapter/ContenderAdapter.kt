import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jobsterific.data.response.GetAllUserResponseItem
import com.example.jobsterific.databinding.ItemContenderBinding

class ContenderAdapter(private val dataList: List<GetAllUserResponseItem>) :
    ListAdapter<GetAllUserResponseItem, ContenderAdapter.MyViewHolder>(DIFF_CALLBACK) {

    var onItemClick: ((GetAllUserResponseItem?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemContenderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(user)
        }
    }

    override fun getItemCount(): Int {
        return dataList.filter { it.isCustomer == false}.size
    }

    override fun getItem(position: Int): GetAllUserResponseItem {
        return dataList.filter { it.isCustomer == false }[position]
    }

    class MyViewHolder(val binding: ItemContenderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: GetAllUserResponseItem) {
            binding.name.text = "${user.firstName}"
            binding.job.text = "${user.job}"
            binding.email.text = "${user.email}"
            binding.adress.text = "${user.address}"
            Glide.with(binding.image)
                .load("${user.profile}")
                .into(binding.image)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GetAllUserResponseItem>() {
            override fun areItemsTheSame(oldItem: GetAllUserResponseItem, newItem: GetAllUserResponseItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: GetAllUserResponseItem, newItem: GetAllUserResponseItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
