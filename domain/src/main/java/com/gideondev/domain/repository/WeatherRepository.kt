package com.gideondev.domain.repository

import com.gideondev.domain.model.WeatherResponse

interface WeatherRepository {
    suspend fun getWeatherInfo(
        query: String,
        key: String,
    ): WeatherResponse
}