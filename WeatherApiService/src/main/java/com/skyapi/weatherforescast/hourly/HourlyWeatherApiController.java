package com.skyapi.weatherforescast.hourly;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforescast.CommonUtility;
import com.skyapi.weatherforescast.GeolocationException;
import com.skyapi.weatherforescast.GeolocationService;
import com.skyapi.weatherforescast.common.HourlyWeather;
import com.skyapi.weatherforescast.common.Location;
import com.skyapi.weatherforescast.location.LocationNotFoundException;
import com.skyapi.weatherforescast.location.LocationService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/hourly")
public class HourlyWeatherApiController {

    @Autowired GeolocationService geoLocationService;
    @Autowired HourlyWeatherService hourlyWeatherService;
    @Autowired LocationService locationService;
    @Autowired ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<?> listHourlyForecastByIpAddress(HttpServletRequest request) {
        String ipAddress = CommonUtility.getIPAddress(request);

        try {
            int currentHour = Integer.parseInt(request.getHeader("X-Current-Hour"));

            Location location = geoLocationService.getLocation(ipAddress);

            List<HourlyWeather> listHourlyWeather = hourlyWeatherService.getByLocation(location, currentHour);

            if (listHourlyWeather.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(listEntity2DTO(listHourlyWeather));
        } catch (NumberFormatException | GeolocationException e) {

            return ResponseEntity.badRequest().build();
        } catch (LocationNotFoundException e) {

            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{locationCode}")
    public ResponseEntity<?> getHourlyWeatherByLocatinCode(@PathVariable("locationCode") String locationCode,HttpServletRequest request) {

        try {
            int currentHour = Integer.parseInt(request.getHeader("X-Current-Hour"));

            List<HourlyWeather> listHourlyWeather = hourlyWeatherService.getByLocationCode(locationCode, currentHour);

            if (listHourlyWeather.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(listEntity2DTO(listHourlyWeather));
        } catch (NumberFormatException e) {

            return ResponseEntity.badRequest().build();
        } catch (LocationNotFoundException e) {

            return ResponseEntity.notFound().build();
        }
    }

    private HourlyWeatherListDTO listEntity2DTO(List<HourlyWeather> hourlyForecast) {
        Location location = hourlyForecast.get(0).getId().getLocation();

        HourlyWeatherListDTO listDTO = new HourlyWeatherListDTO();
        listDTO.setLocation(location.toString());

        hourlyForecast.forEach(hourlyWeather -> {
            HourlyWeatherDTO dto = modelMapper.map(hourlyWeather, HourlyWeatherDTO.class);
            listDTO.addWeatherHourlyDTO(dto);
        });

        return listDTO;
    }
}
