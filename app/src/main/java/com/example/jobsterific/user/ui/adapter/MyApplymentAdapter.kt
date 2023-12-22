package com.example.jobsterific.user.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsterific.data.response.ApplymentItems
import com.example.jobsterific.databinding.ItemBatchUserBinding

class MyApplymentAdapter(private val dataList: List<ApplymentItems?>?, var context : Context): ListAdapter<ApplymentItems,MyApplymentAdapter.MyViewHolder>(MyApplymentAdapter.DIFF_CALLBACK) {
    var onItemClick: ((ApplymentItems?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyApplymentAdapter.MyViewHolder {
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
        fun bind(review: ApplymentItems){
            binding.nameBatch
                .text = " ${review.batch?.campaignName}"
            binding.period
                .text = "${review.batch?.startDate} - ${review.batch?.endDate}"
            binding.description
                .text = " ${review.batch?.campaignDesc}"
            binding.job
                .text = " ${review.batch?.campaignKeyword}"


        }
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ApplymentItems>() {
            override fun areItemsTheSame(oldItem: ApplymentItems, newItem: ApplymentItems): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ApplymentItems, newItem: ApplymentItems): Boolean {
                return oldItem == newItem
            }
        }
    }
}


