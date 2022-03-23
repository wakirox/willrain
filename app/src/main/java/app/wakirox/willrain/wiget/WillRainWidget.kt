package app.wakirox.willrain.wiget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import app.wakirox.willrain.MainActivity
import app.wakirox.willrain.R
import app.wakirox.willrain.domain.DomainController
import app.wakirox.willrain.repository.WeatherRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


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

   @OptIn(DelicateCoroutinesApi::class)
   private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {

       GlobalScope.launch(Unconfined) {
           val city = DomainController.city(context)
           repository.getData(city, lang = context.getString(R.string.lang)).let {
               val rainToday =
                   if (it.list[0].weather.any { w -> w.main.equals("Rain", true) })
                       context.getString(R.string.yes)
                   else
                       context.getString(R.string.no)

               val rainTomorrow =
                   if (it.list[1].weather.any { w -> w.main.equals("Rain", true) })
                       context.getString(R.string.yes)
                   else
                       context.getString(R.string.no)

               // Construct the RemoteViews object
               val views = RemoteViews(context.packageName, R.layout.will_rain_widget)

               views.setTextViewText(R.id.appwidget_text_title, String.format(
                   context.getString(R.string.rain_string),
                   DomainController.cityString(context)
               ))

               views.setTextViewText(R.id.appwidget_text, rainToday)
               views.setTextViewText(R.id.appwidget_text_tommorow, rainTomorrow)

               val intent = Intent(context, MainActivity::class.java)
               val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
               views.setOnClickPendingIntent(R.id.ll_widget, pendingIntent)

               // Instruct the widget manager to update the widget
               appWidgetManager.updateAppWidget(appWidgetId, views)
           }

       }


    }
}
