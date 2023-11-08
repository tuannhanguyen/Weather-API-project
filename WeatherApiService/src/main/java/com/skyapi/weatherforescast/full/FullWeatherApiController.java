package com.skyapi.weatherforescast.full;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforescast.BadRequestException;
import com.skyapi.weatherforescast.CommonUtility;
import com.skyapi.weatherforescast.GeolocationService;
import com.skyapi.weatherforescast.common.Location;
import com.skyapi.weatherforescast.location.LocationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/fully")
public class FullWeatherApiController {

    @Autowired
    private GeolocationService geolocationService;

    @Autowired
    private FullWeatherService fullWeatherService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FullWeatherModelAssembler modelAssembler;

    @GetMapping
    public ResponseEntity<EntityModel<FullWeatherDTO>> getFullWeatherByIPAddress(HttpServletRequest request) {
        String IPAddress = CommonUtility.getIPAddress(request);
        Location locationFromIP = geolocationService.getLocation(IPAddress);
        Location locationInDb = fullWeatherService.getByLocation(locationFromIP);

        FullWeatherDTO dto = entity2DTO(locationInDb);

        return ResponseEntity.ok(modelAssembler.toModel(dto));
    }

    @GetMapping("/{locationCode}")
    public ResponseEntity<FullWeatherDTO> getFullWeatherByLocationCode(@PathVariable("locationCode") String locationCode) {
        Location locationInDb = locationService.get(locationCode);

        return ResponseEntity.ok(entity2DTO(locationInDb));
    }

    @PutMapping("/{locationCode}")
    public ResponseEntity<?> updateByLocationCode(@PathVariable("locationCode") String locationCode,
    		@RequestBody @Valid FullWeatherDTO dto) {

    	if (dto.getListDailyWeather().isEmpty()) {
    		throw new BadRequestException("Daily weather data cannot be empty");
    	}

    	if (dto.getListHourlyWeather().isEmpty()) {
    		throw new BadRequestException("Hourly weather data cannot be empty");
    	}

    	Location updatedLocation = fullWeatherService.update(locationCode, DTO2Entity(dto));

    	return ResponseEntity.ok(entity2DTO(updatedLocation));
    }

    private Location DTO2Entity(FullWeatherDTO dto) {
    	return modelMapper.map(dto, Location.class);
    }

    private FullWeatherDTO entity2DTO(Location location) {
        FullWeatherDTO dto = modelMapper.map(location, FullWeatherDTO.class);

        // do not show the filed location in location object
        if (dto.getRealtimeWeather() != null) {
            dto.getRealtimeWeather().setLocation(null);
        }

        return dto;
    }
}
