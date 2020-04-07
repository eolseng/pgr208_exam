package no.kristiania.pgr208_exam.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.kristiania.pgr208_exam.db.SailAwayDatabase
import no.kristiania.pgr208_exam.entities.Feature
import no.kristiania.pgr208_exam.repositories.FeatureRepository
import no.kristiania.pgr208_exam.utils.UpdateStatus

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    private val featureRepository: FeatureRepository
    val features: LiveData<List<Feature>>

    private val _updateStatus: MutableLiveData<UpdateStatus> = MutableLiveData(UpdateStatus.NOOP)
    val updateStatus: LiveData<UpdateStatus> = _updateStatus

    init {
        val db = SailAwayDatabase.getDatabase(application.applicationContext, viewModelScope)
        val featureDao = db.featureDao()
        featureRepository = FeatureRepository(application.applicationContext, featureDao)
        features = featureRepository.allFeatures
        updateFeatures()
    }

    private fun updateFeatures() =
        viewModelScope.launch {
            _updateStatus.value = UpdateStatus.UPDATING
            _updateStatus.value = featureRepository.updateFeatures()
        }
}
