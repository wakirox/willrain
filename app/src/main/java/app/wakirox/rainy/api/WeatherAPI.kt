package app.wakirox.rainy.api

import app.wakirox.rainy.domain.DomainController
import app.wakirox.rainy.model.WeatherResult
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherAPI {

    @GET("forecast/daily?cnt=2&appid=${DomainController.API_KEY}")
    suspend fun dayData(@Query("q") cityName : String, @Query("lang") lang : String) : WeatherResult

    @GET("forecast/daily?cnt=2&appid=${DomainController.API_KEY}")
    suspend fun dayDataCoords(@Query("lat") lat : String, @Query("lon") lon : String, @Query("lang") lang : String) : WeatherResult
}