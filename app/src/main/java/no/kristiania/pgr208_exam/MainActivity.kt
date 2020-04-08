package no.kristiania.pgr208_exam

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import no.kristiania.pgr208_exam.adapters.FeaturesAdapter
import no.kristiania.pgr208_exam.entities.Feature
import no.kristiania.pgr208_exam.utils.UpdateStatus
import no.kristiania.pgr208_exam.viewModels.MainViewModel

class MainActivity : AppCompatActivity(), FeaturesAdapter.OnFeatureClickListener {

    private lateinit var model: MainViewModel
    private val featuresAdapter = FeaturesAdapter(emptyList(), this)
    private val queryHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        // Setup the RecyclerView
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = featuresAdapter

        // Setup the ViewModel and Observers
        model = ViewModelProvider(this).get(MainViewModel::class.java)
        model.features.observe(this, featuresObserver)
        model.updateStatus.observe(this, updateObserver)

        // Setup RecyclerLayout
        refresh_layout.setOnRefreshListener { model.updateFeatures() }

        // Setup SearchView with FTS Search
        search_bar.setOnQueryTextListener(getQueryTextListener())
    }

    override fun onPause() {
        super.onPause()
        queryHandler.removeCallbacksAndMessages(null)
    }

    override fun onFeatureClicked(feature: Feature) {
        val placeActivity = Intent(applicationContext, PlaceActivity::class.java)
        placeActivity.putExtra("id", feature.properties.id)
        startActivity(placeActivity)
    }

    override fun onLocationClicked(feature: Feature) {
        val mapActivity = Intent(applicationContext, MapsActivity::class.java)
        mapActivity.putExtra("name", feature.properties.name)
        mapActivity.putExtra("lat", feature.geometry?.coordinates?.get(1))
        mapActivity.putExtra("lon", feature.geometry?.coordinates?.get(0))
        startActivity(mapActivity)
    }

    private val featuresObserver = Observer<List<Feature>> { features ->
        features?.let { featuresAdapter.setFeatures(features) }
    }

    private val updateObserver = Observer<UpdateStatus> { status ->
        when (status) {
            UpdateStatus.NOOP -> {}
            UpdateStatus.UPDATING -> handleUpdating()
            UpdateStatus.SUCCESS -> handleSuccess()
            UpdateStatus.ERROR -> handleError()
            else -> Log.e("UpdateObserver", "Unknown status: $status")
        }
    }

    private fun handleUpdating() {
        Toast.makeText(this, "Attempting to update places", Toast.LENGTH_SHORT).show()
    }

    private fun handleSuccess() {
        Toast.makeText(this, "Places updated", Toast.LENGTH_SHORT).show()
        refresh_layout.isRefreshing = false
    }

    private fun handleError() {
        Toast.makeText(this, "Failed to update places", Toast.LENGTH_SHORT).show()
        refresh_layout.isRefreshing = false
    }

    private fun getQueryTextListener(): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String?): Boolean {
                queryHandler.removeCallbacksAndMessages(null)
                queryHandler.postDelayed({ model.filterText.value = query }, 300L)
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                queryHandler.removeCallbacksAndMessages(null)
                model.filterText.value = query
                return false
            }
        }
    }
}
