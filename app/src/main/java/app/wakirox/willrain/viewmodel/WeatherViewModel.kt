package app.wakirox.willrain.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.wakirox.willrain.domain.DomainController
import app.wakirox.willrain.model.WeatherResult
import app.wakirox.willrain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository, application: Application) : AndroidViewModel(application) {

    private val dispatchers: CoroutineContext = Dispatchers.IO
    var coroutineScope: CoroutineScope = CoroutineScope(dispatchers + SupervisorJob())

    private val weatherData by lazy {
        MutableLiveData<WeatherResult>().also { loadData() }
    }

    fun getData() = weatherData

    private fun loadData(){
        coroutineScope.launch {
            weatherData.postValue(repository.getData(DomainController.city(getApplication())))
        }
    }

}