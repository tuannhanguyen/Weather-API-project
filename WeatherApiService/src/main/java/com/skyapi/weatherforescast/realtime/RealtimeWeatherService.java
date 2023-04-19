package com.skyapi.weatherforescast.realtime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skyapi.weatherforescast.common.Location;
import com.skyapi.weatherforescast.common.RealtimeWeather;
import com.skyapi.weatherforescast.location.LocationNotFoundException;

@Service
public class RealtimeWeatherService {
    @Autowired
    RealtimeWeatherRepository realtimeWeatherRepo;

    public RealtimeWeather getByLocation(Location location) throws LocationNotFoundException {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();

        RealtimeWeather realtimeWeather = realtimeWeatherRepo.findByCountryCodeAndCity(countryCode, cityName);

        if (realtimeWeather == null) {
            throw new LocationNotFoundException("No location found with the given country code and city name");
        }

        return realtimeWeather;
    }

}
