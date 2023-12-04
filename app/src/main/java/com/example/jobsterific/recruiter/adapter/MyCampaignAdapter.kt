
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsterific.R
import com.example.jobsterific.recruiter.CourseRVModal

class MyCampaignAdapter(
    private val courseList: ArrayList<CourseRVModal>,
    private val context: Context? = null
) : RecyclerView.Adapter<MyCampaignAdapter.MyCampaignViewHolder>() {


    var onItemClick: ((CourseRVModal) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyCampaignViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_my_campaign,
            parent, false
        )
        return MyCampaignViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyCampaignViewHolder, position: Int) {
        val currentCourse = courseList[position]
        holder.courseNameTV.text = currentCourse.courseName
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currentCourse)
        }
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    class MyCampaignViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseNameTV: TextView = itemView.findViewById(R.id.name)
        val icon:ImageView= itemView.findViewById(R.id.periodIcon)
    }
}
