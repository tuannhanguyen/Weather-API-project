package com.skyapi.weatherforescast.hourly;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder("hour_of_day")
public class HourlyWeatherDTO {
    @JsonProperty("hour_of_day")
    private int hourOfDay;
    private int temperature;
    private int precipitation;
    private String status;
}
