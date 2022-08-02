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
import android.widget.CursorAdapter
import android.widget.FilterQueryProvider
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.asFlow
import app.wakirox.rainy.domain.DomainController
import app.wakirox.rainy.model.WeatherResult
import app.wakirox.rainy.ui.theme.WillRainTheme
import app.wakirox.rainy.viewmodel.WeatherViewModel
import app.wakirox.rainy.widget.WillRainWidget
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComposeMainActivity : ComponentActivity() {
    val viewmodel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WillRainTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(topBar = {
                        TopAppBar(
                            backgroundColor = MaterialTheme.colors.primaryVariant,
                            title = {
                            Text(text = stringResource(id = R.string.app_name))
                        },
                            actions = {
                                IconButton(onClick = { viewmodel.loadData(DomainController.city(context = this@ComposeMainActivity),getString(R.string.lang)) }) {
                                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                                }
                                IconButton(onClick = {  }) {
                                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                                }
                                var showMenu by remember { mutableStateOf(false) }
                                IconButton(onClick = { showMenu = !showMenu }) {
                                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                                }

                                DropdownMenu(
                                    expanded = showMenu,
                                    onDismissRequest = { showMenu = false }
                                ) {
                                    val uriHandler = LocalUriHandler.current
                                    val context = LocalContext.current
                                    val creatorUrl = stringResource(R.string.creator_url)

                                    DropdownMenuItem(onClick = {
                                        uriHandler.openUri(creatorUrl)
                                    }) {
                                        Row {
                                            Icon(Icons.Filled.Person, "")
                                            Text(
                                                text = stringResource(id = R.string.app_creator),
                                                Modifier.padding(horizontal = 10.dp)
                                            )
                                        }
                                    }

//                                    DropdownMenuItem(onClick = {
////                navController.navigate("settings")
//                                    }) {
//                                        Row {
//                                            Icon(Icons.Filled.Settings, "")
//                                            Text(
//                                                text = stringResource(id = R.string.settings),
//                                                Modifier.padding(horizontal = 10.dp)
//                                            )
//                                        }
//                                    }

                                    Box(Modifier.padding(15.dp)){
                                        Row {
                                            Icon(Icons.Filled.ThumbUp, contentDescription = null, tint = Color.Gray)
                                            Text(
                                                text = stringResource(id = R.string.version) + " " + BuildConfig.VERSION_NAME+"+"+BuildConfig.VERSION_CODE,
                                                Modifier.padding(horizontal = 10.dp),
                                                color = Color.Gray
                                            )
                                        }
                                    }

                                }
                            })
                    }) {
                        val data by viewmodel.getData().asFlow().collectAsState(initial = null)
                        data?.let {
                            MainView(it)
                        } ?: kotlin.run {
                            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = CenterHorizontally, verticalArrangement = Arrangement.Center) {
                                CircularProgressIndicator()
                                Text(text = "Loading in progress")
                            }

                        }

                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewmodel.loadData(DomainController.city(context = this),getString(R.string.lang))
    }
}

@Composable
fun MainView(weatherResult: WeatherResult) {

    Column(modifier = Modifier
        .padding(10.dp)
        .verticalScroll(rememberScrollState())) {
        Text(
            text = stringResource(id = R.string.rain_string, weatherResult.city.name).uppercase(),
            color = MaterialTheme.colors.primary,
            fontSize = 45.sp,
            fontWeight = FontWeight.Bold
        )
        WeatherIcon(rains = weatherResult.itRainsToday)
        Text(
            text = stringResource(id = R.string.and_tomorrow, weatherResult.city.name).uppercase(),
            color = MaterialTheme.colors.primary,
            fontSize = 45.sp,
            fontWeight = FontWeight.Bold
        )
        WeatherIcon(rains = weatherResult.itRainsTomorrow)
    }
}

@Composable
fun WeatherIcon(rains: Boolean) {
    Box {
        Icon(
            painterResource(if (rains) R.drawable.ic_weather_ko else R.drawable.ic_weather_ok),
            tint = MaterialTheme.colors.secondary,
            contentDescription = "",
            modifier = Modifier.size(150.dp)
        )
        Text(
            text = stringResource(id = if (rains) R.string.yes else R.string.no).uppercase(),
            modifier = Modifier.padding(start = 80.dp, top = 20.dp),
            color = MaterialTheme.colors.primaryVariant,
            fontWeight = FontWeight.Bold,
            fontSize = 100.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WillRainTheme {
        MainView(
            weatherResult = WeatherResult.mock()
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF191919)
@Composable
fun DefaultPreviewDark() {
    WillRainTheme(darkTheme = true) {
        MainView(
            weatherResult = WeatherResult.mock()
        )
    }
}