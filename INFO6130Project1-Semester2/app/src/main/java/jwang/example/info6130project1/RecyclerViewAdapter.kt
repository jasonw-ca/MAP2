package jwang.example.info6130project1

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class RecyclerViewAdapter(var dataSet: ArrayList<Photo>): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var gridImageView: ImageView = itemView.findViewById(R.id.imageViewRV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(dataSet[position].img_src).into(holder.gridImageView)
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, ImageActivity::class.java)
            intent.putExtra(CAMERA, dataSet[position].camera.name)
            intent.putExtra(EARTH_DATE, dataSet[position].earth_date)
            intent.putExtra(ROVER_NAME, dataSet[position].rover.name)
            intent.putExtra(IMAGE_URL, dataSet[position].img_src)
            (it.context as Activity).startActivity(intent)
        }
    }

    override fun getItemCount(): Int {

        return dataSet.size

    }
}