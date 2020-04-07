package no.kristiania.pgr208_exam

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place)
        supportActionBar?.hide()

        // TODO: This is some shitty hacky shit
        placeId = intent.extras!!.get("id") as Long
        model = ViewModelProvider(this, PlaceViewModelFactory(application, placeId)).get(PlaceViewModel::class.java)
        model.place.observe(this, placeObserver)
        model.updateStatus.observe(this, updateObserver)

        // Setup the Map button
        location_button.setOnClickListener{
            val place = model.place.value
            val mapActivity = Intent(applicationContext, MapsActivity::class.java)
            mapActivity.putExtra("name", place?.name)
            mapActivity.putExtra("lat", place?.lat)
            mapActivity.putExtra("lon", place?.lon)
            startActivity(mapActivity)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        updateHandler.removeCallbacksAndMessages(null)
    }

    private val placeObserver = Observer<Place> {place ->
        place?.let {
            place_title.text = place.name
            place_comment.text = place.comments

            if (!place.banner.isNullOrBlank()){
                Picasso.get().load(place.banner).into(place_image)
            }
        }
    }

    private val updateObserver = Observer<UpdateStatus> { status ->
        when (status) {
            UpdateStatus.NOOP -> {}
            UpdateStatus.UPDATING -> {}
            UpdateStatus.SUCCESS -> {}
            UpdateStatus.ERROR -> updateHandler.postDelayed({handleError()}, 500L)
            else -> Log.e("UpdateObserver", "Unknown status: $status")
        }
    }

    private fun handleError() {
        val message = if (model.place.value == null) {
            "Could not connect to the server.\nPlease try again later"
        } else {
            "Could not connect to the server.\nShowing cached data"
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    inner class PlaceViewModelFactory(
        private val application: Application,
        private val placeId: Long) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PlaceViewModel(application, placeId) as T
        }
    }
}

