package no.kristiania.pgr208_exam.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.kristiania.pgr208_exam.db.SailAwayDatabase
import no.kristiania.pgr208_exam.entities.Place
import no.kristiania.pgr208_exam.repositories.PlaceRepository
import no.kristiania.pgr208_exam.utils.UpdateStatus

class PlaceViewModel(
    application: Application,
    placeId: Long
) : AndroidViewModel(application) {

    private val placeRepository: PlaceRepository
    val place: LiveData<Place>
    val updateStatus: LiveData<UpdateStatus>

    init {
        val db = SailAwayDatabase.getDatabase(application.applicationContext, viewModelScope)
        val placeDao = db.placeDao()
        placeRepository = PlaceRepository(application.applicationContext, placeDao)
        updateStatus = placeRepository.updateStatus
        place = getById(placeId)
    }

    fun getById(id: Long): LiveData<Place> {
        viewModelScope.launch { placeRepository.updatePlace(id) }
        return placeRepository.getById(id)
    }

}