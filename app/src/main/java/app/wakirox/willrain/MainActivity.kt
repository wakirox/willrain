package app.wakirox.willrain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import app.wakirox.willrain.databinding.ActivityMainBinding
import app.wakirox.willrain.domain.DomainController
import app.wakirox.willrain.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val viewmodel : WeatherViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = String.format(getString(R.string.rain_string), DomainController.city(this))

        viewmodel.getData().observe(this){
            binding.tvResult.text = if(it.list[1].weather.any { w -> w.main.equals("Rain",true) } ) { getString(R.string.yes)} else getString(R.string.no)
        }

    }
}