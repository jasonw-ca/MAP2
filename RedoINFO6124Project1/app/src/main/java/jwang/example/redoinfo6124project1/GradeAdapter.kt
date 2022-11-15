package jwang.example.redoinfo6124project1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jwang.example.redoinfo6124project1.models.Grade
import jwang.example.redoinfo6124project1.models.GradeType

class GradeAdapter(private val dataSet: ArrayList<Grade>): RecyclerView.Adapter<GradeAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val tvFullMark = view.findViewById<TextView>(R.id.full_mark_value)
        val tvMyMark = view.findViewById<TextView>(R.id.my_mark_value)
        val tvMarkWeight = view.findViewById<TextView>(R.id.mark_weight_value)
        val tvGradeType = view.findViewById<TextView>(R.id.tvGradeType)
        val imageView = view.findViewById<ImageView>(R.id.imageViewGradeType)
    }



    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvFullMark.text = dataSet[position].fullMark.toString()
        holder.tvMyMark.text = dataSet[position].myMark.toString()
        holder.tvMarkWeight.text = dataSet[position].weight.toString() + "%"
        holder.tvGradeType.text=dataSet[position].type.string
        holder.imageView.setImageResource(
            when(dataSet[position].type){
                GradeType.EXAM -> R.drawable.ic_exam
                GradeType.PROJECT -> R.drawable.ic_project
                GradeType.LAB -> R.drawable.ic_lab

            }
        )

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_item, parent, false )


        return ViewHolder(view)
    }


}
