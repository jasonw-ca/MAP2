package jwang.example.redoinfo6124project1

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import jwang.example.redoinfo6124project1.fragments.DashboardFragment
import jwang.example.redoinfo6124project1.fragments.GradesListFragment
import jwang.example.redoinfo6124project1.models.Grade
import jwang.example.redoinfo6124project1.models.GradeType
import java.io.*

var gradeList6124: ArrayList<Grade> = arrayListOf()
var gradeList6125: ArrayList<Grade> = arrayListOf()
var gradeList6126: ArrayList<Grade> = arrayListOf()
var gradeList6127: ArrayList<Grade> = arrayListOf()
var gradeList6128: ArrayList<Grade> = arrayListOf()
var gradeList6129: ArrayList<Grade> = arrayListOf()
var courseList = listOf("INFO6124","INFO6125","INFO6126","INFO6127","INFO6128","INFO6129")

class MainActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(
                R.id.fragment_placeholder,
                DashboardFragment.newInstance("ok")
            ).commit()
        }
    }

    override fun onStop() {
        courseList.forEach {
            when(it){
                "INFO6124" ->  writeFile("6124.json", gradeList6124)
                "INFO6125" ->  writeFile("6125.json", gradeList6125)
                "INFO6126" ->  writeFile("6126.json", gradeList6126)
                "INFO6127" ->  writeFile("6127.json", gradeList6127)
                "INFO6128" ->  writeFile("6128.json", gradeList6128)
                "INFO6129" ->  writeFile("6129.json", gradeList6129)
            }
        }

        super.onStop()
    }

    private fun writeFile(fileName: String, gradeList: ArrayList<Grade>) {
        val ofile: FileOutputStream = openFileOutput(fileName, MODE_PRIVATE)
        val osw = OutputStreamWriter(ofile)
        var jsonList = Gson().toJson(gradeList)
        for (grade in jsonList) {
            osw.write(grade.toString())
        }
        osw.flush()
        osw.close()
        Log.d("MyTag", "file written")
    }

    override fun onStart() {
        super.onStart()
        courseList.forEach {
            when(it){
                "INFO6124" ->  readFile("6124.json", gradeList6124)
                "INFO6125" ->  readFile("6125.json", gradeList6125)
                "INFO6126" ->  readFile("6126.json", gradeList6126)
                "INFO6127" ->  readFile("6127.json", gradeList6127)
                "INFO6128" ->  readFile("6128.json", gradeList6128)
                "INFO6129" ->  readFile("6129.json", gradeList6129)
            }
        }
    }

    private fun readFile(fileName: String, gradeList: ArrayList<Grade> /* = java.util.ArrayList<jwang.example.redoinfo6124project1.models.Grade> */) {
        try {

            val fin: FileInputStream = openFileInput(fileName)
            val isr = InputStreamReader(fin)
            val inputBuffer = CharArray(100)
            var str = ""
            var charRead: Int
            while (isr.read(inputBuffer).also { charRead = it } > 0) {
                val readString = String(inputBuffer, 0, charRead)
                str += readString
            }
            val listGradeType = object : TypeToken<ArrayList<Grade>>() {}.type
            val tempList : ArrayList<Grade> = Gson().fromJson(str, listGradeType)
            gradeList.clear()
            gradeList.addAll(tempList)
            Log.d("MyTag", "list: ${gradeList.size}")

        } catch (ioe: IOException) {
            Log.d("MyTag", "Read file fails")
            ioe.printStackTrace()
        }
    }
}