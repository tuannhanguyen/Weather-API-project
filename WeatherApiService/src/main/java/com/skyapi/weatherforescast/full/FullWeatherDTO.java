package com.skyapi.weatherforescast.full;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.skyapi.weatherforescast.daily.DailyWeatherDTO;
import com.skyapi.weatherforescast.hourly.HourlyWeatherDTO;
import com.skyapi.weatherforescast.realtime.RealtimeWeatherDTO;

import jakarta.validation.Valid;
import lombok.Data;

@Data
public class FullWeatherDTO {
	
// @JsonProperty replaced by PropertyNamingStrategy.SNAKE_CASE in configuration

    private String location;

//    @JsonProperty("realtime_weather")
    @JsonInclude(value = Include.CUSTOM, valueFilter = RealtimeWeatherFieldFilter.class )
    @Valid
    private RealtimeWeatherDTO realtimeWeather;

//    @JsonProperty("hourly_forecast")
    @Valid
    private List<HourlyWeatherDTO> listHourlyWeather;

//    @JsonProperty("daily_forecast")
    @Valid
    private List<DailyWeatherDTO> listDailyWeather;
}
