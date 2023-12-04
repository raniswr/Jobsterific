
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.jobsterific.R

class ViewPagerAdapter(val context: Context, val imageList: List<Int>) : PagerAdapter() {
    override fun getCount(): Int {
        return imageList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as RelativeLayout
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView: View = mLayoutInflater.inflate(R.layout.item_slider, container, false)
        val imageView: ImageView = itemView.findViewById<View>(R.id.idIVImage) as ImageView

        // Load image from the internet using Glide
        Glide.with(context)
            .load(imageList[position])
            .placeholder(R.drawable.image2) // You can set a placeholder image
            .error(R.drawable.image2) // You can set an error image
            .into(imageView)

        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }
}
