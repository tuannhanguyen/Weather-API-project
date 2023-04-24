package com.skyapi.weatherforescast.location;

import org.springframework.data.repository.CrudRepository;

import com.skyapi.weatherforescast.common.HourlyWeather;

public interface HourlyWeatherRepository extends CrudRepository<HourlyWeather, String> {

}
