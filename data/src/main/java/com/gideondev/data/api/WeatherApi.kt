package com.gideondev.data.api

import com.gideondev.domain.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getWeatherInfo(
        @Query("q") query: String,
        @Query("appid") key: String,
    ): Response<WeatherResponse>
}