package app.wakirox.willrain

import android.app.SearchManager
import android.content.Context
import android.content.Context.*
import android.content.Intent
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.view.Menu
import android.widget.CursorAdapter
import android.widget.FilterQueryProvider
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import app.wakirox.willrain.databinding.ActivityMainBinding
import app.wakirox.willrain.domain.DomainController
import app.wakirox.willrain.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val viewmodel: WeatherViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


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

        viewmodel.getData().observe(this) {
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
        }


    }

    override fun onResume() {
        super.onResume()
        refreshActivity()
    }

    private fun refreshActivity() {
        supportActionBar?.title = String.format(
            getString(R.string.rain_string),
            DomainController.city(this)
        )

        viewmodel.loadData()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu?.findItem(R.id.search)?.actionView as? SearchView)?.apply {
            // Assumes current activity is the searchable activity

            setOnSuggestionListener(object : SearchView.OnSuggestionListener {
                override fun onSuggestionSelect(position: Int): Boolean {
                    return false
                }

                override fun onSuggestionClick(position: Int): Boolean {
                    val cursor = suggestionsAdapter.getItem(position) as Cursor
                    val txt = cursor.getString(cursor.getColumnIndex("city"))
                    viewmodel.loadData(txt)
                    return false


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
        DomainController.saveCity(this, query)
        refreshActivity()
    }

}