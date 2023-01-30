package com.gideondev.data.remote

import com.gideondev.data.api.WeatherApi
import com.gideondev.domain.model.WeatherResponse
import retrofit2.Response

class WeatherServiceImpl(private val weatherApi: WeatherApi) : WeatherApiService  {
    override suspend fun getWeatherInfo(query: String, key: String): Response<WeatherResponse> {
        return  weatherApi.getWeatherInfo(query = query, key = key)
    }
}