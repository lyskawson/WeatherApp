package com.example.weatherapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val degree = "\u00b0"

fun getFormattedDate(dt : Number, pattern : String = "dd/MM/yy") : String{
    return SimpleDateFormat(pattern, Locale.getDefault()).format(Date(dt.toLong() * 1000))
}

fun getIconUrl(icon: String) :String{
    return "https://openweathermap.org/img/wn/$icon@2x.png"
}