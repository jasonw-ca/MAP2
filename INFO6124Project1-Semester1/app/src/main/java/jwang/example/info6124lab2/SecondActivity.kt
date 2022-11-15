package jwang.example.info6124lab2

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.FileNotFoundException
import java.lang.reflect.Array

class SecondActivity : AppCompatActivity() {





    lateinit var recyclerView: RecyclerView
    lateinit var recyclerViewManager: RecyclerView.LayoutManager
    var gradeRecordList = arrayListOf<GradeRecord>()
    lateinit var adapter: RecyclerAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        //gradeRecordList = ArrayList()


        try {
            val sharedPreferences = getSharedPreferences("grade records", MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString("record", "")
            val type = object : TypeToken<ArrayList<GradeRecord>>() {}.type
            gradeRecordList = gson.fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
        }






        recyclerView = findViewById(R.id.recyclerViewRecords)
        recyclerViewManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = recyclerViewManager
        recyclerView.setHasFixedSize(true)
        adapter = RecyclerAdapter(gradeRecordList)
        recyclerView.adapter = adapter




    }

    fun onButtonClick(view: View) {

        val sharedPreferences = getSharedPreferences("grade records", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json: String = gson.toJson(gradeRecordList)
        editor.putString("record", json)
        editor.apply()
        finish()



    }

    fun onDeleteAllClick(view: View) {
        gradeRecordList.clear()
//        adapter = RecyclerAdapter(gradeRecordList)
//        recyclerView.adapter = adapter
       adapter.notifyDataSetChanged()

    }


}