package com.skyapi.weatherforescast.full;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skyapi.weatherforescast.daily.DailyWeatherDTO;
import com.skyapi.weatherforescast.hourly.HourlyWeatherDTO;
import com.skyapi.weatherforescast.realtime.RealtimeWeatherDTO;

import lombok.Data;

@Data
public class FullWeatherDTO {

    private String location;

    @JsonProperty("realtime_weather")
    @JsonInclude(value = Include.CUSTOM,
        valueFilter = RealtimeWeatherFieldFilter.class )
    private RealtimeWeatherDTO realtimeWeather;

    @JsonProperty("hourly_forecast")
    private List<HourlyWeatherDTO> listHourlyWeather;

    @JsonProperty("daily_forecast")
    private List<DailyWeatherDTO> listDailyWeather;
}
