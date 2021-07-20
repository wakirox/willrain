package app.wakirox.willrain

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.CursorAdapter
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

    val viewmodel : WeatherViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        viewmodel.getData().observe(this){
            binding.tvResult.text = if(it.list[1].weather.any { w -> w.main.equals("Rain",true) } )
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the options menu from XML
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            // Assumes current activity is the searchable activity
            suggestionsAdapter = SimpleCursorAdapter(
                context,
                android.R.layout.simple_list_item_2,
                viewmodel.cityCursor(),
                arrayOf("city", "state"),
                intArrayOf(android.R.id.text1, android.R.id.text2),
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
            )
            isSubmitButtonEnabled = true
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            isIconifiedByDefault = false // Do not iconify the widget; expand it by default
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.search){
            onSearchRequested()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun doMySearch(query: String) {
        DomainController.saveCity(this,query)
        refreshActivity()
    }

}