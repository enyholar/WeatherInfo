package com.gideondev.testdata.builder

import com.gideondev.domain.model.Coord
import com.gideondev.domain.model.Main
import com.gideondev.domain.model.Sys
import com.gideondev.domain.model.WeatherResponse

class WeatherResponseBuilder {
    private val main = Main(temp = 261.12, feelsLike = 298.4, tempMax = 300.45, pressure = 1055, tempMin = 206.98)
    private val sys = Sys(country = "United Kingdom", id = 435636, sunset = 36637473)
    private val coordinate = Coord(lon = 3.9049, lat = 6.904)
    fun build () = WeatherResponse(
        main = main,
        sys = sys,
        coord = coordinate,
        name = "bournemouth",
        cod = 200
    )

    companion object{
        fun weatherResponse() = WeatherResponseBuilder()
    }
}