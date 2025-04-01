package com.example.weatherapp.data

import com.example.weatherapp.network.WeatherApi

interface WeatherRepository {
    suspend fun getCurrentWeather(endUrl: String): CurrentWeather
    suspend fun getForecastWeather(endUrl: String): ForecastWeather
}

class WeatherRepositoryImpl : WeatherRepository {
    override suspend fun getCurrentWeather(endUrl: String): CurrentWeather {
        return WeatherApi.retrofitService.getCurrentWeather(endUrl)
    }

    override suspend fun getForecastWeather(endUrl: String): ForecastWeather {
        return WeatherApi.retrofitService.getForecastWeather(endUrl)
    }
}

//interface WeatherRepository {
//    suspend fun getCurrentWeather(latitude: Double, longitude: Double, apiKey: String): CurrentWeather
//    suspend fun getForecastWeather(latitude: Double, longitude: Double, apiKey: String): ForecastWeather
//}
//
//class WeatherRepositoryImpl(private val weatherApiService: WeatherApiService) : WeatherRepository {
//    override suspend fun getCurrentWeather(latitude: Double, longitude: Double, apiKey: String): CurrentWeather {
//        return weatherApiService.getCurrentWeather(latitude, longitude, apiKey)
//    }
//
//    override suspend fun getForecastWeather(latitude: Double, longitude: Double, apiKey: String): ForecastWeather {
//        return weatherApiService.getForecastWeather(latitude, longitude, apiKey)
//    }
//}