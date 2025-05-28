package com.example.weatherapp.navigation

import kotlinx.serialization.Serializable

interface AppScreenRoute

@Serializable
object HomeRoute : AppScreenRoute

@Serializable
object ForecastRoute : AppScreenRoute
