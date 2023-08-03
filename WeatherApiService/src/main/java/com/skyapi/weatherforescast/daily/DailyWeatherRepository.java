package com.skyapi.weatherforescast.daily;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.skyapi.weatherforescast.common.DailyWeather;
import com.skyapi.weatherforescast.common.DailyWeatherId;

public interface DailyWeatherRepository extends CrudRepository<DailyWeather, DailyWeatherId> {

    @Query("SELECT d FROM DailyWeather d WHERE d.id.location.code = ?1 AND d.id.location.trashed = false")
    public List<DailyWeather> findByLocationCode(String locationCode);

}
