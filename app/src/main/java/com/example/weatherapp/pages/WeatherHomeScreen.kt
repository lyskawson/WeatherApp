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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.weatherapp.R
import com.example.weatherapp.customuis.AppBackground
import com.example.weatherapp.data.CurrentWeather
import com.example.weatherapp.data.ForecastWeather
import com.example.weatherapp.utils.degree
import com.example.weatherapp.utils.getFormattedDate
import com.example.weatherapp.utils.getIconUrl


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherHomeScreen(
    onRefresh: () -> Unit,
    isConnected: Boolean,
    uiState: WeatherHomeUiState,
    modifier: Modifier = Modifier
) {
    Box( //use box to stack the background and the content
        modifier = modifier.fillMaxSize()
    ) {
        AppBackground(photoId = R.drawable.clouds)
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Weather App",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        actionIconContentColor = Color.White
                    )
                )
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
                    when (uiState) {
                        is WeatherHomeUiState.Loading -> Text("Loading...")
                        is WeatherHomeUiState.Error -> ErrorSection("Failed to load data", onRefresh = onRefresh)
                        is WeatherHomeUiState.Success -> WeatherSection(weather = uiState.weather)
                    }
                }
            }
        }

    }
}

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
        ForecastWeatherSection(
            forecastItems = weather.forecastWeather.list!!,
            modifier = Modifier.weight(1f)
        )

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
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            getFormattedDate(currentWeather.dt!!, pattern = "MMM dd, yyyy"),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            "${currentWeather.main?.temp?.toInt()}$degree",
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            "feels like ${currentWeather.main?.feelsLike?.toInt()}$degree",
            style = MaterialTheme.typography.titleMedium
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(getIconUrl(currentWeather.weather?.get(0)?.icon!!))
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
            Text(
                text = currentWeather.weather[0]!!.description!!,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()

        ) {
            Column{
                Text(
                    text = "Humidity: ${currentWeather.main?.humidity}%",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Pressure: ${currentWeather.main?.pressure}hPa",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Surface(
                modifier = Modifier
                    .height(100.dp)
                    .width(2.dp), color = Color.White) {}
            Spacer(modifier = Modifier.width(10.dp))
            Column() {
                Text(
                    text = "Sunrise: ${getFormattedDate(currentWeather.sys?.sunrise!!, pattern = "HH:mm")}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Sunrise: ${getFormattedDate(currentWeather.sys?.sunset!!, pattern = "HH:mm")}",
                    style = MaterialTheme.typography.titleMedium
                )


            }


        }
    }
}


@Composable
fun ForecastWeatherSection(
    forecastItems :List<ForecastWeather.ForecastItem?>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(7.dp),

    ){
        items(forecastItems.size){index ->
            ForecastWeatherItem(forecastItems[index]!!)
            
        }
    }
}

@Composable
fun ForecastWeatherItem(
    item: ForecastWeather.ForecastItem,
    modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha=0.5f)),
        modifier = modifier
    ){
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ){
            Text(getFormattedDate(item.dt!!, pattern = "EEE"), style = MaterialTheme.typography.titleMedium)
            Text(getFormattedDate(item.dt!!, pattern = "HH:mm"), style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(10.dp))
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(getIconUrl(item.weather?.get(0)?.icon!!))
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(top = 4.dp, bottom = 4.dp)

            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                "${item.main?.temp?.toInt()}$degree",
                style = MaterialTheme.typography.titleMedium
            )

        }
    }
    
}

@Composable
fun ErrorSection(
    message: String,
    onRefresh : () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Text(message)
        Spacer(modifier = Modifier.height(8.dp))
        IconButton(
            onClick = onRefresh,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ){
            Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.White)
        }

    }
    
}
