package app.wakirox.rainy.widget

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.*
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import app.wakirox.rainy.R
import app.wakirox.rainy.domain.DomainController
import app.wakirox.rainy.model.WeatherResult
import app.wakirox.rainy.repository.WeatherRepository
import kotlinx.coroutines.launch

@Composable
fun MyWidget() {
    val context = LocalContext.current
    val city = DomainController.city(context)
    var weatherResult : WeatherResult? = null
    val coroutineScope = rememberCoroutineScope()


   LaunchedEffect(key1 = weatherResult, block = {
       coroutineScope.launch {
           weatherResult = WeatherRepository().getData(city, context.getString(R.string.lang))
       }
   })

    if(weatherResult == null){
        CircularProgressIndicator()
    }else{
        Column(horizontalAlignment = Alignment.Horizontal.Start, modifier = GlanceModifier.background(
            Color.Black
        ).fillMaxSize().padding(10.dp)) {
            Text(text = context.getString(R.string.rain_string, city.cityAscii), style = TextStyle(color = ColorProvider(Color.White)))
            Box(contentAlignment = Alignment.CenterStart) {
                Image(provider = ImageProvider(if(weatherResult!!.itRainsToday) R.drawable.ic_weather_ko else R.drawable.ic_weather_ok), contentDescription = "")
                Row {
                    Spacer(modifier = GlanceModifier.width(35.dp))
                    Text(
                        text = context.getString(if (weatherResult!!.itRainsToday) R.string.yes else R.string.no),
                        style = TextStyle(
                            color = ColorProvider(Color.White),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ))
                }
            }
            Text(text = context.getString(R.string.and_tomorrow), style = TextStyle(color = ColorProvider(Color.White)))
            Box(contentAlignment = Alignment.CenterStart) {
                Image(provider = ImageProvider(R.drawable.ic_weather_ok), contentDescription = "")
                Row {
                    Spacer(modifier = GlanceModifier.width(35.dp))
                    Text(
                        text = context.getString(if (weatherResult!!.itRainsToday) R.string.yes else R.string.no),
                        style = TextStyle(
                            color = ColorProvider(Color.White),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ))
                }
            }
        }
    }


}
class WaterWidget : GlanceAppWidget() {
    @Composable
    override fun Content() {
        MyWidget()
    }




}

@Preview
@Composable
private fun PreviewWaterWidget(){
    MyWidget()
}

class WaterWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = WaterWidget()

}