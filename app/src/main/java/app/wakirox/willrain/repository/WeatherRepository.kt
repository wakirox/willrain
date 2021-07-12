package app.wakirox.willrain.repository

import app.wakirox.willrain.api.WeatherAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class WeatherRepository @Inject constructor() {

    private var retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private var service: WeatherAPI = retrofit.create(WeatherAPI::class.java)

    //todo create database to cache data for today
    suspend fun getData(city : String) = service.dayData(city)

}