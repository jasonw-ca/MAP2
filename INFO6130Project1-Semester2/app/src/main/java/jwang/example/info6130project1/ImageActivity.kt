package jwang.example.info6130project1

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class ImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        //actionBar?.setDisplayHomeAsUpEnabled(true)
        val camera = intent.getStringExtra(CAMERA)
        val earthDate = intent.getStringExtra(EARTH_DATE)
        val roverName = intent.getStringExtra(ROVER_NAME)
        val imgUrl = intent.getStringExtra(IMAGE_URL)
        findViewById<TextView>(R.id.textView).text =
            "Photo taken by $roverName with camera $camera on earth date of $earthDate"
        Picasso.get().load(imgUrl).into(findViewById<ImageView>(R.id.imageView))
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            android.R.id.home -> {
//                finish()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
}