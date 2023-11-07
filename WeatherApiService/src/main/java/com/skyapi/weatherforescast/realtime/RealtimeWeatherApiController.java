package com.skyapi.weatherforescast.realtime;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforescast.CommonUtility;
import com.skyapi.weatherforescast.GeolocationException;
import com.skyapi.weatherforescast.GeolocationService;
import com.skyapi.weatherforescast.common.Location;
import com.skyapi.weatherforescast.common.RealtimeWeather;
import com.skyapi.weatherforescast.daily.DailyWeatherApiController;
import com.skyapi.weatherforescast.full.FullWeatherApiController;
import com.skyapi.weatherforescast.hourly.HourlyWeatherApiController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/realtime")
@Validated
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

			return ResponseEntity.ok(addLinksByIP(dto));
		} catch (GeolocationException e) {
			LOGGER.error(e.getMessage(), e);

			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/{locationCode}")
	public ResponseEntity<?> getRealtimeWeatherByLocationCode(@PathVariable("locationCode") String locationCode) {
		RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocationCode(locationCode);
		RealtimeWeatherDTO dto = entity2DTO(realtimeWeather);

		return ResponseEntity.ok(addLinksByLocationCode(dto, locationCode));
	}

	@PutMapping("/{locationCode}")
	public ResponseEntity<?> updateRealtimeWeather(@PathVariable("locationCode") String locationCode,
			@RequestBody @Valid RealtimeWeatherDTO dto) {

		RealtimeWeather realtimeWeather = dto2Entity(dto);
		RealtimeWeather updatedRealtimeWeather = realtimeWeatherService.update(locationCode, realtimeWeather);
		
		RealtimeWeatherDTO entity2dto = entity2DTO(updatedRealtimeWeather);

		return ResponseEntity.ok(addLinksByLocationCode(entity2dto, locationCode));
	}

	private RealtimeWeatherDTO entity2DTO(RealtimeWeather realtimeWeather) {
		return modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);
	}

	private RealtimeWeather dto2Entity(RealtimeWeatherDTO dto) {
		return modelMapper.map(dto, RealtimeWeather.class);
	}

	private RealtimeWeatherDTO addLinksByIP(RealtimeWeatherDTO dto) {
		
		dto.add(linkTo(methodOn(RealtimeWeatherApiController.class).getRealtimeWeatherByIPAddress(null))
				.withSelfRel());
		
		dto.add(linkTo(methodOn(HourlyWeatherApiController.class).listHourlyForecastByIPAddress(null))
				.withRel("hourly_weather"));
		
		dto.add(linkTo(methodOn(DailyWeatherApiController.class).listDailyForecastByIPAddress(null))
				.withRel("daily_weather"));

		dto.add(linkTo(methodOn(FullWeatherApiController.class).getFullWeatherByIPAddress(null))
				.withRel("full_weather"));

		return dto;
	}
	
	private RealtimeWeatherDTO addLinksByLocationCode(RealtimeWeatherDTO dto, String locationCode) {
		
		dto.add(linkTo(methodOn(RealtimeWeatherApiController.class).getRealtimeWeatherByLocationCode(locationCode))
				.withSelfRel());
		
		dto.add(linkTo(methodOn(HourlyWeatherApiController.class).listHourlyForecastByLocationCode(locationCode, null))
				.withRel("hourly_weather"));
		
		dto.add(linkTo(methodOn(DailyWeatherApiController.class).listDailyForecastByLocationCode(locationCode))
				.withRel("daily_weather"));

		dto.add(linkTo(methodOn(FullWeatherApiController.class).getFullWeatherByLocationCode(locationCode))
				.withRel("full_weather"));

		return dto;
	}

}
