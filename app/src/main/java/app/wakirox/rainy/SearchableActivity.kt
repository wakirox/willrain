package app.wakirox.rainy

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchableActivity : AppCompatActivity() {

//    val viewmodel : WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_searchable_base)

        // Verify the action and get the query
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                doMySearch(query)
            }
        }
        if (Intent.ACTION_VIEW == intent.action) {
            //a suggestion was clicked... do something about it...
            intent.getStringExtra(SearchManager.USER_QUERY)?.also { query ->
                doMySearch(query)
            }
        }


    }
    private fun doMySearch(query : String){
        //DomainController.saveCity(this,query)
        finish()
    }

}