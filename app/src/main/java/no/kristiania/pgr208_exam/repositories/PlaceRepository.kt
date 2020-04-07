package no.kristiania.pgr208_exam.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import no.kristiania.pgr208_exam.db.PlaceDao
import no.kristiania.pgr208_exam.entities.Place
import no.kristiania.pgr208_exam.services.NoForeignLandService
import no.kristiania.pgr208_exam.utils.UpdateStatus
import no.kristiania.pgr208_exam.utils.Utils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class PlaceRepository(
    private val context: Context,
    private val placeDao: PlaceDao
) {
    private val service: NoForeignLandService = getService()

    private val _updateStatus: MutableLiveData<UpdateStatus> = MutableLiveData(UpdateStatus.NOOP)
    val updateStatus: LiveData<UpdateStatus> = _updateStatus

    fun getById(id: Long): LiveData<Place> {
        return placeDao.getById(id)
    }

    suspend fun updatePlace(id: Long) {
        Log.i("PlaceRepository", "Attempting to update place with ID $id")
        _updateStatus.value = UpdateStatus.UPDATING
        if (Utils.isOnline(context)) {
            try {
                val wrapper = service.getById(id)
                val place = wrapper.place
                // TODO: Remove HTML tags and &nbsp; from comments before inserting into DB
                placeDao.insert(place)
                _updateStatus.value = UpdateStatus.SUCCESS
                Log.i("PlaceRepository", "Successfully updated place with ID $id")
            } catch (e: Exception) {
                Log.e("PlaceRepository", "Failed to update place: $e")
                _updateStatus.value = UpdateStatus.ERROR
            }
        } else {
            Log.e("PlaceRepository", "Failed to update place: No internet connection")
            _updateStatus.value = UpdateStatus.ERROR
        }
    }

    private fun getService(): NoForeignLandService {
        return Retrofit.Builder()
            .baseUrl("https://www.noforeignland.com/home/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NoForeignLandService::class.java)
    }
}