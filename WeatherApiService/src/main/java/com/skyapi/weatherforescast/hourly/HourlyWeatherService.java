package com.skyapi.weatherforescast.hourly;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skyapi.weatherforescast.common.HourlyWeather;
import com.skyapi.weatherforescast.common.Location;
import com.skyapi.weatherforescast.location.LocationNotFoundException;
import com.skyapi.weatherforescast.location.LocationRepository;

@Service
public class HourlyWeatherService {

    @Autowired private HourlyWeatherRepository hourlyWeatherRepo;
    @Autowired private LocationRepository locationRepo;

    public List<HourlyWeather> getByLocation(Location location, int currentHour) throws LocationNotFoundException {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();

        Location locationInDb = locationRepo.findByCountryCodeAndCityname(countryCode, cityName);

        if (locationInDb == null) {
            throw new LocationNotFoundException("No location found with the given country code and city name");
        }

        return hourlyWeatherRepo.findByLocationCode(locationInDb.getCode(), currentHour);
    }

    public List<HourlyWeather> getByLocationCode(String locationCode, int currentHour) throws LocationNotFoundException {
        Location locationInDb = locationRepo.findByCode(locationCode);

        if (locationInDb == null) {
            throw new LocationNotFoundException("No location found with the given code");
        }

        return hourlyWeatherRepo.findByLocationCode(locationCode, currentHour);
    }

}
