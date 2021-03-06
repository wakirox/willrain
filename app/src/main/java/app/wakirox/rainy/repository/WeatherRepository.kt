package app.wakirox.rainy.repository

import app.wakirox.rainy.api.WeatherAPI
import app.wakirox.rainy.model.CityEntity
import app.wakirox.rainy.model.WeatherResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


class WeatherRepository @Inject constructor() {

    private var client = OkHttpClient.Builder().addInterceptor(
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    ).build()

    private var retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private var service: WeatherAPI = retrofit.create(WeatherAPI::class.java)

    //todo create database to cache data for today
    suspend fun getData(city: CityEntity, lang : String) = service.dayData(city.city.let { city.countryCode?.takeIf { it.isNotEmpty() }?.let { "${city.city},${it}" } ?: city.city }, lang)

    fun getDataFlow(city: CityEntity, lang: String) : Flow<WeatherResult> {
        return flow {
            emit(getData(city,lang))
        }
    }

}