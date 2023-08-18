package com.skyapi.weatherforescast.full;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforescast.CommonUtility;
import com.skyapi.weatherforescast.GeolocationService;
import com.skyapi.weatherforescast.common.Location;
import com.skyapi.weatherforescast.location.LocationService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/fully")
public class FullWeatherController {

    @Autowired
    private GeolocationService geolocationService;

    @Autowired
    private FullWeatherService fullWeatherService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<FullWeatherDTO> getByIPAddress(HttpServletRequest request) {
        String IPAddress = CommonUtility.getIPAddress(request);
        Location locationFromIP = geolocationService.getLocation(IPAddress);
        Location locationInDb = fullWeatherService.getByLocation(locationFromIP);

        return ResponseEntity.ok(entity2DTO(locationInDb));
    }

    @GetMapping("/{locationCode}")
    public ResponseEntity<FullWeatherDTO> getByLocationCode(@PathVariable("locationCode") String locationCode) {
        Location locationInDb = locationService.get(locationCode);

        return ResponseEntity.ok(entity2DTO(locationInDb));
    }

    private FullWeatherDTO entity2DTO(Location location) {
        FullWeatherDTO dto = modelMapper.map(location, FullWeatherDTO.class);

        if (dto.getRealtimeWeather() != null) {
            dto.getRealtimeWeather().setLocation(null);
        }

        return dto;
    }
}
