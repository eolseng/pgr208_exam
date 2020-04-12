package no.kristiania.pgr208_exam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import no.kristiania.pgr208_exam.entities.Feature
import no.kristiania.pgr208_exam.utils.UpdateStatus
import no.kristiania.pgr208_exam.viewModels.SplashViewModel

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_MIN = 2000L
    private val startTime = SystemClock.uptimeMillis()

    private lateinit var model: SplashViewModel
    private val splashHandler = Handler()
    private var hasCache: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("SplashActivity", "Activity created")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        // Set up the ViewModel and Observers
        model = ViewModelProvider(this).get(SplashViewModel::class.java)
        model.features.observe(this, featureObserver)
        model.updateStatus.observe(this, updateObserver)
    }

    override fun onPause() {
        Log.i("SplashActivity", "Activity paused")
        super.onPause()
        splashHandler.removeCallbacksAndMessages(null)
    }

    override fun onDestroy() {
        Log.i("SplashActivity", "Activity destroyed")
        super.onDestroy()
        // Remove all delayed actions when destroying the SplashActivity
        splashHandler.removeCallbacksAndMessages(null)
    }

    private val updateObserver = Observer<UpdateStatus> { status ->
        Log.d("SplashActivity", "Status updated: $status")
        when (status) {
            UpdateStatus.NOOP -> { }
            UpdateStatus.UPDATING -> { }
            UpdateStatus.SUCCESS -> handleSuccess()
            UpdateStatus.ERROR -> handleError()
            else -> Log.e("UpdateObserver", "Unknown status: $status")
        }
    }

    private val featureObserver = Observer<List<Feature>> { features ->
        hasCache = features.isNotEmpty()
    }

    private fun handleError() {
        val delay = SPLASH_TIME_MIN - timeSinceStart()
        splashHandler.postDelayed(_handleError, delay)
    }

    private val _handleError = {

        if (hasCache) {
            Toast.makeText(
                    this,
            "Unable to retrieve new data.\n" +
                    "Showing cached data",
            Toast.LENGTH_LONG
            ).show()
            handleSuccess()
        } else {
            Toast.makeText(
                this,
                "Sorry, we could not retrieve data from our servers.\n" +
                        "Please try again later!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun handleSuccess() {
        val delay = SPLASH_TIME_MIN - timeSinceStart()
        splashHandler.postDelayed(_goToMainActivity, delay)
    }

    private val _goToMainActivity = {
        Log.i("SplashActivity", "Time in Splash: ${timeSinceStart()}")
        val mainActivity = Intent(applicationContext, MainActivity::class.java)
        startActivity(mainActivity)
        finish()
    }

    private fun timeSinceStart(): Long {
        val nowTime = SystemClock.uptimeMillis()
        return nowTime - startTime
    }
}
