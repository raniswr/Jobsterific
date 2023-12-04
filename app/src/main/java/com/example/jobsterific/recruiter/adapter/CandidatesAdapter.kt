package com.example.jobsterific.recruiter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsterific.R
import com.example.jobsterific.recruiter.CourseRVModal

class CandidatesAdapter(
    private val courseList: ArrayList<CourseRVModal>,
    private val context: Context? = null
) : RecyclerView.Adapter<CandidatesAdapter.CandidatesViewHolder>() {
    var onItemClick: ((CourseRVModal) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CandidatesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_candidates_company,
            parent, false
        )
        return CandidatesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CandidatesViewHolder, position: Int) {
        val currentCourse = courseList[position]
        holder.courseNameTV.text = currentCourse.courseName
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currentCourse)
        }
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    class CandidatesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseNameTV: TextView = itemView.findViewById(R.id.name)
    }
}
