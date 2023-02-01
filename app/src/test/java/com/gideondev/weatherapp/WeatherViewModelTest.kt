package com.gideondev.weatherapp

import com.gideondev.domain.usecase.WeatherUseCase
import com.gideondev.testdata.builder.WeatherResponseBuilder
import com.gideondev.testdata.coroutine.CoroutineTestRule
import com.gideondev.weatherapp.presentation.viewmodel.WeatherViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.given
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
class WeatherViewModelTest {
    private val weatherUseCase: WeatherUseCase = mock()
    private val weatherResponse = WeatherResponseBuilder.weatherResponse().build()
    private val dispatcherProvider = CoroutineTestRule().testDispatcherProvider

    private val weatherViewModel = WeatherViewModel(
        dispatcherProvider = dispatcherProvider,
        weatherUseCase = weatherUseCase,
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcherProvider.io())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when user search for city weather, then response is returned with weather information`() =
        runTest {
            given(
                weatherUseCase.invoke(
                    key = "34354878ghbdcdd",
                    query = "Bournemouth",
                )
            ).willReturn(weatherResponse)

           val res =  weatherViewModel.getWeatherInfo("Bournemouth")
            assertThat(weatherViewModel.uiState.weather).isEqualTo(weatherResponse)
        }

    @Test
    fun `when user search for city weather, then event is WeatherError event `() =
        runTest {
            Mockito.`when`(
                weatherUseCase.invoke(
                    key = "34354878ghbdcdd",
                    query = "Bournemouth",
                )
            ).thenThrow(RuntimeException())
            weatherViewModel.getWeatherInfo("Bournemouth")
            val event = weatherViewModel.uiState.events.firstOrNull()
            assertThat(event).isInstanceOf(WeatherViewModel.WeatherEvent.WeatherEventError::class.java)
        }
}