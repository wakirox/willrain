package app.wakirox.willrain.domain

import android.content.Context
import java.util.*

object DomainController {

    fun city(context: Context) : String {
        val sharedPref = context.getSharedPreferences("V1",Context.MODE_PRIVATE)
        return sharedPref?.getString("city","Roma") ?: "Roma"
    }

    fun saveCity(context: Context, city : String){
        context.getSharedPreferences("V1",Context.MODE_PRIVATE)?.edit()?.apply {
            putString("city",city.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
            apply()
        }
    }



}