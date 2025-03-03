package com.example.mvidecomposetraining.data.mappers

import com.example.mvidecomposetraining.data.remote.models.WeatherCurrentDto
import com.example.mvidecomposetraining.data.remote.models.WeatherDto
import com.example.mvidecomposetraining.data.remote.models.WeatherForecastDto
import com.example.mvidecomposetraining.domain.entities.Forecast
import com.example.mvidecomposetraining.domain.entities.Weather
import java.util.Calendar
import java.util.Date

fun WeatherCurrentDto.toEntity() : Weather {
    return current.toEntity()
}

fun WeatherForecastDto.toEntity() : Forecast {
    return Forecast(
        current = current.toEntity(),
        upcoming = forecast.list.drop(1).map { dayDto ->
            val days = dayDto.days
            Weather(
                tempC = days.tempC,
                condition = days.conditionDto.text,
                conditionUrl = days.conditionDto.iconUrl.correctImageUrl(),
                date = dayDto.date.toCalendar()
            )
        }
    )
}

fun WeatherDto.toEntity() : Weather {
    return Weather(
        tempC = tempC,
        date = date.toCalendar(),
        condition = conditionDto.text,
        conditionUrl = conditionDto.iconUrl.correctImageUrl()
    )
}

private fun Long.toCalendar(): Calendar {
    return Calendar.getInstance().apply {
        time = Date(this@toCalendar * 1000)
    }
}

private fun String.correctImageUrl() : String {
    return "https:$this".replace("64x64", "128x128")
}