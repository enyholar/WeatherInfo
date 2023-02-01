package com.gideondev.weatherapp.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gideondev.domain.CoroutinesDispatcherProvider
import com.gideondev.domain.model.WeatherResponse
import com.gideondev.domain.usecase.WeatherUseCase
import com.gideondev.weatherapp.R
import com.gideondev.weatherapp.utils.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherUseCase: WeatherUseCase,
    private val dispatcherProvider: CoroutinesDispatcherProvider,

    ) : ViewModel() {
    var uiState by mutableStateOf(UiState())
        private set

    fun getWeatherInfo(city: String) {
        if (city.isNotEmpty()) {
            viewModelScope.launch(dispatcherProvider.io()) {
                uiState = uiState.copy(isLoading = true)
              uiState = try {
                    val result = weatherUseCase.invoke(query = city, key = Constant.WEATHER_API_KEY)
                    if (result.cod == 200) {
                        uiState.copy(weather = result, isLoading = false)
                    } else {
                        val events = uiState.events +
                                WeatherEvent.WeatherEventError(error = R.string.error_no_city_found)
                        uiState.copy(events = events, isLoading = false)
                    }
                } catch (ex: Exception) {
                    val error = translateExceptionToStringId(ex)
                    val events = uiState.events +
                            WeatherEvent.WeatherEventError(error = error)
                    uiState.copy(events = events, isLoading = false)
                }
            }
        }
    }

    fun handleEvent(type: WeatherEvent.EventType) {
        val events = uiState.events.filterNot { it.type == type }
        uiState = uiState.copy(events = events)
    }

    data class UiState(
        val weather: WeatherResponse = WeatherResponse(),
        val isLoading: Boolean = false,
        val events: List<WeatherEvent> = emptyList(),
    )

    sealed class WeatherEvent {
        enum class EventType {
            ERROR,
            NAVIGATE
        }

        abstract val type: EventType

        data class WeatherEventError(
            override val type: EventType = EventType.ERROR,
            val error: Int,
        ) : WeatherEvent()
    }
}