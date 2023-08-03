package com.skyapi.weatherforescast.daily;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforescast.CommonUtility;
import com.skyapi.weatherforescast.GeolocationService;
import com.skyapi.weatherforescast.common.DailyWeather;
import com.skyapi.weatherforescast.common.Location;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/daily")
public class DailyWeatherController {

    @Autowired
    private GeolocationService geolocationService;

    @Autowired
    private DailyWeatherService dailyWeatherService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<?> getDailyForecastByIPAddress(HttpServletRequest request) {
        String ipAddress = CommonUtility.getIPAddress(request);

        Location locationFromIP = geolocationService.getLocation(ipAddress);
        List<DailyWeather> dailyForecast = dailyWeatherService.getByLocation(locationFromIP);

        if (dailyForecast.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(listEntity2DTOList(dailyForecast));
    }

    private DailyWeatherListDTO listEntity2DTOList(List<DailyWeather> listDailyWeather) {
        Location location = listDailyWeather.get(0).getId().getLocation();

        DailyWeatherListDTO listDTO = new DailyWeatherListDTO();
        listDTO.setLocation(location.toString());

        listDailyWeather.forEach(dailyWeather -> {
            DailyWeatherDTO dto = modelMapper.map(dailyWeather, DailyWeatherDTO.class);
            listDTO.addWeatherDailyDTO(dto);
        });

        return listDTO;
    }

}
