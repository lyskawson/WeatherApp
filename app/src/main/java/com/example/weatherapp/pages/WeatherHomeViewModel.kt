package com.example.weatherapp.pages

import android.app.Application
import android.net.ConnectivityManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.weatherapp.data.ConnectivityRepository
import com.example.weatherapp.data.CurrentWeather
import com.example.weatherapp.data.DefaultConnectivityRepository
import com.example.weatherapp.data.ForecastWeather
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.WeatherRepositoryImpl
import com.example.weatherapp.utils.WEATHER_API_KEY
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherHomeViewModel(private val connectivityRepository: ConnectivityRepository): ViewModel() {
    private val weatherRepository: WeatherRepository = WeatherRepositoryImpl()
    var uiState: WeatherHomeUiState by mutableStateOf(WeatherHomeUiState.Loading )
    private var lattitude: Double = 0.0
    private var longitude: Double = 0.0

    val connectivityState: StateFlow<ConnectivityState> = connectivityRepository.connectivityState


    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        uiState = WeatherHomeUiState.Error
    }

        fun setLocation(lattitude: Double, longitude: Double) {
            this.lattitude = lattitude
            this.longitude = longitude
    }


    fun getWeatherData(){
        viewModelScope.launch(exceptionHandler) {

            uiState = try {
                val currentWeather = async{getCurrentData()}.await()
                val forecastWeather = async{ getForecastData()}.await()
                WeatherHomeUiState.Success(Weather(currentWeather, forecastWeather))

            } catch (e: Exception) {
                WeatherHomeUiState.Error

            }
        }
    }

    private suspend fun getCurrentData() : CurrentWeather{
        val endUrl = "weather?lat=$lattitude&lon=$longitude&exclude={part}&appid=$WEATHER_API_KEY&units=metric"
        return weatherRepository.getCurrentWeather(endUrl)

    }

    private suspend fun getForecastData() : ForecastWeather {
        val endUrl = "forecast?lat=$lattitude&lon=$longitude&exclude={part}&appid=$WEATHER_API_KEY&units=metric"
        return weatherRepository.getForecastWeather(endUrl)

    }

    //its needed because of the private val connectivityRepository: ConnectivityRepository in the constructor
    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as Application)
                val connectivityManager = application.getSystemService(ConnectivityManager::class.java)
                WeatherHomeViewModel(connectivityRepository = DefaultConnectivityRepository(connectivityManager))
            }
        }

    }


}