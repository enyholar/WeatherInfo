package com.gideondev.domain.usecase
import com.gideondev.domain.repository.WeatherRepository

class WeatherUseCase(
    private val weatherRepository: WeatherRepository
) {
    suspend fun invoke(
        query: String,
        key: String,
    ) = weatherRepository.getWeatherInfo(
        query = query,
        key = key,
    )
}