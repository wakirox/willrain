package app.wakirox.willrain.model

import com.google.gson.annotations.SerializedName

data class WeatherResult (
	@SerializedName("city") val city : City,
	@SerializedName("cod") val cod : Int,
	@SerializedName("message") val message : Double,
	@SerializedName("cnt") val cnt : Int,
	@SerializedName("list") val list : List<Forecast>
)