package com.skyapi.weatherforescast.daily;

import java.util.ArrayList;
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

    public List<DailyWeather> getByLocationCode(String locationCode) {
        Location locationInDb = locationRepo.findByCode(locationCode);

        if (locationInDb == null) {
            throw new LocationNotFoundException(locationCode);
        }

        return dailyWeatherRepo.findByLocationCode(locationCode);
    }

    public List<DailyWeather> updateByLocationCode(String locationCode, List<DailyWeather> dailyWeatherInRequest ) {
        Location location = locationRepo.findByCode(locationCode);

        if (location == null) {
            throw new LocationNotFoundException(locationCode);
        }

        dailyWeatherInRequest.forEach(dailyWeather -> {
            dailyWeather.getId().setLocation(location);
        });

        List<DailyWeather> dailyWeatherInDb = location.getListDailyWeather();
        List<DailyWeather> dailyWeatherTobeRemoved = new ArrayList<>();

        for (DailyWeather item : dailyWeatherInDb) {
            if (!dailyWeatherInRequest.contains(item)) {
                dailyWeatherTobeRemoved.add(item.getShallowCopy());
            }
        }

        for (DailyWeather item : dailyWeatherTobeRemoved) {
            dailyWeatherInDb.remove(item);
        }

        return (List<DailyWeather>) dailyWeatherRepo.saveAll(dailyWeatherInRequest);
    }
}
