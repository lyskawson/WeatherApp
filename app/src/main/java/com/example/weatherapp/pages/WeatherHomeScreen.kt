package com.example.weatherapp.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.weatherapp.R
import com.example.weatherapp.customuis.AppBackground
import com.example.weatherapp.data.CurrentWeather
import com.example.weatherapp.data.ForecastWeather
import com.example.weatherapp.navigation.AppBottomNavigationBar
import com.example.weatherapp.navigation.ForecastRoute
import com.example.weatherapp.navigation.HomeRoute
import com.example.weatherapp.utils.degree
import com.example.weatherapp.utils.getFormattedDate
import com.example.weatherapp.utils.getIconUrl



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherHomeScreen(
    isConnected: Boolean,
    onRefresh: () -> Unit,
    uiState: WeatherHomeUiState,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        AppBackground(photoId = R.drawable.clouds)
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Weather App", style = MaterialTheme.typography.titleLarge) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        actionIconContentColor = Color.White,

                        )
                )
            },
            bottomBar = {
                AppBottomNavigationBar(navController = navController)
            },
            containerColor = Color.Transparent
        ) {
            Surface(
                color = Color.Transparent,
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .wrapContentSize()
            ) {
                if (!isConnected) {
                    Text("No internet connection", style = MaterialTheme.typography.titleMedium)
                } else {
                    WeatherAppNavigationHost(
                        navController = navController,
                        uiState = uiState,
                        onRefresh = onRefresh
                    )
                }
            }
        }
    }
}
@Composable
fun WeatherAppNavigationHost(
    navController: NavHostController,
    uiState: WeatherHomeUiState,
    onRefresh: () -> Unit
) {
    NavHost(navController = navController, startDestination = HomeRoute) {
        composable<HomeRoute> {
            // Content for the "Home" tab (Current Weather)
            when (uiState) {
                is WeatherHomeUiState.Loading -> {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text("Loading current weather...", color = Color.White)
                    }
                }
                is WeatherHomeUiState.Error -> ErrorSection(
                    message = "Failed to load current weather",
                    onRefresh = onRefresh,
                    modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)
                )
                is WeatherHomeUiState.Success -> {
                    // Pass only current weather to CurrentWeatherSectionHost
                    CurrentWeatherSectionHost(currentWeather = uiState.weather.currentWeather)
                }
            }
        }
        composable<ForecastRoute> {
            // Content for the "Forecast" tab
            when (uiState) {
                is WeatherHomeUiState.Loading -> {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text("Loading forecast...", color = Color.White)
                    }
                }
                is WeatherHomeUiState.Error -> ErrorSection( // You can have a more specific error for forecast if needed
                    message = "Failed to load forecast",
                    onRefresh = onRefresh, // onRefresh might reload all data
                    modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)
                )
                is WeatherHomeUiState.Success -> {
                    ForecastScreen(forecastWeather = uiState.weather.forecastWeather)
                }
            }
        }
    }
}

// This composable now specifically hosts the CurrentWeatherSection
@Composable
fun CurrentWeatherSectionHost(
    currentWeather: CurrentWeather?,
    modifier: Modifier = Modifier
) {
    if (currentWeather == null) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text("Current weather data not available.", color = Color.White)
        }
        return
    }
    // The original CurrentWeatherSection, but now it's the primary content for the Home tab
    Column(
        modifier = modifier
            .fillMaxSize() // Fill the space provided by NavHost
            .padding(16.dp), // Add padding around the content
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Content of the original CurrentWeatherSection START ---
        Text(
            "${currentWeather.name}, ${currentWeather.sys?.country}",
            style = MaterialTheme.typography.titleMedium, color = Color.White)
        Text(
            getFormattedDate(currentWeather.dt!!, pattern = "MMM dd yyyy"),
            style = MaterialTheme.typography.titleMedium, color = Color.White)
        Spacer(modifier = Modifier.height(20.dp))
        Text("${currentWeather.main?.temp?.toInt()}$degree",
            style = MaterialTheme.typography.displayLarge, color = Color.White)
        Spacer(modifier = Modifier.height(20.dp))
        Text("feels like ${currentWeather.main?.feelsLike?.toInt()}$degree",
            style = MaterialTheme.typography.titleMedium, color = Color.White)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(getIconUrl(currentWeather.weather?.get(0)!!.icon!!))
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Text(currentWeather.weather[0]!!.description!!, style = MaterialTheme.typography.titleMedium, color = Color.White)
        }
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly, // Use SpaceEvenly for better distribution
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) { // Center text within columns
                Text("Humidity ${currentWeather.main?.humidity}%",
                    style = MaterialTheme.typography.titleMedium, color = Color.White)
                Text("Pressure ${currentWeather.main?.pressure}hPa",
                    style = MaterialTheme.typography.titleMedium, color = Color.White)
                Text("Visibility ${currentWeather.visibility ?: "N/A"}m", // Corrected Visibility
                    style = MaterialTheme.typography.titleMedium, color = Color.White)
            }
            // Consider removing the Surface divider or styling it better if kept
            // Surface(modifier = Modifier.width(1.dp).height(100.dp).background(Color.White.copy(alpha = 0.5f)))
            Column(horizontalAlignment = Alignment.CenterHorizontally) { // Center text
                Text("Sunrise ${getFormattedDate(currentWeather.sys?.sunrise!!, pattern = "HH:mm")}",
                    style = MaterialTheme.typography.titleMedium, color = Color.White)
                Text("Sunset ${getFormattedDate(currentWeather.sys.sunset!!, pattern = "HH:mm")}", // Corrected typo
                    style = MaterialTheme.typography.titleMedium, color = Color.White)
            }
        }
        // --- Content of the original CurrentWeatherSection END ---
    }
}


