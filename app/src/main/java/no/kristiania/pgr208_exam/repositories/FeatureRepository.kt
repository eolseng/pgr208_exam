package no.kristiania.pgr208_exam.repositories

import android.content.Context
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.kristiania.pgr208_exam.db.FeatureDao
import no.kristiania.pgr208_exam.entities.Feature
import no.kristiania.pgr208_exam.services.NoForeignLandService
import no.kristiania.pgr208_exam.utils.UpdateStatus
import no.kristiania.pgr208_exam.utils.Utils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class FeatureRepository(
    private val context: Context,
    private val featureDao: FeatureDao
) {
    private val service: NoForeignLandService = getService()
    val allFeatures: LiveData<List<Feature>> = featureDao.getAll()

    suspend fun updateFeatures(): UpdateStatus {

        lateinit var status: UpdateStatus
        val startTime = SystemClock.uptimeMillis()

        if (Utils.isOnline(context)) {
            withContext(Dispatchers.IO) {
                status = try {
                    val wrapper = service.getAll()
                    val features = wrapper.features
                    featureDao.updateFeatures(features)
                    Log.i("FeatureRepository", "Updated Features in ${SystemClock.uptimeMillis() - startTime}ms")
                    UpdateStatus.SUCCESS
                } catch (e: Exception) {
                    Log.e("FeatureRepository", "Failed to update features: $e")
                    UpdateStatus.ERROR
                }
            }
        } else {
            status = UpdateStatus.ERROR
        }
        return status
    }

    fun getFilteredPosts(filter: String): LiveData<List<Feature>>{
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