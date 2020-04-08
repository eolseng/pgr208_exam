package no.kristiania.pgr208_exam.viewModels

import android.app.Application
import android.util.Log
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

    val updateStatus: LiveData<UpdateStatus>

    init {
        val db = SailAwayDatabase.getDatabase(application.applicationContext)
        val featureDao = db.featureDao()
        featureRepository = FeatureRepository(application.applicationContext, featureDao)
        features = featureRepository.allFeatures
        updateStatus = featureRepository.updateStatus
        updateFeatures()
    }

    private fun updateFeatures() =
        viewModelScope.launch {
            featureRepository.updateFeatures()
        }
}
