package com.skyapi.weatherforescast.daily;

import org.springframework.data.repository.CrudRepository;

import com.skyapi.weatherforescast.common.DailyWeather;
import com.skyapi.weatherforescast.common.DailyWeatherId;

public interface DailyWeatherRepository extends CrudRepository<DailyWeather, DailyWeatherId> {

}
