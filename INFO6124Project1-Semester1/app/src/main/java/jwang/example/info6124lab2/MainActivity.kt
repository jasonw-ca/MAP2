package jwang.example.info6124lab2

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken



class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    lateinit var spinner: Spinner
    lateinit var spinnerValue: String
    lateinit var radioGroup: RadioGroup
    lateinit var fullGradeText: EditText
    lateinit var rcvGradeText: EditText
    lateinit var percText: EditText
    lateinit var topText: TextView
    private lateinit var gradeRecordList: MutableList<GradeRecord>




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gradeRecordList = ArrayList<GradeRecord>()







        spinner = findViewById(R.id.spinner)
        topText = findViewById(R.id.textView)

        radioGroup = findViewById(R.id.radioGroup)
        radioGroup.check(R.id.rButtonLab)
        fullGradeText = findViewById((R.id.fullGradeEditText))
        rcvGradeText = findViewById(R.id.rcvGradeEditText)
        percText = findViewById(R.id.percEditText)
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.courses,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this



    }

    override fun onResume() {
        super.onResume()
        fullGradeText.setText("")
        rcvGradeText.setText("")
        percText.setText("")

        try {


        val sharedPreferences: SharedPreferences = getSharedPreferences("grade records", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("record", "")
        val type = object : TypeToken<ArrayList<GradeRecord>>() {}.type
        gradeRecordList = gson.fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }





    fun onButtonClick(view: View) {

        when(view.id) {
            R.id.summaryButton -> {
                val intent = Intent(this, FourthActivity::class.java)

                startActivity(intent)
            }

            R.id.seeRecordsButton -> {
                val intent = Intent(this, SecondActivity::class.java)

                startActivity(intent)
            }

            R.id.recordButton -> {

//Collect input from UI
                val selectedRdId: Int = radioGroup.checkedRadioButtonId
                val rdValue = findViewById<RadioButton>(selectedRdId).text.toString()
                val fullGrade = fullGradeText.text.toString()
                val rcvGrade = rcvGradeText.text.toString()
                val percentage = percText.text.toString()




                if (fullGrade == ""||rcvGrade ==""||percentage=="") {
                    Toast.makeText(applicationContext, getString(R.string.empty_field),Toast.LENGTH_SHORT).show()

                } else if (fullGrade.toInt() > 100){
                    Toast.makeText(applicationContext, getString(R.string.full_over_100),Toast.LENGTH_SHORT).show()
                }else if (rcvGrade.toInt() > fullGrade.toInt()) {
                    Toast.makeText(applicationContext, getString(R.string.rcv_gt_full),Toast.LENGTH_SHORT).show()
                } else if (percentage.toInt() > 100) {
                    Toast.makeText(applicationContext, getString(R.string.perc_gt_100),Toast.LENGTH_SHORT).show()
                } else {
//Build a data object with the collected data
                var gr = GradeRecord(
                    spinnerValue,
                    rdValue,
                    fullGrade,
                    rcvGrade,
                    percentage
                )
//Append the data object to the list
                gradeRecordList.add(gr)


//Write the list into shared pref
                val sharedPreferences = getSharedPreferences("grade records", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                val gson = Gson()
                val json: String = gson.toJson(gradeRecordList)
                editor.putString("record", json)
                editor.apply()
//Toast a message to let the user know
                Toast.makeText(this, "Grade record saved.", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, SecondActivity::class.java)
                startActivity(intent)
            }
            }

            R.id.settingsButton -> {
                val intent = Intent(this, ThirdActivity::class.java)

                startActivity(intent)
            }
       }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
          spinnerValue = p0?.getItemAtPosition(p2).toString()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}




}