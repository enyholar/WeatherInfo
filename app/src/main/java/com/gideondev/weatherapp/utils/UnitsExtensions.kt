package com.gideondev.weatherapp.utils

import kotlin.math.roundToInt

fun Double.toTemperature(unit: String): String {
    return when (unit) {
        "°C" -> {
            val celcius = this - 273.15
            "${celcius.roundToInt()} °C"
        }
        "°F" -> {
            val fahrenheit = (9 / 5 * (this - 273)) + 32
            "${fahrenheit.roundToInt()} °F"
        }
        else -> {
            ""
        }
    }
}

fun Double.toSpeed(unit: String): String {
    return when (unit) {
        "m/s" -> "${this.roundToInt()} m/s"
        "km/h" -> {
            val speed = this * 3.6
            "${speed.roundToInt()} km/h"
        }
        "mph" -> {
            val speed = this * 2.23694
            "${speed.roundToInt()} mph"
        }
        else -> {
            ""
        }
    }
}

fun Int.toDirection(): String = "${this}°"

fun Int.toPressure(unit: String): String {
    return when (unit) {
        "hPa" -> "$this hPa"
        "inHg" -> {
            val pressure = this * 0.029529983071445
            "${pressure.roundToInt()} inHg"
        }
        else -> {
            ""
        }
    }
}

fun Int.toCloudCover(): String = "$this %"

fun Int.toHumidity(): String = "$this %"