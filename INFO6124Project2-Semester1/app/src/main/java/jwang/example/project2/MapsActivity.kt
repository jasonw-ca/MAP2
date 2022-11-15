package jwang.example.project2

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import jwang.example.project2.databinding.ActivityMapsBinding
import java.io.IOException
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object{
        var myCurrentAddress = ""
    }

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var loc: LatLng
    private val TAG = "MyMaps"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
                Log.i(TAG, "Fine Location accessed")
                getCurrentLocation()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
                Log.i(TAG, "Course Location accessed")
                getCurrentLocation()
            } else -> {
            Log.i(TAG, "No location permissions")
            }
        }
    }

    private fun getCurrentLocation() {
        Log.i(TAG, "Getting current location")
        fusedLocationClient.lastLocation
            .addOnSuccessListener(this) { lastLocation: Location? ->
                if (lastLocation != null) {
                    loc = LatLng(lastLocation.latitude, lastLocation.longitude)
                    val snippetString:String= getAddress(loc)
                    Log.i(TAG, loc.toString())
                    mMap.addMarker(MarkerOptions().position(loc).icon(
                        BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_RED))
                        .title(getString(R.string.my_location_title)).snippet(snippetString))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc))
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 15f))
                    mMap.setInfoWindowAdapter(object : InfoWindowAdapter {
                        override fun getInfoWindow(arg0: Marker): View? {
                            return null
                        }

                        override fun getInfoContents(marker: Marker): View {
                            val info = LinearLayout(this@MapsActivity)
                            info.orientation = LinearLayout.VERTICAL
                            val title = TextView(this@MapsActivity)
                            title.setTextColor(Color.BLACK)
                            title.gravity = Gravity.CENTER
                            title.setTypeface(null, Typeface.BOLD)
                            title.text = marker.title
                            val snippet = TextView(this@MapsActivity)
                            snippet.setTextColor(Color.GRAY)
                            snippet.text = marker.snippet
                            info.addView(title)
                            info.addView(snippet)
                            return info
                        }
                    })
                }
            }
    }

    private fun getAddress(loc:LatLng): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        var addresses: List<Address>? = null
        try {
            addresses = geocoder.getFromLocation(loc.latitude, loc.longitude, 1)
        } catch (e1: IOException) {
            Log.e(TAG, getString(R.string.service), e1)
        } catch (e2: IllegalArgumentException) {
            Log.e(TAG, getString(R.string.invalid_lat_long)+ ". " +
                    "Latitude = " + loc.latitude +
                    ", Longitude = " +
                    loc.longitude, e2)
        }
        if (addresses != null) {
            // Get the first address
            val address = addresses[0]
            myCurrentAddress = address.getAddressLine(0) + "\n" + "Lng: "+loc.longitude + ", Lat: "+loc.latitude
//                address.locality,
//                address.countryName

            return myCurrentAddress
        }
        else
        {
            Log.e(TAG, getString(R.string.no_address_found))
            return ""
        }
    }

    fun onButtonClick(v: View) {
        when(v.id){

            R.id.goEmail->{
                if (myCurrentAddress == "") {
                    Toast.makeText(this,getString(R.string.no_address), Toast.LENGTH_LONG).show()
                } else {
                    val intent = Intent(this, EmailActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            R.id.goSMS->{
                if (myCurrentAddress == "") {
                    Toast.makeText(this,getString(R.string.no_address), Toast.LENGTH_LONG).show()
                } else {
                    val intent = Intent(this, SMSActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}