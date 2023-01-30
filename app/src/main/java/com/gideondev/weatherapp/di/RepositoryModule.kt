package com.gideondev.weatherapp.di
import com.gideondev.data.remote.WeatherApiService
import com.gideondev.data.repository.WeatherRepositoryImpl
import com.gideondev.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideWeatherRepository(
        weatherApiService: WeatherApiService,
    ): WeatherRepository =
        WeatherRepositoryImpl(weatherApiService = weatherApiService)
}