package no.kristiania.pgr208_exam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private var placeName: String = ""
    private var placeLat: Double = 0.00
    private var placeLon: Double = 0.00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        placeName = intent.extras?.get(EXTRA_NAME) as String
        placeLat = intent.extras?.get(EXTRA_LAT) as Double
        placeLon = intent.extras?.get(EXTRA_LON) as Double

        supportActionBar?.title = placeName
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker and move the camera
        val coordinates = LatLng(placeLat, placeLon)
        mMap.addMarker(MarkerOptions().position(coordinates).title(placeName))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 12f))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val EXTRA_NAME: String  = "no.kristiania.pgr208_exam.entities.Place.name"
        const val EXTRA_LAT: String  = "no.kristiania.pgr208_exam.entities.Place.lat"
        const val EXTRA_LON: String  = "no.kristiania.pgr208_exam.entities.Place.lon"
    }
}
