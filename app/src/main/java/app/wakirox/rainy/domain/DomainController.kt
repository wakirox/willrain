package app.wakirox.rainy.domain

import android.content.Context
import app.wakirox.rainy.model.CityEntity
import com.google.gson.Gson

object DomainController {
    const val API_KEY = "42be7c9e87de3817f8a681137754c6f9"
    const val sharedPreferencesVersion = "V2"

    fun city(context: Context) : CityEntity {
        val sharedPref = context.getSharedPreferences(sharedPreferencesVersion,Context.MODE_PRIVATE)
        return sharedPref?.getString("city",null)?.let { Gson().fromJson(it,CityEntity::class.java) } ?: CityEntity.default
    }

    fun saveCity(context: Context, city : CityEntity) {
        context.getSharedPreferences(sharedPreferencesVersion,Context.MODE_PRIVATE)?.edit()?.apply {
            putString("city",Gson().toJson(city))
            apply()
        }
    }

    fun cityString(context: Context) : String {
        val sharedPref = context.getSharedPreferences(sharedPreferencesVersion,Context.MODE_PRIVATE)
        return sharedPref?.getString("cityString",null) ?: city(context).city
    }

    fun saveCityString(context: Context, city : String) {
        context.getSharedPreferences(sharedPreferencesVersion,Context.MODE_PRIVATE)?.edit()?.apply {
            putString("cityString",city)
            apply()
        }
    }



}