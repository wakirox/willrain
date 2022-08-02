package app.wakirox.rainy.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class WeatherResult (
	@SerializedName("city") val city : City,
	@SerializedName("cod") val cod : Int,
	@SerializedName("message") val message : Double,
	@SerializedName("cnt") val cnt : Int,
	@SerializedName("list") val list : List<Forecast>
){
	val itRainsToday get() = list[0].weather.any { w -> w.main.equals("Rain", true) }
	val itRainsTomorrow get() = list[1].weather.any { w -> w.main.equals("Rain", true) }

	companion object {
		fun mock() : WeatherResult {
			return WeatherResult(
				city = City(
					1,
					"Milan",
					Coord(0.0, 0.0),
					country = "Android",
					population = 0,
					timezone = 0
				), cod = 0, cnt = 0, message = 0.0, list = listOf(
					Forecast(
						0,
						0,
						0,
						Temp(0.0, 0.0, 0.0, eve = 0.0, morn = 0.0, night = 0.0),
						clouds = 0,
						deg = 0,
						feels_like = Feels_like(0.0, 0.0, 0.0, morn = 0.0),
						gust = 0.0,
						humidity = 0,
						pop = 0.0,
						pressure = 0,
						rain = 0.0,
						speed = 0.0,
						weather = listOf(Weather(0,"Rain","",""))
					),
					Forecast(
						0,
						0,
						0,
						Temp(0.0, 0.0, 0.0, eve = 0.0, morn = 0.0, night = 0.0),
						clouds = 0,
						deg = 0,
						feels_like = Feels_like(0.0, 0.0, 0.0, morn = 0.0),
						gust = 0.0,
						humidity = 0,
						pop = 0.0,
						pressure = 0,
						rain = 0.0,
						speed = 0.0,
						weather = listOf(Weather(0,"Sunny","",""))
					)
				)
			)
		}
	}
}