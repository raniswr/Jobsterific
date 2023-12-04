
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsterific.R
import com.example.jobsterific.recruiter.CourseRVModal

class BatchAdapter(
    private val courseList: ArrayList<CourseRVModal>,
    private val context: Context? = null
) : RecyclerView.Adapter<BatchAdapter.BatchViewHolder>() {
    private var filteredCourseList = courseList.toList()

    var onItemClick: ((CourseRVModal) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BatchViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_batch_user,
            parent, false
        )
        return BatchViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BatchViewHolder, position: Int) {
        val currentCourse = filteredCourseList[position]
        Log.d("BatchAdapter", "Binding data: ${currentCourse.courseName}")
        holder.courseNameTV.text = currentCourse.courseName
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currentCourse)
        }
    }

    override fun getItemCount(): Int {
        return filteredCourseList.size
    }

    class BatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseNameTV: TextView = itemView.findViewById(R.id.nameBatch)
    }
    fun showAll() {
        filteredCourseList = courseList.toList()
        notifyDataSetChanged()

    }
    fun filter(query: String) {
        Log.d("BatchAdapter", "Before filtering: $courseList")
        filteredCourseList = courseList.filter { it.courseName.contains(query, ignoreCase = true) }
        notifyDataSetChanged()
        Log.d("BatchAdapter", "Filtering with query: $query, Result: $filteredCourseList")
    }

}
