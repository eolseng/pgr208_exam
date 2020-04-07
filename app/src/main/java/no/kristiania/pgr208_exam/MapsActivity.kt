package no.kristiania.pgr208_exam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

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

        placeName = intent.extras?.get("name") as String
        placeLat = intent.extras?.get("lat") as Double
        placeLon = intent.extras?.get("lon") as Double
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker and move the camera
        val coordinates = LatLng(placeLat, placeLon)
        mMap.addMarker(MarkerOptions().position(coordinates).title(placeName))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 12f))
    }
}
