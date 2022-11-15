package jwang.example.info6130project1


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import jwang.example.info6130project1.databinding.ActivityMainBinding
import java.io.InputStreamReader
import java.io.SequenceInputStream
import java.net.HttpURLConnection
import java.net.URL

const val CAMERA = "camera"
const val EARTH_DATE = "earth date"
const val ROVER_NAME = "rover name"
const val IMAGE_URL = "image url"

const val IS_GRID = "is grid"
const val BUTTON_OPPORTUNITY = "button opportunity"
const val BUTTON_CURIOSITY = "button curiosity"
const val BUTTON_SPIRIT = "button spirit"

class MainActivity : AppCompatActivity() {

    var photosOriginal: ArrayList<Photo> = arrayListOf()
    var photos: ArrayList<Photo> = arrayListOf()
    var isGridLayout = true
    lateinit private var binding: ActivityMainBinding
    var buttonOpportunityOn = true
    var buttonCuriosityOn = true
    var buttonSpiritOn = true
    var adapter = RecyclerViewAdapter(photos)
    lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.setHasFixedSize(true)
        if (savedInstanceState != null) {
            isGridLayout = savedInstanceState.getBoolean(IS_GRID)
            Log.d("Mytag", "isgridOnGet: $isGridLayout")
            buttonCuriosityOn = savedInstanceState.getBoolean(BUTTON_CURIOSITY)
            buttonOpportunityOn = savedInstanceState.getBoolean(BUTTON_OPPORTUNITY)
            buttonSpiritOn = savedInstanceState.getBoolean(BUTTON_SPIRIT)
        }
        getPhotoData().start()
    }


    private fun checkRoverSelection(status:Boolean, button: Button, rover: String) {
        if (!status) {
            button.setBackgroundColor(getColor(android.R.color.darker_gray))
            val thisRoverPhotos =
                photosOriginal.filter { it.rover.name == rover } as ArrayList<Photo>
            photos.removeAll(thisRoverPhotos.toSet())
        }
    }

    fun onButtonClicked(view: View) {
        when(view.id) {
            R.id.buttonCuriosity -> {
                buttonCuriosityOn = !buttonCuriosityOn
                onRoverButtonClick(buttonCuriosityOn, view)
            }

            R.id.buttonOpportunity -> {
                buttonOpportunityOn = !buttonOpportunityOn
                onRoverButtonClick(buttonOpportunityOn, view)
            }
            R.id.buttonSpirit -> {
                buttonSpiritOn = !buttonSpiritOn
                onRoverButtonClick(buttonSpiritOn, view)
            }
        }
    }

    private fun getPhotoData(): Thread {

        return Thread {
           binding.textViewDataFail.text = "Loading"
            val url1 = URL("https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/latest_photos?api_key=2sa9XFvnIUuB8fkKT2k5er85hdzGrOjD52cOF9Ic")
            val url2 = URL("https://api.nasa.gov/mars-photos/api/v1/rovers/spirit/latest_photos?api_key=2sa9XFvnIUuB8fkKT2k5er85hdzGrOjD52cOF9Ic")
            val url3 = URL("https://api.nasa.gov/mars-photos/api/v1/rovers/opportunity/latest_photos?api_key=2sa9XFvnIUuB8fkKT2k5er85hdzGrOjD52cOF9Ic")
            val connection1 = url1.openConnection() as HttpURLConnection
            val connection2 = url2.openConnection() as HttpURLConnection
            val connection3 = url3.openConnection() as HttpURLConnection

            if(connection1.responseCode == 200 && connection2.responseCode == 200 && connection3.responseCode == 200) {
                val input1 = connection1.inputStream
                val input2 = connection2.inputStream
                val input3 = connection3.inputStream
                val inputs = listOf(input1, input2, input3)
                for (input in inputs) {
                    val inputStreamReader = InputStreamReader(input,"UTF-8")
                    val request = Gson().fromJson(inputStreamReader,APIFormat::class.java)
                    photosOriginal.addAll(request.latest_photos!!)
                    inputStreamReader.close()
                    input.close()
                }

                photos.addAll(photosOriginal)
                updateUI(photos)

            }
            else
            {
                runOnUiThread {
                    kotlin.run {
                        binding.textViewDataFail.text = "Failed connection"
                    }
                }
            }
        }
    }

    private fun updateUI(photos: ArrayList<Photo>) {
        runOnUiThread {
            kotlin.run {

                    checkRoverSelection(buttonCuriosityOn, binding.buttonCuriosity, "Curiosity")
                    checkRoverSelection(buttonOpportunityOn, binding.buttonOpportunity, "Opportunity")
                    checkRoverSelection(buttonSpiritOn, binding.buttonSpirit, "Spirit")
                    adapter = RecyclerViewAdapter(photos)
                    chooseLayout()
                    binding.textViewPhotoNumber.text = getString(R.string.total_number) + adapter.itemCount
                    binding.textViewDataFail.visibility = TextView.GONE
            }
        }
    }

    private fun onRoverButtonClick(on: Boolean, view: View) {
        if (on) {
            view.setBackgroundColor(getColor(R.color.purple_500))
        } else {
            view.setBackgroundColor(getColor(android.R.color.darker_gray))
        }
        when(view.id){
            R.id.buttonSpirit->{
                val spiritPhotos = photosOriginal.filter { it.rover.name == "Spirit" } as ArrayList<Photo>
                if (buttonSpiritOn) {

                    photos.addAll(spiritPhotos)
                } else {
                    photos.removeAll(spiritPhotos)
                }
                adapter.notifyDataSetChanged()
                binding.textViewPhotoNumber.text = getString(R.string.total_number) + adapter.itemCount
            }
            R.id.buttonCuriosity->{
                val curiosityPhotos = photosOriginal.filter { it.rover.name == "Curiosity" } as ArrayList<Photo>
                if (buttonCuriosityOn) {
                    photos.addAll(curiosityPhotos)
                } else {
                    photos.removeAll(curiosityPhotos)
                }
                adapter.notifyDataSetChanged()
                binding.textViewPhotoNumber.text = getString(R.string.total_number) + adapter.itemCount
            }
            R.id.buttonOpportunity->{
                val opportunityPhotos = photosOriginal.filter { it.rover.name == "Opportunity" } as ArrayList<Photo>
                if (buttonOpportunityOn) {
                    photos.addAll(opportunityPhotos)
                } else {
                    photos.removeAll(opportunityPhotos)
                }
                adapter.notifyDataSetChanged()
                binding.textViewPhotoNumber.text = getString(R.string.total_number) + adapter.itemCount
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.layout_menu, menu)
        return (super.onCreateOptionsMenu(menu))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_switch_layout -> {
                isGridLayout = !isGridLayout
                chooseLayout()
                changeIcon(item)
                return true
            }
            R.id.action_about -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
/*
 * The following two functions are referenced from https://github.com/google-developer-training/
 * android-fundamentals-apps-v2/tree/master/RecyclerView including the drawables for icons.
 */

    private fun changeIcon(item: MenuItem) {
        if (isGridLayout) {
            item.icon = resources.getDrawable(R.drawable.ic_grid_layout)
        } else {
            item.icon = getDrawable(R.drawable.ic_linear_layout)
        }
    }

    private fun chooseLayout() {
        if (isGridLayout) {
            binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
            binding.recyclerView.adapter = adapter
        } else {
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            binding.recyclerView.adapter = adapter
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putBoolean(IS_GRID, isGridLayout)
        Log.d("Mytag", "isgridOnSave: $isGridLayout")
        savedInstanceState.putBoolean(BUTTON_CURIOSITY, buttonCuriosityOn)
        savedInstanceState.putBoolean(BUTTON_OPPORTUNITY, buttonOpportunityOn)
        savedInstanceState.putBoolean(BUTTON_SPIRIT, buttonSpiritOn)
    }
}