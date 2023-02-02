package com.gideondev.weatherapp.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gideondev.domain.model.WeatherResponse
import com.gideondev.weatherapp.presentation.viewmodel.WeatherViewModel
import com.gideondev.weatherapp.R
import com.gideondev.weatherapp.presentation.ui.component.MessageDialog
import com.gideondev.weatherapp.presentation.ui.component.WeatherElementIcon
import com.gideondev.weatherapp.utils.*


@Composable
fun MainScreen(
    weatherViewModel: WeatherViewModel = hiltViewModel(),

    ) {
    val uiState = weatherViewModel.uiState
    uiState.events.firstOrNull()?.let {
        HandleEvents(
            event = it,
            handleEvent = weatherViewModel::handleEvent,
        )
    }
    MainContent(
        getWeatherInfo = weatherViewModel::getWeatherInfo,
        isLoading = uiState.isLoading,
        weatherResponse = uiState.weather
    )

}

@Composable
private fun MainContent(
    getWeatherInfo: (String) -> Unit,
    isLoading: WeatherViewModel.LOADINGSTATE,
    weatherResponse: WeatherResponse
) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            SearchCard(getWeatherInfo = getWeatherInfo)
            if (isLoading == WeatherViewModel.LOADINGSTATE.LOADING) {
                LoadingView()
            } else if (isLoading == WeatherViewModel.LOADINGSTATE.NOTLOADING && weatherResponse.cod == 200) {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = stringResource(id = R.string.txt_temperature),
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    )
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp),
                    text = weatherResponse.main?.temp!!.toTemperature("°C"),
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp)
                ) {
                    WeatherElementIcon(
                        iconRes = R.drawable.ic_temp,
                        iconTitle = R.string.current_temp_feels_like_title,
                        iconDescription = weatherResponse.main?.feelsLike?.toTemperature("°C")
                            ?: "~~",
                        modifier = Modifier.weight(0.5f)
                    )
                    WeatherElementIcon(
                        iconRes = R.drawable.ic_cloud,
                        iconTitle = R.string.search_clouds_title,
                        iconDescription = weatherResponse.clouds?.all?.toCloudCover() ?: "~~",
                        modifier = Modifier.weight(0.5f)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp)
                ) {
                    WeatherElementIcon(
                        iconRes = R.drawable.ic_humidity,
                        iconTitle = R.string.search_humidity_title,
                        iconDescription = weatherResponse.main?.humidity?.toHumidity() ?: "~~",
                        modifier = Modifier.weight(0.5f)
                    )
                    WeatherElementIcon(
                        iconRes = R.drawable.ic_pressure,
                        iconTitle = R.string.search_pressure_title,
                        iconDescription = weatherResponse.main?.pressure?.toPressure("hPa") ?: "~~",
                        modifier = Modifier.weight(0.5f)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp)
                ) {
                    WeatherElementIcon(
                        iconRes = R.drawable.ic_wind,
                        iconTitle = R.string.search_wind_speed_title,
                        iconDescription = weatherResponse.wind?.speed?.toSpeed("m/s") ?: "~~",
                        modifier = Modifier.weight(0.5f)
                    )
                    WeatherElementIcon(
                        iconRes = R.drawable.ic_degrees,
                        iconTitle = R.string.search_direction_title,
                        iconDescription = weatherResponse.wind?.deg?.toDirection() ?: "~~",
                        // iconDegrees = current?.windDegrees ?: 0,
                        modifier = Modifier.weight(0.5f)
                    )
                }
            }


        }
    }
}


@Composable
fun SearchCard(getWeatherInfo: (String) -> Unit) {
    val (cityText, setCityText) = rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
            .height(48.dp)
    ) {
        Row {
            TextField(
                value = cityText,
                onValueChange = setCityText,
                label = { Text("City") },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    focusManager.clearFocus()
                    getWeatherInfo(cityText)
                },
                modifier = Modifier
                    .padding(start = 8.dp)
                    .height(48.dp)
            ) {
                Text("Search")
            }
        }
    }
}

@Composable
private fun LoadingView() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        CircularProgressIndicator()
    }
}


@Composable
private fun HandleEvents(
    event: WeatherViewModel.WeatherEvent,
    handleEvent: (WeatherViewModel.WeatherEvent.EventType) -> Unit,
) {
    when (event) {
        is WeatherViewModel.WeatherEvent.WeatherEventError -> {
            MessageDialog(
                primaryAction = { handleEvent(event.type) },
                message = stringResource(id = event.error),
                primaryText = stringResource(id = R.string.ok),
            )
        }
    }
}

@Preview(name = "phone", device = "spec:shape=Normal,width=360,height=840,unit=dp,dpi=480")
@Preview(name = "landscape", device = "spec:shape=Normal,width=840,height=800,unit=dp,dpi=480")
@Preview(name = "tablet", device = "spec:shape=Normal,width=600,height=800,unit=dp,dpi=480")
@Composable
private fun MainScreenPreview() {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        MainContent(
            getWeatherInfo = {},
            isLoading = WeatherViewModel.LOADINGSTATE.DEFAULT,
            weatherResponse = WeatherResponse(),
        )
    }
}