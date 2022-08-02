package app.wakirox.rainy.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import app.wakirox.rainy.MainActivity
import app.wakirox.rainy.R
import app.wakirox.rainy.domain.DomainController
import app.wakirox.rainy.model.WeatherResult
import app.wakirox.rainy.repository.WeatherRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

private const val ACTION_UPDATE = "action.UPDATE"

/**
 * Implementation of App Widget functionality.
 */
class WillRainWidget : AppWidgetProvider() {

    private val repository = WeatherRepository()

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        if (ACTION_UPDATE == intent?.action) {
            // if the user clicked next
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val thisAppWidgetComponentName = ComponentName(context!!.packageName, javaClass.name)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName)
            onUpdate(context, appWidgetManager, appWidgetIds)
        }

    }

    private var result : WeatherResult? = null

   @OptIn(DelicateCoroutinesApi::class)
   private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
       result?.let {
           render(it, context, appWidgetManager, appWidgetId)
       }
       GlobalScope.launch(Unconfined) {
           val city = DomainController.city(context)
           repository.getData(city, lang = context.getString(R.string.lang)).let {
               result = it
               render(it, context, appWidgetManager, appWidgetId)
           }

       }


    }
    private fun getPendingSelfIntent(context: Context, action: String): PendingIntent? {
        val intent =
            Intent(context, javaClass) // An intent directed at the current class (the "self").
        intent.action = action
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }
    private fun render(
        it: WeatherResult,
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val rainToday =
            if (it.itRainsToday)
                context.getString(R.string.yes)
            else
                context.getString(R.string.no)

        val todayDrawable =
            if (it.list[0].weather.any { w -> w.main.equals("Rain", true) })
                R.drawable.ic_weather_ko
            else
                R.drawable.ic_weather_ok

        val rainTomorrow =
            if (it.list[1].weather.any { w -> w.main.equals("Rain", true) })
                context.getString(R.string.yes)
            else
                context.getString(R.string.no)

        val tomorrowDrawable =
            if (it.list[1].weather.any { w -> w.main.equals("Rain", true) })
                R.drawable.ic_weather_ko
            else
                R.drawable.ic_weather_ok

        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.will_rain_widget)

        views.setTextViewText(
            R.id.appwidget_text_title, String.format(
                context.getString(R.string.rain_string),
                DomainController.cityString(context)
            )
        )
        views.setInt(R.id.today_image, "setImageResource", todayDrawable)
        views.setInt(R.id.tomorrow_image, "setImageResource", tomorrowDrawable)

        views.setTextViewText(R.id.update_text, Date().dateToString("HH:mm"))


        views.setTextViewText(R.id.appwidget_text, rainToday)
        views.setTextViewText(R.id.appwidget_text_tommorow, rainTomorrow)

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        views.setOnClickPendingIntent(R.id.ll_widget, pendingIntent)

        views.setOnClickPendingIntent(R.id.btn_update, getPendingSelfIntent(context, ACTION_UPDATE))


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}

private fun Date.dateToString(format: String): String {
    //simple date formatter
    val dateFormatter = SimpleDateFormat(format, Locale.getDefault())

    //return the formatted date string
    return dateFormatter.format(this)
}

