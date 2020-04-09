package no.kristiania.pgr208_exam

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_place.*
import no.kristiania.pgr208_exam.entities.Place
import no.kristiania.pgr208_exam.utils.UpdateStatus
import no.kristiania.pgr208_exam.viewModels.PlaceViewModel

class PlaceActivity : AppCompatActivity() {

    private lateinit var model: PlaceViewModel
    private var placeId: Long = 0
    private val updateHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("PlaceActivity", "Activity created")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place)
        supportActionBar?.hide()

        placeId = intent.extras!!.get("id") as Long
        model = ViewModelProvider(
            this,
            PlaceViewModelFactory(application, placeId)
        ).get(PlaceViewModel::class.java)
        model.place.observe(this, placeObserver)
        model.updateStatus.observe(this, updateObserver)

        // Setup Back button
        back_button.setOnClickListener { finish() }

        // Setup the Map button
        location_button.setOnClickListener {
            val place = model.place.value
            val mapActivity = Intent(applicationContext, MapsActivity::class.java)
            mapActivity.putExtra("name", place?.name)
            mapActivity.putExtra("lat", place?.lat)
            mapActivity.putExtra("lon", place?.lon)
            startActivity(mapActivity)
        }
    }

    override fun onPause() {
        Log.i("PlaceActivity", "Activity paused")
        super.onPause()
    }

    override fun onDestroy() {
        Log.i("PlaceActivity", "Activity destroyed")
        super.onDestroy()
        updateHandler.removeCallbacksAndMessages(null)
    }

    private val placeObserver = Observer<Place> { place ->
        place?.let {
            place_title.text = place.name
            place_comment.text = place.comments
            if (!place.banner.isNullOrBlank()) {
                Picasso.get().load(place.banner).into(place_image)
            }
        }
    }

    private val updateObserver = Observer<UpdateStatus> { status ->
        when (status) {
            UpdateStatus.NOOP -> {
            }
            UpdateStatus.UPDATING -> {
            }
            UpdateStatus.SUCCESS -> {
            }
            UpdateStatus.ERROR -> updateHandler.postDelayed({ handleError() }, 500L)
            else -> Log.e("UpdateObserver", "Unknown status: $status")
        }
    }

    private fun handleError() {
        Log.i("PlaceActivity", "handleError started")
        val message = if (model.place.value == null) {
            "Could not connect to the server.\nPlease try again later"
        } else {
            "Could not connect to the server.\nShowing cached data"
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        Log.i("PlaceActivity", "handleError finished")
    }

    // This seems like a hacky way to pass the placeId to the ViewModel constructor.
    inner class PlaceViewModelFactory(
        private val application: Application,
        private val placeId: Long
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PlaceViewModel(application, placeId) as T
        }
    }
}
