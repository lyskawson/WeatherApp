package com.example.weatherapp.data

import com.google.gson.annotations.SerializedName

data class ForecastWeather(
    @SerializedName("city")
    val city: City? = null,
    @SerializedName("cnt")
    val cnt: Int? = null,
    @SerializedName("cod")
    val cod: String? = null,
    @SerializedName("list")
    val list: List<ForecastItem?>? = null,
    @SerializedName("message")
    val message: Int? = null
) {
    data class City(
        @SerializedName("coord")
        val coord: Coord? = null,
        @SerializedName("country")
        val country: String? = null,
        @SerializedName("id")
        val id: Int? = null,
        @SerializedName("name")
        val name: String? = null,
        @SerializedName("population")
        val population: Int? = null,
        @SerializedName("sunrise")
        val sunrise: Int? = null,
        @SerializedName("sunset")
        val sunset: Int? = null,
        @SerializedName("timezone")
        val timezone: Int? = null
    ) {
        data class Coord(
            @SerializedName("lat")
            val lat: Double? = null,
            @SerializedName("lon")
            val lon: Double? = null
        )
    }

    data class ForecastItem(
        @SerializedName("clouds")
        val clouds: Clouds? = null,
        @SerializedName("dt")
        val dt: Int? = null,
        @SerializedName("dt_txt")
        val dtTxt: String? = null,
        @SerializedName("main")
        val main: Main? = null,
        @SerializedName("pop")
        val pop: Double? = null,
        @SerializedName("rain")
        val rain: Rain? = null,
        @SerializedName("sys")
        val sys: Sys? = null,
        @SerializedName("visibility")
        val visibility: Number? = null,
        @SerializedName("weather")
        val weather: List<Weather?>? = null,
        @SerializedName("wind")
        val wind: Wind? = null
    ) {
        data class Clouds(
            @SerializedName("all")
            val all: Number? = null
        )

        data class Main(
            @SerializedName("feels_like")
            val feelsLike: Number? = null,
            @SerializedName("grnd_level")
            val grndLevel: Number? = null,
            @SerializedName("humidity")
            val humidity: Number? = null,
            @SerializedName("pressure")
            val pressure: Number? = null,
            @SerializedName("sea_level")
            val seaLevel: Number? = null,
            @SerializedName("temp")
            val temp: Number? = null,
            @SerializedName("temp_kf")
            val tempKf: Number? = null,
            @SerializedName("temp_max")
            val tempMax: Number? = null,
            @SerializedName("temp_min")
            val tempMin: Number? = null
        )

        data class Rain(
            @SerializedName("3h")
            val h: Number? = null
        )

        data class Sys(
            @SerializedName("pod")
            val pod: String? = null
        )

        data class Weather(
            @SerializedName("description")
            val description: String? = null,
            @SerializedName("icon")
            val icon: String? = null,
            @SerializedName("id")
            val id: Number? = null,
            @SerializedName("main")
            val main: String? = null
        )

        data class Wind(
            @SerializedName("deg")
            val deg: Number? = null,
            @SerializedName("gust")
            val gust: Number? = null,
            @SerializedName("speed")
            val speed: Number? = null
        )
    }
}


