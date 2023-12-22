package com.example.jobsterific.recruiter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsterific.data.response.RecommendationsItem
import com.example.jobsterific.databinding.ItemCandidatesCompanyBinding

class CandidatesAdapter(private val dataList: List<RecommendationsItem?>?, var context : Context): ListAdapter<RecommendationsItem, CandidatesAdapter.MyViewHolder>(CandidatesAdapter.DIFF_CALLBACK) {
    var onItemClick: ((RecommendationsItem?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidatesAdapter.MyViewHolder {
        val binding =  ItemCandidatesCompanyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val login = getItem(position)
        holder.bind(login)
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(login)
        }
    }
    class MyViewHolder(val binding: ItemCandidatesCompanyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: RecommendationsItem){
            binding.name
                .text = " ${review.firstName} ${review.lastName}"
            binding.job
                .text = "${review.job}"
            binding.email
                .text = " ${review.email}"
            binding.adress
                .text = " ${review.address}"


        }
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecommendationsItem>() {
            override fun areItemsTheSame(oldItem: RecommendationsItem, newItem: RecommendationsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: RecommendationsItem, newItem: RecommendationsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}



