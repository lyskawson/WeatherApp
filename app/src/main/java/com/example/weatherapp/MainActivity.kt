package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.pages.ConnectivityState
import com.example.weatherapp.pages.WeatherHomeScreen
import com.example.weatherapp.pages.WeatherHomeUiState
import com.example.weatherapp.pages.WeatherHomeViewModel
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val client: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        enableEdgeToEdge()
        setContent {
            WeatherApp(client)
        }
    }
}


@Composable
fun WeatherApp(
    client: FusedLocationProviderClient,
    modifier: Modifier = Modifier) {
    val weatherHomeViewModel : WeatherHomeViewModel = viewModel(factory = WeatherHomeViewModel.Factory)

    //for location permission
    val context  = LocalContext.current
    var permissionGranted by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),

    ){ isGranted ->
            permissionGranted = isGranted
        }
    LaunchedEffect(Unit) {
        val isPermissionGranted = ContextCompat.checkSelfPermission(
            context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED
        if (!isPermissionGranted) {
            launcher.launch(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        } else {
            permissionGranted = true
        }
    }
    LaunchedEffect(permissionGranted) {
        if (permissionGranted) {
            client.lastLocation.addOnSuccessListener { location ->

                weatherHomeViewModel.setLocation(location.latitude, location.longitude)
                weatherHomeViewModel.getWeatherData()

            }
        }
    }

    val connectivityState by weatherHomeViewModel.connectivityState.collectAsState()
    WeatherAppTheme {
        WeatherHomeScreen(
            onRefresh = {
                weatherHomeViewModel.uiState = WeatherHomeUiState.Loading
                weatherHomeViewModel.getWeatherData()
            },
            isConnected = connectivityState == ConnectivityState.Available,
            uiState = weatherHomeViewModel.uiState)
    }
}

@Preview
@Composable
private fun WeatherAppPreview() {
    WeatherApp(client = LocationServices.getFusedLocationProviderClient(LocalContext.current))
    WeatherHomeScreen(isConnected = true, uiState =  WeatherHomeUiState.Loading, onRefresh = { }, modifier = Modifier)

}
