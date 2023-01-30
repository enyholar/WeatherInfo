package com.gideondev.data.repository

import com.gideondev.data.remote.WeatherApiService
import com.gideondev.data.utils.ApiError
import com.gideondev.data.utils.ApiException
import com.gideondev.data.utils.ApiSuccess
import com.gideondev.data.utils.handleApi
import com.gideondev.domain.model.WeatherResponse
import com.gideondev.domain.repository.WeatherRepository

class WeatherRepositoryImpl(
    private val weatherApiService: WeatherApiService
) : WeatherRepository {

    override suspend fun getWeatherInfo(
        query: String,
        key: String,
    ): WeatherResponse {
        val networkCallResult = handleApi {
            weatherApiService.getWeatherInfo(
                query = query,
                key = key,
            )
        }
        return when (networkCallResult) {
            is ApiSuccess -> networkCallResult.data
            is ApiError -> throw Throwable(networkCallResult.message)
            is ApiException -> throw networkCallResult.e
        }
    }
}