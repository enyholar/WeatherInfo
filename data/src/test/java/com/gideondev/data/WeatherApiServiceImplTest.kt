package com.gideondev.data
import com.gideondev.data.api.WeatherApi
import com.gideondev.data.remote.WeatherApiService
import com.gideondev.data.remote.WeatherServiceImpl
import com.gideondev.domain.model.WeatherResponse
import com.gideondev.testdata.builder.WeatherResponseBuilder
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
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.exceptions.base.MockitoException
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import retrofit2.Response


@ExperimentalCoroutinesApi
class WeatherApiServiceImplTest {
    private val weatherApi: WeatherApi = mock()
    private val weatherApiService: WeatherApiService = WeatherServiceImpl(
        weatherApi = weatherApi
    )

    val dispatcher = StandardTestDispatcher()
    private val weatherResponse = WeatherResponseBuilder.weatherResponse().build()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `when received successful response, then weather info response is retrieved from api`() =
        runTest {
            val expectedResponse: Response<WeatherResponse> = Response.success(weatherResponse)

            given(
                weatherApi.getWeatherInfo(
                    query = any(),
                    key = any(),
                )
            ).willReturn(expectedResponse)

            val result = weatherApiService.getWeatherInfo(
                key = "74754584dhdfhh",
                query = "bournemouth",
            )

            assertThat(result.body()).isEqualTo(weatherResponse)

        }

    @Test
    fun `when received error response, then propagate error`() = runTest {
        `when`(
            weatherApi.getWeatherInfo(
                query = any(),
                key = any(),
            )
        ).thenThrow(MockitoException("something went wrong"))
        assertThrows<MockitoException> {
            weatherApiService.getWeatherInfo(
                key = "74754584dhdfhh",
                query = "bournemouth",
            )
        }

    }

}