package com.gideondev.weatherapp.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
    private val dispatcherProvider: CoroutinesDispatcherProvider,
    private val weatherUseCase: WeatherUseCase,
    private val notificationBuilder: NotificationCompat.Builder,
    private val notificationManager: NotificationManagerCompat
) : ViewModel() {
    var uiState by mutableStateOf(UiState())
        private set

    fun getWeatherInfo(city: String) {
        if (city.isNotEmpty()) {
            viewModelScope.launch(dispatcherProvider.io()) {
                uiState = uiState.copy(isLoading = LOADINGSTATE.LOADING)
                try {
                    val result = weatherUseCase.invoke(query = city, key = Constant.WEATHER_API_KEY)
                    if (result.cod == 200) {
                        uiState =
                            uiState.copy(weather = result, isLoading = LOADINGSTATE.NOTLOADING)
                        val temp = result.main?.temp?.minus(273.15)
                        if (temp != null && (temp > 20 || temp < 0)) {
                            showNotification(temp)
                        }
                    } else {
                        val events = uiState.events +
                                WeatherEvent.WeatherEventError(error = R.string.error_no_city_found)
                        uiState = uiState.copy(events = events, isLoading = LOADINGSTATE.NOTLOADING)
                    }
                } catch (ex: Exception) {
                    val error =  if (ex.message == "not found"){
                        R.string.error_no_city_found
                    }else{
                        translateExceptionToStringId(ex)
                    }
                    val events = uiState.events +
                            WeatherEvent.WeatherEventError(error = error)
                    uiState = uiState.copy(events = events, isLoading = LOADINGSTATE.NOTLOADING)
                }
            }
        }
    }

    private fun showNotification(temp: Double) {
        notificationManager.notify(
            1209, notificationBuilder
                .setContentTitle("Weather update")
                .setContentText(if (temp < 0) "It is freezing" else "It is boiling")
                .build()
        )
    }

    fun handleEvent(type: WeatherEvent.EventType) {
        val events = uiState.events.filterNot { it.type == type }
        uiState = uiState.copy(events = events)
    }

    data class UiState(
        val weather: WeatherResponse = WeatherResponse(),
        val isLoading: LOADINGSTATE = LOADINGSTATE.DEFAULT,
        val events: List<WeatherEvent> = emptyList(),
    )

    enum class LOADINGSTATE {
        DEFAULT,
        LOADING,
        NOTLOADING,
    }

    sealed class WeatherEvent {
        enum class EventType {
            ERROR,
        }

        abstract val type: EventType

        data class WeatherEventError(
            override val type: EventType = EventType.ERROR,
            val error: Int,
        ) : WeatherEvent()
    }
}