package com.skyapi.weatherforescast.realtime;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforescast.CommonUtility;
import com.skyapi.weatherforescast.ErrorDTO;
import com.skyapi.weatherforescast.GeolocationException;
import com.skyapi.weatherforescast.GeolocationService;
import com.skyapi.weatherforescast.common.Location;
import com.skyapi.weatherforescast.common.RealtimeWeather;
import com.skyapi.weatherforescast.location.LocationNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/realtime")
public class RealtimeWeatherApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RealtimeWeatherApiController.class);

    @Autowired
    GeolocationService locationService;
    @Autowired
    RealtimeWeatherService realtimeWeatherService;
    @Autowired
    ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<?> getRealtimeWeatherByIPAddress(HttpServletRequest request) {
        String ipAddress = CommonUtility.getIPAddress(request);
        try {
            Location location = locationService.getLocation(ipAddress);
            RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocation(location);

            RealtimeWeatherDTO dto = modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);

            return ResponseEntity.ok(dto);
        } catch (GeolocationException e) {
            LOGGER.error(e.getMessage(), e);

            return ResponseEntity.badRequest().build();

        } catch (LocationNotFoundException e) {
            LOGGER.error(e.getMessage(), e);

            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{locationCode}")
    public ResponseEntity<?> getRealtimeWeatherByLocationCode(@PathVariable("locationCode") String code, HttpServletRequest request) {
        try {
            RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocationCode(code);
            RealtimeWeatherDTO dto = entity2DTO(realtimeWeather);

            return ResponseEntity.ok(dto);
        } catch (LocationNotFoundException e) {
            LOGGER.error(e.getMessage(), e);

            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{locationCode}")
    public ResponseEntity<?> updateRealtimeWeather(@PathVariable("locationCode") String locationCode,
            @RequestBody @Valid RealtimeWeather realtimeWeatherInRequest) {

        try {
            RealtimeWeather updatedRealtimeWeather = realtimeWeatherService.update(locationCode, realtimeWeatherInRequest);
            RealtimeWeatherDTO dto = entity2DTO(updatedRealtimeWeather);

            return ResponseEntity.ok(dto);
        } catch (LocationNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    private RealtimeWeatherDTO entity2DTO(RealtimeWeather realtimeWeather) {
        return modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);
    }

}
