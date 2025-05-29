package com.example.weatherapp.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.weatherapp.data.ForecastWeather
import com.example.weatherapp.utils.degree
import com.example.weatherapp.utils.getFormattedDate
import com.example.weatherapp.utils.getIconUrl

@Composable
fun ForecastScreen(
    forecastWeather: ForecastWeather?,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (forecastWeather == null || forecastWeather.list.isNullOrEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "No forecast data available or still loading.",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            val groupedByDay = forecastWeather.list
                .filterNotNull()
                .groupBy { getFormattedDate(it.dt!!, "EEE, MMM dd") }

            ForecastWeatherContent(groupedForecast = groupedByDay)
        }
    }
}

@Composable
fun ForecastWeatherContent(
    groupedForecast: Map<String, List<ForecastWeather.ForecastItem>>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            "5 Day Forecast",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = Color.White),
            modifier = Modifier.padding(vertical = 16.dp)
        )
        LazyColumn(
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            groupedForecast.forEach { (day, itemsOnDay) ->
                item {
                    Text(
                        text = day,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold, color = Color.White),
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                    Divider(color = Color.White.copy(alpha = 0.3f), thickness = 1.dp, modifier = Modifier.padding(bottom = 8.dp))
                }

                items(itemsOnDay, key = { item -> item.dtTxt ?: item.dt.toString() }) { forecastItem ->
                    ForecastWeatherListItem(item = forecastItem)
                    if (itemsOnDay.last() != forecastItem) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            thickness = 1.dp,
                            color = Color.White.copy(alpha = 0.15f)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ForecastWeatherListItem(
    item: ForecastWeather.ForecastItem,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1.8f)
        ) {
            Text(
                text = getFormattedDate(item.dt!!, "HH:mm"),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color.White),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.weather?.firstOrNull()?.description?.replaceFirstChar { it.uppercase() } ?: "N/A",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.85f)),
                maxLines = 1
            )
        }


        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(getIconUrl(item.weather?.firstOrNull()?.icon ?: ""))
                .crossfade(true)
                .build(),
            contentDescription = item.weather?.firstOrNull()?.description,
            modifier = Modifier
                .size(50.dp) // Adjusted icon size slightly if no card padding
                .weight(0.8f)
        )

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "${item.main?.temp?.toInt()}$degree",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = Color.White),
            )
            item.main?.feelsLike?.let { feelsLikeTemp ->
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Feels ${feelsLikeTemp.toInt()}$degree",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(alpha = 0.7f))
                )
            }
        }
    }
}