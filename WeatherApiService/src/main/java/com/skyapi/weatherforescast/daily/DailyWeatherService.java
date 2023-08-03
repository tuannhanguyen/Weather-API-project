package com.skyapi.weatherforescast.daily;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skyapi.weatherforescast.common.DailyWeather;
import com.skyapi.weatherforescast.common.Location;
import com.skyapi.weatherforescast.location.LocationNotFoundException;
import com.skyapi.weatherforescast.location.LocationRepository;

@Service
public class DailyWeatherService {

    @Autowired
    private LocationRepository locationRepo;

    @Autowired
    private DailyWeatherRepository dailyWeatherRepo;

    public List<DailyWeather> getByLocation(Location location) {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();

        Location locationInDb = locationRepo.findByCountryCodeAndCityname(countryCode, cityName);

        if (locationInDb == null) {
            throw new LocationNotFoundException(countryCode, cityName);
        }

        return dailyWeatherRepo.findByLocationCode(locationInDb.getCode());
    }
}
