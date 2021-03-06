package app.wakirox.rainy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import app.wakirox.rainy.domain.DomainController
import app.wakirox.rainy.model.CityEntity
import app.wakirox.rainy.model.WeatherResult
import app.wakirox.rainy.repository.CityRepository
import app.wakirox.rainy.repository.WeatherRepository
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
    private var coroutineScope: CoroutineScope = CoroutineScope(dispatchers + SupervisorJob())

    private val weatherData = MutableLiveData<WeatherResult>()

    val currentCity = MutableLiveData<String>()

    private val cityList by lazy {
        MutableLiveData<List<CityEntity>>().also {
            coroutineScope.launch {
                it.postValue(cityRepository.cities(getApplication()))
            }
        }
    }

    fun cityCursor() = cityRepository.cursor()
    fun cityCursor(query : String) = cityRepository.cursorFilter(query)

    fun getData() = weatherData
    fun getCities(filter : String) = cityRepository.cities(filter)

    fun loadData(city : String, state : String, lang : String){
        cityRepository.cityByState(city,state).first().let {
            loadData(it, lang)
        }
    }

    fun loadData(city : CityEntity = DomainController.city(getApplication()), lang : String) {

        coroutineScope.launch {
            try {
                DomainController.saveCity(getApplication(), city)
                weatherData.postValue(repository.getData(city, lang))
            }catch (e : Exception){
                DomainController.saveCity(getApplication(), CityEntity.default)
                weatherData.postValue(repository.getData(CityEntity.default, lang))
            }

        }
    }

}