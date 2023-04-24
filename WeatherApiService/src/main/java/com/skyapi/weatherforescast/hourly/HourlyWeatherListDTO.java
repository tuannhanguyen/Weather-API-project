package com.skyapi.weatherforescast.hourly;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class HourlyWeatherListDTO {
    private String location;

    private List<HourlyWeatherDTO> hourlyForecast = new ArrayList<>() ;

    public void addWeatherHourlyDTO(HourlyWeatherDTO dto) {
        this.hourlyForecast.add(dto);
    }
}
