package com.skyapi.weatherforescast.hourly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
            throw new LocationNotFoundException("No location found with the given code" + locationCode);
        }

        return hourlyWeatherRepo.findByLocationCode(locationCode, currentHour);
    }

    public List<HourlyWeather> updateByLocationCode(String locationCode, List<HourlyWeather> hourlyWeatherInRequest) throws LocationNotFoundException {
    	Location location = locationRepo.findByCode(locationCode);
    	
    	if (location == null) {
    		throw new LocationNotFoundException("No location found with the given code" + locationCode);
    	}
    	
    	hourlyWeatherInRequest.forEach(item -> {
    		item.getId().setLocation(location);
    	});
    	
    	List<HourlyWeather> hourlyWeatherInDb = location.getListHourlyWeather();
    	List<HourlyWeather> hourlyWeatherToBeRemoved = new ArrayList<>();
    	
    	for (HourlyWeather item : hourlyWeatherInDb) {
			if (!hourlyWeatherInRequest.contains(item)) {
				hourlyWeatherToBeRemoved.add(item.getShallowCopy());
			}
		}
    	
    	for (HourlyWeather item : hourlyWeatherToBeRemoved) {
    		hourlyWeatherInDb.remove(item);
		}
    	
        return (List<HourlyWeather>) hourlyWeatherRepo.saveAll(hourlyWeatherInRequest);
    }

}
