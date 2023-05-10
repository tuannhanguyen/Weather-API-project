package com.skyapi.weatherforescast.realtime;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skyapi.weatherforescast.common.Location;
import com.skyapi.weatherforescast.common.RealtimeWeather;
import com.skyapi.weatherforescast.location.LocationNotFoundException;
import com.skyapi.weatherforescast.location.LocationRepository;

@Service
public class RealtimeWeatherService {
    @Autowired
    RealtimeWeatherRepository realtimeWeatherRepo;
    @Autowired
    LocationRepository locationRepo;

    public RealtimeWeather getByLocation(Location location) {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();

        RealtimeWeather realtimeWeather = realtimeWeatherRepo.findByCountryCodeAndCity(countryCode, cityName);

        if (realtimeWeather == null) {
            throw new LocationNotFoundException(countryCode, cityName);
        }

        return realtimeWeather;
    }

    public RealtimeWeather getByLocationCode(String locationCode) {
        RealtimeWeather realtimeWeather = realtimeWeatherRepo.findByLocationCode(locationCode);

        if (realtimeWeather == null) {
            throw new LocationNotFoundException(locationCode);
        }

        return realtimeWeather;
    }

    public RealtimeWeather update(String locationCode, RealtimeWeather realtimeWeather) {
        Location location = locationRepo.findByCode(locationCode);

        if (location == null) {
            throw new LocationNotFoundException(locationCode);
        }

        realtimeWeather.setLocationCode(locationCode);
        realtimeWeather.setLocation(location);
        realtimeWeather.setLastUpdated(new Date());

        if (location.getRealtimeWeather() == null) {
            location.setRealtimeWeather(realtimeWeather);
            Location updatedLocation = locationRepo.save(location);

            return updatedLocation.getRealtimeWeather();
        }

        return realtimeWeatherRepo.save(realtimeWeather);
    }

}
