package com.gideondev.data

import com.gideondev.data.remote.WeatherApiService
import com.gideondev.data.repository.WeatherRepositoryImpl
import com.gideondev.domain.model.WeatherResponse
import com.gideondev.testdata.builder.WeatherResponseBuilder
import kotlin.Exception
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.exceptions.base.MockitoException
import retrofit2.Response


@ExperimentalCoroutinesApi
class WeatherRepositoryImplTest {
    private val weatherApiService: WeatherApiService = mock()
    private val weatherRepository =  WeatherRepositoryImpl(
        weatherApiService = weatherApiService
    )

    val dispatcher = StandardTestDispatcher()
    private val weatherResponse = WeatherResponseBuilder().build()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `when received result, then weather info response is returned`() =
        runTest {
            val expectedResponse: Response<WeatherResponse> = Response.success(weatherResponse)

            given(
                weatherApiService.getWeatherInfo(
                    query = any(),
                    key = any(),
                )
            ).willReturn(expectedResponse)

            val result = weatherRepository.getWeatherInfo(
                key = "74754584dhdfhh",
                query = "bournemouth",
            )

            assertThat(result).isEqualTo(weatherResponse)
        }

    @Test
    fun `when received error response, then propagate error`()  = runTest{
        Mockito.`when`(
            weatherApiService.getWeatherInfo(
                query = any(),
                key = any(),
            )
        ).thenThrow(MockitoException("something went wrong"))

        assertThrows<Exception> {
            weatherRepository.getWeatherInfo(
                key = "74754584dhdfhh",
                query = "bournemouth",
            )
        }
    }

}