package com.skyapi.weatherforescast.hourly;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class HourlyWeatherListDTO {
    private String location;

    @JsonProperty("hourly_forecast")
    private List<HourlyWeatherDTO> hourlyForecast = new ArrayList<>() ;

    public void addWeatherHourlyDTO(HourlyWeatherDTO dto) {
        this.hourlyForecast.add(dto);
    }
}
