package no.kristiania.pgr208_exam.repositories

import android.content.Context
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import no.kristiania.pgr208_exam.db.FeatureDao
import no.kristiania.pgr208_exam.entities.Feature
import no.kristiania.pgr208_exam.services.NoForeignLandService
import no.kristiania.pgr208_exam.utils.UpdateStatus
import no.kristiania.pgr208_exam.utils.Utils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FeatureRepository(
    private val context: Context,
    private val featureDao: FeatureDao
) {

    private val service: NoForeignLandService = getService()
    val allFeatures: LiveData<List<Feature>> = featureDao.getAll()

    private val _updateStatus: MutableLiveData<UpdateStatus> = MutableLiveData(UpdateStatus.NOOP)
    val updateStatus: LiveData<UpdateStatus> = _updateStatus

    suspend fun updateFeatures() {

        Log.i("FeatureRepository", "Attempting to update Features")
        _updateStatus.value = UpdateStatus.UPDATING
        val startTime = SystemClock.uptimeMillis()

        if (Utils.isOnline(context)) {
            try {
                val wrapper = service.getAll()
                val features = wrapper.features
                featureDao.updateFeatures(features)
                Log.i(
                    "FeatureRepository",
                    "Updated ${features.size} Features in ${SystemClock.uptimeMillis() - startTime}ms"
                )
                _updateStatus.value = UpdateStatus.SUCCESS
            } catch (e: Exception) {
                Log.w("FeatureRepository", "Failed to update features: $e")
                _updateStatus.value = UpdateStatus.ERROR
            }
        } else {
            Log.w("FeatureRepository", "Failed to update features: No internet connection")
            _updateStatus.value = UpdateStatus.ERROR
        }
    }

    fun getFilteredPosts(filter: String): LiveData<List<Feature>> {
        return featureDao.searchFeatures("$filter*")
    }

    private fun getService(): NoForeignLandService {
        return Retrofit.Builder()
            .baseUrl("https://www.noforeignland.com/home/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NoForeignLandService::class.java)
    }
}