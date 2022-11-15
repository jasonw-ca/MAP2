package jwang.example.project2

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView

class SMSActivity : AppCompatActivity() {

    private lateinit var addressString: String
    private lateinit var myCurrentAddresses: String
    val TAG = "mySMS"
    lateinit var  smsNumberEditText: EditText
    var phoneNumber = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smsactivity)

        myCurrentAddresses = MapsActivity.myCurrentAddress
        smsNumberEditText = findViewById(R.id.editTextPhoneNumber)

    }

    fun onButtonClick(v: View) {
        when(v.id){
            R.id.goEmail->{
                val intent = Intent(this, EmailActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.goMap->{
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.sendSMSButton->{
                Log.d(TAG,myCurrentAddresses)
                phoneNumber = smsNumberEditText.text.toString()
                composeSMS(phoneNumber, myCurrentAddresses)
            }
        }
    }

    private fun composeSMS(number: String, body: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:")
            putExtra("address", number)
            putExtra("sms_body", body)
        }
        startActivity(Intent.createChooser(intent, "Which app to send Email?"))
    }

    override fun onResume() {
        super.onResume()
        try {
            val prefsEditor = getSharedPreferences("myLastInput", Context.MODE_PRIVATE)
            smsNumberEditText.setText(prefsEditor.getString("phoneNumber", ""))
            Log.d(TAG,"EmailAddress")
        } catch (e: Exception){
            Log.d(TAG,"sharedPref exception: ", e)
        }
    }

    override fun onPause() {
        super.onPause()
        val prefsEditor = getSharedPreferences("myLastInput", Context.MODE_PRIVATE).edit()
        prefsEditor.putString("phoneNumber",smsNumberEditText.text.toString())
        prefsEditor.apply()
    }
}