package no.kristiania.pgr208_exam.viewModels

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import no.kristiania.pgr208_exam.db.SailAwayDatabase
import no.kristiania.pgr208_exam.entities.Feature
import no.kristiania.pgr208_exam.repositories.FeatureRepository
import no.kristiania.pgr208_exam.utils.UpdateStatus

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val featureRepository: FeatureRepository
    val features: LiveData<List<Feature>>
    val filterText: MutableLiveData<String> = MutableLiveData("")

    val updateStatus: LiveData<UpdateStatus>

    init {
        val db = SailAwayDatabase.getDatabase(application.applicationContext, viewModelScope)
        val featureDao = db.featureDao()
        featureRepository = FeatureRepository(application.applicationContext, featureDao)
        updateStatus = featureRepository.updateStatus
        features = Transformations.switchMap(filterText) { query ->
            if (query.isNullOrBlank()) {
                featureRepository.allFeatures
            } else {
                featureRepository.getFilteredPosts(query)
            }
        }
    }

    fun updateFeatures() =
        viewModelScope.launch { featureRepository.updateFeatures() }

}