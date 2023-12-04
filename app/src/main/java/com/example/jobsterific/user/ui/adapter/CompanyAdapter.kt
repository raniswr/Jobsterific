package com.example.jobsterific.user.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsterific.R
import com.example.jobsterific.recruiter.CourseRVModal


class CompanyAdapter(
    // on below line we are passing variables
    // as course list and context
    private val courseList: ArrayList<CourseRVModal>,
    private val context: Context?=null
) : RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CompanyViewHolder {
        // this method is use to inflate the layout file
        // which we have created for our recycler view.
        // on below line we are inflating our layout file.
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_candidates_company,
            parent, false
        )
        // at last we are returning our view holder
        // class with our item View File.
        return CompanyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        // on below line we are setting data to our text view and our image view.
        holder.courseNameTV.text = courseList.get(position).courseName
//        holder.courseIV.setImageResource(courseList.get(position).courseImg)
    }

    override fun getItemCount(): Int {
        // on below line we are
        // returning our size of our list
        return courseList.size
    }

    class CompanyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // on below line we are initializing our course name text view and our image view.
        val courseNameTV: TextView = itemView.findViewById(R.id.companyName)

    }
}