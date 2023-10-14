package com.skyapi.weatherforescast.full;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skyapi.weatherforescast.common.DailyWeather;
import com.skyapi.weatherforescast.common.HourlyWeather;
import com.skyapi.weatherforescast.common.Location;
import com.skyapi.weatherforescast.common.RealtimeWeather;
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
    
    public Location update(String locationCode, Location locationInRequest) {
    	Location locationInDB = locationRepo.findByCode(locationCode);
    	
    	if (locationInDB == null) {
    		throw new LocationNotFoundException(locationCode);
    	}
    	
    	RealtimeWeather realtimeWeather = locationInRequest.getRealtimeWeather();
    	realtimeWeather.setLocation(locationInDB);
    	realtimeWeather.setLocationCode(locationCode);
    	realtimeWeather.setLastUpdated(new Date());
    	
    	List<HourlyWeather> listHourlyWeather = locationInRequest.getListHourlyWeather();
    	listHourlyWeather.forEach(hw -> hw.getId().setLocation(locationInDB));
    	
    	List<DailyWeather> listDailyWeather = locationInRequest.getListDailyWeather();
    	listDailyWeather.forEach(dw -> dw.getId().setLocation(locationInDB));
    	
    	locationInRequest.setCode(locationInDB.getCode());
    	locationInRequest.setCityName(locationInDB.getCityName());
    	locationInRequest.setRegionName(locationInDB.getRegionName());
    	locationInRequest.setCountryCode(locationInDB.getCountryCode());
    	locationInRequest.setCountryName(locationInDB.getCountryName());
    	locationInRequest.setEnabled(locationInDB.isEnabled());
    	locationInRequest.setTrashed(locationInDB.isTrashed());
    	
    	return locationRepo.save(locationInRequest);
    }


}
