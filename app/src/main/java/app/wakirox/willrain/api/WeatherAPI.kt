package app.wakirox.willrain.api

import app.wakirox.willrain.domain.DomainController
import app.wakirox.willrain.model.WeatherResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface WeatherAPI {

    @GET("forecast/daily?cnt=2&appid=${DomainController.API_KEY}")
    suspend fun dayData(@Query("q") cityName : String, @Query("lang") lang : String) : WeatherResult

    @GET("forecast/daily?cnt=2&appid=${DomainController.API_KEY}")
    suspend fun dayDataCoords(@Query("lat") lat : String, @Query("lon") lon : String, @Query("lang") lang : String) : WeatherResult
}