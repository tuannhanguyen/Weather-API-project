package com.skyapi.weatherforescast.hourly;

import lombok.Data;

@Data
public class HourlyWeatherDTO {
    private int hourOfDay;
    private int temperature;
    private int precipitation;
    private String status;
}
