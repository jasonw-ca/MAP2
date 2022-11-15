package jwang.example.info6124lab2

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView



class RecyclerAdapter(private val dataSet: ArrayList<GradeRecord>):
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewCourseName: TextView
        val textViewGradeType: TextView
        val textViewFullGrade: TextView
        val textViewRcvGrade: TextView
        val textViewPercentage: TextView
        val cardView: LinearLayout
        val deleteButton: Button

        init {
            textViewCourseName = view.findViewById(R.id.courseNameRvTxt)
            textViewGradeType = view.findViewById(R.id.gradeTypeRvTxt)
            textViewFullGrade = view.findViewById(R.id.fullGradeRvTxt)
            textViewRcvGrade = view.findViewById(R.id.rcvGradeRvTxt)
            textViewPercentage = view.findViewById(R.id.percentageRvTxt)
            cardView = view.findViewById(R.id.cardView)
            deleteButton = view.findViewById(R.id.deleteButton)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false )
        val lp = view.getLayoutParams()
        lp.height = 310

        view.setLayoutParams(lp)


        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.textViewCourseName.text = App.context?.resources?.getString(R.string.course_name) + ":" + " " + dataSet[position].courseName
        holder.textViewGradeType.text = App.context?.resources?.getString(R.string.grade_type) + ":" + " " + dataSet[position].gradeType
        holder.textViewFullGrade.text = App.context?.resources?.getString(R.string.full_grade) + ":" + " " + dataSet[position].fullGrade
        holder.textViewRcvGrade.text = App.context?.resources?.getString(R.string.received_grade) + ":" + " " + dataSet[position].receivedGrade
        holder.textViewPercentage.text = App.context?.resources?.getString(R.string.weight_of_total_grade) + ":" + " " + dataSet[position].percentage + "%"
        holder.deleteButton.setOnClickListener{
            dataSet.removeAt(position)
            notifyDataSetChanged()

        }

        when(dataSet[position].courseName) {

            "INFO6124" -> {holder.cardView.setBackgroundColor(Color.GREEN)}
            "INFO6125" -> {holder.cardView.setBackgroundColor(Color.RED)}
            "INFO6126" -> {holder.cardView.setBackgroundColor(Color.rgb(33,135,245))}
            "INFO6127" -> {holder.cardView.setBackgroundColor(Color.GRAY)}
            "INFO6128" -> {holder.cardView.setBackgroundColor(Color.MAGENTA)}
            "INFO6129" -> {holder.cardView.setBackgroundColor(Color.BLACK)}
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }


}