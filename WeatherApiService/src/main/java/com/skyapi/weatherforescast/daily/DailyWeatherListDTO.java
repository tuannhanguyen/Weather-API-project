package com.skyapi.weatherforescast.daily;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyWeatherListDTO {
	
	// @JsonProperty replaced by PropertyNamingStrategy.SNAKE_CASE in configuration
    private String location;

//    @JsonProperty("daily_forecast")
    private List<DailyWeatherDTO> dailyForecast = new ArrayList<>();

    public void addWeatherDailyDTO(DailyWeatherDTO dto) {
        this.dailyForecast.add(dto);
    }

}
