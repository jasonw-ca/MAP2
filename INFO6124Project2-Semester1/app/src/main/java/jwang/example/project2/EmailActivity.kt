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

class EmailActivity : AppCompatActivity() {

    private lateinit var addressString: String
    private lateinit var myCurrentAddresses: String
    val TAG = "myEmail"
    private lateinit var emailAddressEditText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)
        emailAddressEditText = findViewById(R.id.editTextEmailAddress)

        myCurrentAddresses = MapsActivity.myCurrentAddress
    }

    fun onButtonClick(v: View) {
        when(v.id){
            R.id.goSMS->{
                val intent = Intent(this, SMSActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.goMap->{
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.sendEmailButton->{


                val emailAddress = arrayOf(emailAddressEditText.text.toString())
                val emailSubjectEditText = findViewById<TextView>(R.id.editTextEmailSubject)
                val subjectString = emailSubjectEditText.text.toString()
                composeEmail(emailAddress, subjectString, myCurrentAddresses)
            }
        }
    }

    fun composeEmail(addresses: Array<String>, subject: String, body: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, addresses)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        startActivity(Intent.createChooser(intent, "Which app to send Email?"))
    }

    override fun onResume() {
        super.onResume()
        try {
            val prefsEditor = getSharedPreferences("myLastInput", Context.MODE_PRIVATE)
            emailAddressEditText.setText(prefsEditor.getString("emailAddress", ""))
            Log.d(TAG,"EmailAddress")
        } catch (e: Exception){
            Log.d(TAG,"sharedPref exception: ", e)
        }
    }

    override fun onPause() {
        super.onPause()
        val prefsEditor = getSharedPreferences("myLastInput", Context.MODE_PRIVATE).edit()
        prefsEditor.putString("emailAddress",emailAddressEditText.text.toString())
        prefsEditor.apply()
    }

}