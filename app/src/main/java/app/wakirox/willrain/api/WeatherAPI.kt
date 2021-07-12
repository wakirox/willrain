package app.wakirox.willrain.api

import app.wakirox.willrain.model.WeatherResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface WeatherAPI {

    @GET("forecast/daily?cnt=2&appid=42be7c9e87de3817f8a681137754c6f9")
    suspend fun dayData(@Query("q") cityName : String) : WeatherResult
}