@Composable
fun ErrorSection( // Kept ErrorSection, ensure text color is visible
    message: String,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(message, color = Color.White) // Ensure text is visible
        Spacer(modifier = Modifier.height(8.dp))
        IconButton(
            onClick = onRefresh,
        ) {
            Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White)
        }
    }
}

//
//@Composable
//fun ErrorSection(
//    message: String,
//    onRefresh: () -> Unit,
//    modifier: Modifier = Modifier) {
//    Column {
//        Text(message)
//        Spacer(modifier = Modifier.height(8.dp))
//        IconButton(
//            onClick = onRefresh,
//            modifier = Modifier.align(Alignment.CenterHorizontally)
//        ) {
//            Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.White)
//        }
//    }
//}

@Composable
fun WeatherSection(
    weather: Weather,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(8.dp)
    ) {
        CurrentWeatherSection(
            currentWeather = weather.currentWeather,
            modifier = Modifier.weight(1f)
        )
        //ForecastWeatherSection(forecastItems = weather.forecastWeather.list!!)

    }
}

@Composable
fun CurrentWeatherSection(
    currentWeather: CurrentWeather,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "${currentWeather.name}, ${currentWeather.sys?.country}",
            style = MaterialTheme.typography.titleMedium)
        Text(
            getFormattedDate(currentWeather.dt!!, pattern = "MMM dd yyyy"),
            style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(20.dp))
        Text("${currentWeather.main?.temp?.toInt()}$degree",
            style = MaterialTheme.typography.displayLarge)
        Spacer(modifier = Modifier.height(20.dp))
        Text("feels like ${currentWeather.main?.feelsLike?.toInt()}$degree",
            style = MaterialTheme.typography.titleMedium)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(getIconUrl(currentWeather.weather?.get(0)!!.icon!!))
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Text(currentWeather.weather[0]!!.description!!, style = MaterialTheme.typography.titleMedium)
        }
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text("Humidity ${currentWeather.main?.humidity}%",
                    style = MaterialTheme.typography.titleMedium)
                Text("Pressure ${currentWeather.main?.pressure}hPa",
                    style = MaterialTheme.typography.titleMedium)
                Text("Visibility ${currentWeather.main?.humidity}%",
                    style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Surface(modifier = Modifier
                .width(2.dp)
                .height(100.dp)) {  }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text("Sunrise ${getFormattedDate(currentWeather.sys?.sunrise!!, pattern = "HH:mm")}",
                    style = MaterialTheme.typography.titleMedium)
                Text("Sunrise ${getFormattedDate(currentWeather.sys.sunset!!, pattern = "HH:mm")}",
                    style = MaterialTheme.typography.titleMedium)

            }
        }

    }
}

//@Composable
//fun ForecastWeatherSection(
//    forecastItems: List<ForecastWeather.ForecastItem?>,
//    modifier: Modifier = Modifier
//) {
//    LazyRow(
//        horizontalArrangement = Arrangement.spacedBy(6.dp)
//    ) {
//        items(forecastItems.size) {index ->
//            ForecastWeatherItem(forecastItems[index]!!)
//        }
//    }
//}
//
//@Composable
//fun ForecastWeatherItem(
//    item: ForecastWeather.ForecastItem,
//    modifier: Modifier = Modifier) {
//    Card(
//        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.5f)),
//        modifier = modifier
//    ) {
//        Column(
//            modifier = Modifier.padding(8.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.spacedBy(4.dp)
//        ) {
//            Text(getFormattedDate(item.dt!!, pattern = "EEE"), style = MaterialTheme.typography.titleMedium)
//            Text(getFormattedDate(item.dt, pattern = "HH:mm"), style = MaterialTheme.typography.titleMedium)
//            Spacer(modifier = Modifier.height(10.dp))
//            AsyncImage(
//                model = ImageRequest.Builder(LocalContext.current)
//                    .data(getIconUrl(item.weather?.get(0)!!.icon!!))
//                    .crossfade(true)
//                    .build(),
//                contentDescription = null,
//                modifier = Modifier
//                    .size(40.dp)
//                    .padding(top = 4.dp, bottom = 4.dp)
//            )
//            Spacer(modifier = Modifier.height(10.dp))
//            Text("${item.main?.temp?.toInt()}$degree", style = MaterialTheme.typography.titleMedium)
//        }
//    }
//}