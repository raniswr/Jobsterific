package com.example.jobsterific.recruiter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsterific.R
import com.example.jobsterific.recruiter.CourseRVModal

class ViewCandidateOfCampaignAdapter(
    private val courseList: ArrayList<CourseRVModal>,
    private val context: Context? = null
) : RecyclerView.Adapter<ViewCandidateOfCampaignAdapter.ViewCandidateOfCampaignViewHolder>() {

    var onItemClick: ((CourseRVModal) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewCandidateOfCampaignViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_view_candidate_of_campaign,
            parent, false
        )
        return ViewCandidateOfCampaignViewHolder(itemView)
    }

    override fun onBindViewHolder(holder:ViewCandidateOfCampaignViewHolder, position: Int) {
        val currentCourse = courseList[position]
        holder.courseNameTV.text = currentCourse.courseName
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currentCourse)
        }
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    class ViewCandidateOfCampaignViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseNameTV: TextView = itemView.findViewById(R.id.name)
    }
}
