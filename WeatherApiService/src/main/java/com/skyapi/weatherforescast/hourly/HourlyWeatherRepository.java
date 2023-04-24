package com.skyapi.weatherforescast.hourly;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.skyapi.weatherforescast.common.HourlyWeather;

public interface HourlyWeatherRepository extends CrudRepository<HourlyWeather, String> {

    @Query("SELECT h FROM HourlyWeather h WHERE h.id.location.code = ?1 AND h.id.hourOfDay > ?2 AND h.id.location.trashed = false")
    public List<HourlyWeather> findByLocationCode(String locationCode, int currentHour);

}
