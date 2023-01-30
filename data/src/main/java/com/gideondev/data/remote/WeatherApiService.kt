package com.gideondev.data.remote
import com.gideondev.domain.model.WeatherResponse
import retrofit2.Response

interface WeatherApiService {
    suspend fun getWeatherInfo(
        query: String,
        key: String,
    ): Response<WeatherResponse>
}