package app.wakirox.rainy

import android.app.SearchManager
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.view.Menu
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import app.wakirox.rainy.domain.DomainController
import app.wakirox.rainy.viewmodel.WeatherViewModel
import app.wakirox.rainy.widget.WillRainWidget
import app.wakirox.rainy.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val viewmodel: WeatherViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MobileAds.initialize(this) {}

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.adView.setAdSize(AdSize.BANNER)
//        binding.adView.adUnitId = if(BuildConfig.DEBUG) "ca-app-pub-3940256099942544/6300978111" else "ca-app-pub-7798172338753832/1313329820"

        if(resources.getBoolean(R.bool.enableAds)) {
            val adRequest = AdRequest.Builder().build()
            binding.adView.loadAd(adRequest)
        }

        // Verify the action and get the query
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                doMySearch(query)
            }
        }
        if (Intent.ACTION_VIEW == intent.action) {
            //a suggestion was clicked... do something about it...
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                doMySearch(query)
            }
        }

        viewmodel.currentCity.observe(this){
            binding.cityTitle.text = String.format(
                getString(R.string.rain_string),
                it
            )
        }

        viewmodel.getData().observe(this) {

            DomainController.saveCityString(this,it.city.name)
            viewmodel.currentCity.postValue(it.city.name)

            updateWidget()
            binding.tvResultToday.text =
                if (it.list[0].weather.any { w -> w.main.equals("Rain", true) })
                    getString(R.string.yes)
                else
                    getString(R.string.no)

            binding.tvResultTomorrow.text =
                if (it.list[1].weather.any { w -> w.main.equals("Rain", true) })
                    getString(R.string.yes)
                else
                    getString(R.string.no)

            val todayDrawable =
                if (it.list[0].weather.any { w -> w.main.equals("Rain", true) })
                    R.drawable.ic_weather_ko
                else
                    R.drawable.ic_weather_ok

            val tomorrowDrawable =
                if (it.list[1].weather.any { w -> w.main.equals("Rain", true) })
                    R.drawable.ic_weather_ko
                else
                    R.drawable.ic_weather_ok

            binding.todayImageMain.setImageResource(todayDrawable)
            binding.tomorrowImageMain.setImageResource(tomorrowDrawable)
        }


    }

    private fun updateWidget() {
        val intent = Intent(this, WillRainWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids: IntArray = AppWidgetManager.getInstance(application)
            .getAppWidgetIds(ComponentName(application, WillRainWidget::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        sendBroadcast(intent)
    }

    override fun onResume() {
        super.onResume()
        refreshActivity()
    }

    private fun refreshActivity() {
        viewmodel.loadData(lang = getString(R.string.lang))
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu?.findItem(R.id.search)?.actionView as? SearchView)?.apply {
            // Assumes current activity is the searchable activity
            queryHint = context.getString(R.string.search_hint)

            setOnSuggestionListener(object : SearchView.OnSuggestionListener {
                override fun onSuggestionSelect(position: Int): Boolean {
                    return true
                }

                override fun onSuggestionClick(position: Int): Boolean {
                    val cursor = suggestionsAdapter.getItem(position) as Cursor
                    val city = cursor.getString(cursor.getColumnIndexOrThrow("city"))
                    val state = cursor.getString(cursor.getColumnIndexOrThrow("state"))
                    viewmodel.loadData(city,state, getString(R.string.lang))
                    return true


                }

            })
            setSearchableInfo(
                searchManager.getSearchableInfo(componentName)
            )
            suggestionsAdapter = SimpleCursorAdapter(
                this@MainActivity,
                R.layout.search_item,
                viewmodel.cityCursor(),
                arrayOf("city", "state"),
                intArrayOf(R.id.title, R.id.subtitle),
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
            )

            suggestionsAdapter.filterQueryProvider = FilterQueryProvider { constraint ->
                viewmodel.cityCursor(constraint.toString())
            }


            isIconifiedByDefault = false // Do not iconify the widget; expand it by default
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the options menu from XML
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)



        return true
    }

    fun getCursorFromList(people: List<Pair<Int, String>>): Cursor {
        val cursor = MatrixCursor(arrayOf("_id", "name"))
        for (person in people) {
            cursor.newRow()
                .add("_id", person.first)
                .add("name", person.second)
        }
        return cursor
    }

    private fun doMySearch(query: String) {
        //DomainController.saveCity(this, query)
        refreshActivity()
    }

}