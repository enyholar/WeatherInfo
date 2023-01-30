package com.gideondev.weatherapp.di
import com.gideondev.domain.repository.WeatherRepository
import com.gideondev.domain.usecase.WeatherUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideWeatherUseCases(weatherRepository: WeatherRepository) =
        WeatherUseCase(weatherRepository = weatherRepository)
}