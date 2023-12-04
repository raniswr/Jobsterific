package com.example.jobsterific.recruiter.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsterific.R
import com.example.jobsterific.recruiter.CourseRVModal

class ContenderAdapter(
    private val courseList: ArrayList<CourseRVModal>,
    private val context: Context? = null
) : RecyclerView.Adapter<ContenderAdapter.ContenderViewHolder>() {

    var onItemClick: ((CourseRVModal) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContenderViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_contender,
            parent, false
        )
        return ContenderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ContenderViewHolder, position: Int) {
        val currentCourse = courseList[position]
        holder.courseNameTV.text = currentCourse.courseName
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currentCourse)
        }
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    class ContenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseNameTV: TextView = itemView.findViewById(R.id.name)
    }
}
