package com.gideondev.weatherapp.di
import com.gideondev.data.api.WeatherApi
import com.gideondev.data.remote.WeatherApiService
import com.gideondev.data.remote.WeatherServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object RemoteDataModule {
    @Provides
    fun provideWeatherRemoteDataSource(weatherApi: WeatherApi) : WeatherApiService =
        WeatherServiceImpl(weatherApi = weatherApi)
}