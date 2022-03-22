package app.wakirox.willrain.domain

import android.content.Context
import java.util.*

object DomainController {
    const val API_KEY = "42be7c9e87de3817f8a681137754c6f9"

    fun city(context: Context) : String {
        val sharedPref = context.getSharedPreferences("V1",Context.MODE_PRIVATE)
        return sharedPref?.getString("city","Roma,it") ?: "Roma,it"
    }

    fun saveCity(context: Context, city : String) {
        context.getSharedPreferences("V1",Context.MODE_PRIVATE)?.edit()?.apply {
            putString("city",city.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
            apply()
        }
    }



}