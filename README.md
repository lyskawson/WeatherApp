# Weather App


<p align="center">
  <img src="https://github.com/user-attachments/assets/3fe91535-c7dc-4811-89bf-429b83f4c4ef" width="300"/>
</p>

A sleek and modern weather app built with **Jetpack Compose**, providing real-time weather updates and a 5-day forecast using the **OpenWeather API**.

---

## Features

- Get current weather based on your location
- View current date, temperature, and feels-like conditions
- Dynamic weather icon and description
- Humidity, pressure, visibility, sunrise and sunset data
- 5-day forecast with 3-hour intervals
- Refresh capability and offline state handling
- Bottom navigation between **Home** and **Forecast** screens

---

## Built With

### Core Technologies
- [Jetpack Compose](https://developer.android.com/jetpack/compose) – Modern UI toolkit
- [Material 3](https://m3.material.io/) – UI Components
- [Navigation Compose](https://developer.android.com/jetpack/compose/navigation) – Screen routing
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) – Dependency Injection
- [Retrofit](https://square.github.io/retrofit/) + Gson – REST API integration
- [Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization) – (optional) for JSON parsing
- [Coil](https://coil-kt.github.io/coil/compose/) – Image loading
- [Google Play Services Location](https://developers.google.com/android/reference/com/google/android/gms/location/package-summary) – User location

### Architecture

- **MVVM (Model–View–ViewModel)** structure
- **Repository pattern** for API calls and data handling
- **State management** with UI states (`Loading`, `Success`, `Error`)
- **Navigation** with sealed route declarations

---

## API Used

This app integrates with the [OpenWeatherMap API](https://openweathermap.org/api):

- **Current Weather Data** for real-time location-based info
- **5-Day / 3-Hour Forecast** for upcoming weather conditions
- Requires an API key – register at [openweathermap.org](https://openweathermap.org/appid)

---

## Screens Overview

- **Home Screen**
  - Displays current city, date, weather, temp, and other metrics
- **Forecast Screen**
  - Scrollable list of upcoming 3-hour weather blocks
  - Grouped by day, includes temp, weather icon, and feels-like info

---

## Permissions Required

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />