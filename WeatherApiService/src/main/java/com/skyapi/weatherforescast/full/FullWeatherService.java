package com.skyapi.weatherforescast.full;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skyapi.weatherforescast.common.Location;
import com.skyapi.weatherforescast.location.LocationNotFoundException;
import com.skyapi.weatherforescast.location.LocationRepository;

@Service
public class FullWeatherService {

    @Autowired
    private LocationRepository locationRepo;

    public Location getByLocation(Location location) {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();

        Location locationInDb = locationRepo.findByCountryCodeAndCityname(countryCode, cityName);

        if (locationInDb == null) {
            throw new LocationNotFoundException(countryCode, cityName);
        }

        return locationInDb;
    }


}
