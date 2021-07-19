package app.wakirox.willrain.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import app.wakirox.willrain.domain.DomainController
import app.wakirox.willrain.model.CityEntity
import app.wakirox.willrain.model.WeatherResult
import app.wakirox.willrain.repository.CityRepository
import app.wakirox.willrain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val cityRepository: CityRepository,
    application: Application
) : AndroidViewModel(application) {

    private val dispatchers: CoroutineContext = Dispatchers.IO
    var coroutineScope: CoroutineScope = CoroutineScope(dispatchers + SupervisorJob())

    private val weatherData by lazy {
        MutableLiveData<WeatherResult>().also { loadData() }
    }

    private val cityList by lazy {
        MutableLiveData<List<CityEntity>>().also {
            coroutineScope.launch {
                it.postValue(cityRepository.cities(getApplication()))
            }
        }
    }

    fun cityCursor() = cityRepository.cursor()

    fun getData() = weatherData
    fun getCities(filter : String) = cityRepository.cities(filter)

    fun loadData(city : String = DomainController.city(getApplication())) {
        coroutineScope.launch {
            try {
                weatherData.postValue(repository.getData(city))
            }catch (e : Exception){
                DomainController.saveCity(getApplication(),"Rome")
                weatherData.postValue(repository.getData("Rome"))
            }

        }
    }

}