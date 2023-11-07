package com.skyapi.weatherforescast.daily;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforescast.BadRequestException;
import com.skyapi.weatherforescast.CommonUtility;
import com.skyapi.weatherforescast.GeolocationService;
import com.skyapi.weatherforescast.common.DailyWeather;
import com.skyapi.weatherforescast.common.Location;
import com.skyapi.weatherforescast.full.FullWeatherApiController;
import com.skyapi.weatherforescast.hourly.HourlyWeatherApiController;
import com.skyapi.weatherforescast.realtime.RealtimeWeatherApiController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/daily")
@Validated
public class DailyWeatherApiController {

    @Autowired
    private GeolocationService geolocationService;

    @Autowired
    private DailyWeatherService dailyWeatherService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<?> listDailyForecastByIPAddress(HttpServletRequest request) {
        String ipAddress = CommonUtility.getIPAddress(request);

        Location locationFromIP = geolocationService.getLocation(ipAddress);
        List<DailyWeather> dailyForecast = dailyWeatherService.getByLocation(locationFromIP);

        if (dailyForecast.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        DailyWeatherListDTO listEntity2DTOList = listEntity2DTOList(dailyForecast);

        return ResponseEntity.ok(addLinksByIP(listEntity2DTOList));
    }

    @GetMapping("/{locationCode}")
    public ResponseEntity<?> listDailyForecastByLocationCode(@PathVariable("locationCode") String locationCode) {
        List<DailyWeather> dailyForecast = dailyWeatherService.getByLocationCode(locationCode);

        if (dailyForecast.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        DailyWeatherListDTO listEntity2DTOList = listEntity2DTOList(dailyForecast);

        return ResponseEntity.ok(addLinksByLocationCode(listEntity2DTOList, locationCode));
    }

    @PutMapping("/{locationCode}")
    public ResponseEntity<?> updateDailyForecast(@PathVariable("locationCode") String locationCode,
            @RequestBody @Valid List<DailyWeatherDTO> listDTO) throws BadRequestException {

        if (listDTO.isEmpty()) {
            throw new BadRequestException("Daily forecast data cannot be empty");
        }

        List<DailyWeather> listDailyWeather = listDTO2ListEntity(listDTO);

        List<DailyWeather> updatedDailyForecast = dailyWeatherService.updateByLocationCode(locationCode, listDailyWeather);
        
        DailyWeatherListDTO listEntity2DTOList = listEntity2DTOList(updatedDailyForecast);

        return ResponseEntity.ok(addLinksByLocationCode(listEntity2DTOList, locationCode));
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

    private List<DailyWeather> listDTO2ListEntity(List<DailyWeatherDTO> listDTO) {
        List<DailyWeather> listEntity = new ArrayList<>();

        listDTO.forEach(dto -> {
            DailyWeather dailyWeather = modelMapper.map(dto, DailyWeather.class);
            listEntity.add(dailyWeather);
        });

        return listEntity;
    }
    
    private EntityModel<DailyWeatherListDTO> addLinksByIP(DailyWeatherListDTO dto) {
    	EntityModel<DailyWeatherListDTO> entityModel = EntityModel.of(dto);
    	
    	entityModel.add(linkTo(methodOn(DailyWeatherApiController.class).listDailyForecastByIPAddress(null))
    			.withSelfRel());
    	
    	entityModel.add(linkTo(methodOn(RealtimeWeatherApiController.class).getRealtimeWeatherByIPAddress(null))
    			.withRel("realtime_weather"));
    	
    	entityModel.add(linkTo(methodOn(HourlyWeatherApiController.class).listHourlyForecastByIPAddress(null))
    			.withRel("hourly_forecast"));
    	
    	entityModel.add(linkTo(methodOn(FullWeatherApiController.class).getFullWeatherByIPAddress(null))
    			.withRel("full_forecast"));
    	
    	return entityModel;
    }
    
    private EntityModel<DailyWeatherListDTO> addLinksByLocationCode(DailyWeatherListDTO dto, String locationCode) {
    	EntityModel<DailyWeatherListDTO> entityModel = EntityModel.of(dto);
    	
    	entityModel.add(linkTo(methodOn(DailyWeatherApiController.class).listDailyForecastByLocationCode(locationCode))
    			.withSelfRel());
    	
    	entityModel.add(linkTo(methodOn(RealtimeWeatherApiController.class).getRealtimeWeatherByLocationCode(locationCode))
    			.withRel("realtime_weather"));
    	
    	entityModel.add(linkTo(methodOn(HourlyWeatherApiController.class).listHourlyForecastByLocationCode(locationCode, null))
    			.withRel("hourly_forecast"));
    	
    	entityModel.add(linkTo(methodOn(FullWeatherApiController.class).getFullWeatherByLocationCode(locationCode))
    			.withRel("full_forecast"));
    	
    	return entityModel;
    }
}
