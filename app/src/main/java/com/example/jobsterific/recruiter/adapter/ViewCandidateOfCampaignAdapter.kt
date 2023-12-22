package com.example.jobsterific.recruiter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsterific.data.response.ApplymentItem
import com.example.jobsterific.databinding.ItemViewCandidateOfCampaignBinding
class ViewCandidateOfCampaignAdapter(private val dataList: List<ApplymentItem?>?, var context : Context): ListAdapter<ApplymentItem, ViewCandidateOfCampaignAdapter.MyViewHolder>(ViewCandidateOfCampaignAdapter.DIFF_CALLBACK) {
    var onItemClick: ((ApplymentItem?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewCandidateOfCampaignAdapter.MyViewHolder {
        val binding = ItemViewCandidateOfCampaignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val login = getItem(position)
        holder.bind(login)
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(login)
        }
    }
    class MyViewHolder(val binding: ItemViewCandidateOfCampaignBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: ApplymentItem){
            binding.name
                .text = " ${review.user?.firstName} ${review.user?.lastName}"
            binding.job
                .text = "${review.user?.job}"
            binding.email
                .text = " ${review.user?.email}"
            binding.adress
                .text = " ${review.user?.address}"


        }
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ApplymentItem>() {
            override fun areItemsTheSame(oldItem: ApplymentItem, newItem: ApplymentItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ApplymentItem, newItem: ApplymentItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}



