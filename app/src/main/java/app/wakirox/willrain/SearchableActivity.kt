package app.wakirox.willrain

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.view.Menu
import android.widget.CursorAdapter
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import app.wakirox.willrain.domain.DomainController
import app.wakirox.willrain.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchableActivity : AppCompatActivity() {

    val viewmodel : WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchable_base)

        // Verify the action and get the query
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                doMySearch(query)
            }
        }



    }

    private fun doMySearch(query : String){
        DomainController.saveCity(this,query)
        finish()
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
                this@SearchableActivity,
                android.R.layout.simple_list_item_2,
                getCursorFromList(listOf(1 to "Rome",2 to "Milan")),
                arrayOf("name"),
                intArrayOf(android.R.id.text1),
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
            )
            isSubmitButtonEnabled = true
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            isIconifiedByDefault = false // Do not iconify the widget; expand it by default
        }

        return true
    }

    fun getCursorFromList(people: List<Pair<Int,String>>): Cursor {
        val cursor = MatrixCursor(arrayOf("_id", "name"))
        for (person in people) {
            cursor.newRow()
                .add("_id", person.first)
                .add("name", person.second)
        }
        return cursor
    }
}