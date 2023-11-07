package com.skyapi.weatherforescast.hourly;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;

@Data
public class HourlyWeatherListDTO extends RepresentationModel<HourlyWeatherListDTO> {
	
	// @JsonProperty replaced by PropertyNamingStrategy.SNAKE_CASE in configuration
	
    private String location;

//    @JsonProperty("hourly_forecast")
    private List<HourlyWeatherDTO> hourlyForecast = new ArrayList<>() ;

    public void addWeatherHourlyDTO(HourlyWeatherDTO dto) {
        this.hourlyForecast.add(dto);
    }
}
