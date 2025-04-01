package com.example.weatherapp.network

import com.example.weatherapp.data.CurrentWeather
import com.example.weatherapp.data.ForecastWeather
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()


interface WeatherApiService {
    @GET()
    suspend fun getCurrentWeather(@Url endUrl: String): CurrentWeather

    @GET()
    suspend fun getForecastWeather(@Url endUrl: String): ForecastWeather
}

//interface WeatherApiService {
//    @GET("weather")
//    suspend fun getCurrentWeather(
//        @Query("lat") latitude: Double,
//        @Query("lon") longitude: Double,
//        @Query("appid") apiKey: String,
//        @Query("units") units: String = "metric"
//    ): CurrentWeather
//
//    @GET("forecast")
//    suspend fun getForecastWeather(
//        @Query("lat") latitude: Double,
//        @Query("lon") longitude: Double,
//        @Query("appid") apiKey: String,
//        @Query("units") units: String = "metric"
//    ): ForecastWeather
//}


object WeatherApi {
    val retrofitService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    } // by lazy - the value gets computed only upon first access then it is reused
}