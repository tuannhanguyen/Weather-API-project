package com.skyapi.weatherforescast.common;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "weather_hourly")
public class HourlyWeather {

    @EmbeddedId
    private HourlyWeatherId id = new HourlyWeatherId();

    private int temperature;
    private int precipitation;

    @Column(length = 50)
    private String status;

}
