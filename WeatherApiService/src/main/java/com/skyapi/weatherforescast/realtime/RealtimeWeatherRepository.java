package com.skyapi.weatherforescast.realtime;

import org.springframework.data.repository.CrudRepository;

import com.skyapi.weatherforescast.common.RealtimeWeather;

public interface RealtimeWeatherRepository extends CrudRepository<RealtimeWeather, String> {

}
