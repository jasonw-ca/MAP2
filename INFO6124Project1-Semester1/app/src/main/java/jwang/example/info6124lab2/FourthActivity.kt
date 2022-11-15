package jwang.example.info6124lab2

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FourthActivity : AppCompatActivity() {

    private lateinit var gradeRecordList: ArrayList<GradeRecord>
    private lateinit var summaryList: ArrayList<Summary>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fourth)
        gradeRecordList = ArrayList()

        try {

            val sharedPreferences: SharedPreferences =
                getSharedPreferences("grade records", MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString("record", "")
            val type = object : TypeToken<ArrayList<GradeRecord>>() {}.type

            gradeRecordList = gson.fromJson(json, type)
        } catch (e:Exception) {
            e.printStackTrace()
        }
        summaryList = ArrayList()
        var adapter: MyAdapter? = null


       val courseList = arrayOf("INFO6124", "INFO6125", "INFO6126","INFO6127","INFO6128","INFO6129")
        for (course in courseList) {
            var gradeSubmitted: Double = 0.0
            var fullGradeToDate: Double = 0.0
            var rcvGradeToDate: Double = 0.0
            var gradeToDate = 0.0
            gradeRecordList.forEach {
                if (it.courseName == course) {

                    gradeSubmitted += it.percentage!!.toInt()

                    fullGradeToDate += it.fullGrade!!.toInt()
                    rcvGradeToDate += it.receivedGrade!!.toInt()

                }
            }
            if (fullGradeToDate != 0.0) {
                gradeToDate = Math.ceil((rcvGradeToDate / fullGradeToDate) * 100)
            }


            var summary = Summary(course, gradeSubmitted.toInt(), gradeToDate.toInt())


            summaryList.add(summary)




        }
        Log.d("summary", summaryList[0].toString())


        val listview: ListView = findViewById(R.id.listview)

        adapter = MyAdapter(this, summaryList)
        listview.setAdapter(adapter)

    }

    fun onButtonClick(view: View) {
        finish()

    }
}

class MyAdapter(private val context: Context, private val arrayList: java.util.ArrayList<Summary>) : BaseAdapter() {
    private lateinit var courseName: TextView
    private lateinit var totalGradeSubmitted: TextView
    private lateinit var gradeToDate: TextView
    override fun getCount(): Int {
        return arrayList.size
    }
    override fun getItem(position: Int): Any {
        return position
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        convertView = LayoutInflater.from(context).inflate(R.layout.listview_item, parent, false)
        courseName = convertView.findViewById(R.id.courseName)
        totalGradeSubmitted = convertView.findViewById(R.id.totalGradeSubmitted)
        gradeToDate = convertView.findViewById(R.id.gradeToDate)
        courseName.text = arrayList[position].courseName
       totalGradeSubmitted.text = arrayList[position].totalGradeSubmitted.toString()
     gradeToDate.text = arrayList[position].gradeToDate.toString()
        return convertView
    }
}

class Summary (var courseName: String, var totalGradeSubmitted: Int, var gradeToDate: Int)