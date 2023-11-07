package com.skyapi.weatherforescast.hourly;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.skyapi.weatherforescast.GeolocationException;
import com.skyapi.weatherforescast.GeolocationService;
import com.skyapi.weatherforescast.common.HourlyWeather;
import com.skyapi.weatherforescast.common.Location;
import com.skyapi.weatherforescast.daily.DailyWeatherApiController;
import com.skyapi.weatherforescast.full.FullWeatherApiController;
import com.skyapi.weatherforescast.realtime.RealtimeWeatherApiController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/hourly")
@Validated
public class HourlyWeatherApiController {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(HourlyWeatherApiController.class);

	@Autowired
	GeolocationService geoLocationService;
	@Autowired
	HourlyWeatherService hourlyWeatherService;
	@Autowired
	ModelMapper modelMapper;

	@GetMapping
	public ResponseEntity<?> listHourlyForecastByIPAddress(HttpServletRequest request) {
		String ipAddress = CommonUtility.getIPAddress(request);

		try {
			int currentHour = Integer.parseInt(request.getHeader("X-Current-Hour"));

			Location location = geoLocationService.getLocation(ipAddress);

			List<HourlyWeather> listHourlyWeather = hourlyWeatherService.getByLocation(location, currentHour);

			if (listHourlyWeather.isEmpty()) {
				return ResponseEntity.noContent().build();
			}

			HourlyWeatherListDTO listEntity2ListDTO = listEntity2ListDTO(listHourlyWeather);

			return ResponseEntity.ok(addLinksByIP(listEntity2ListDTO));
			
		} catch (NumberFormatException | GeolocationException e) {

			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/{locationCode}")
	public ResponseEntity<?> listHourlyForecastByLocationCode(@PathVariable("locationCode") String locationCode,
			HttpServletRequest request) {

		try {
			int currentHour = Integer.parseInt(request.getHeader("X-Current-Hour"));

			List<HourlyWeather> listHourlyWeather = hourlyWeatherService.getByLocationCode(locationCode, currentHour);

			if (listHourlyWeather.isEmpty()) {
				return ResponseEntity.noContent().build();
			}

			HourlyWeatherListDTO listEntity2ListDTO = listEntity2ListDTO(listHourlyWeather);
			
			return ResponseEntity.ok(addLinksByLocationCode(listEntity2ListDTO, locationCode));
			
		} catch (NumberFormatException e) {

			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping("/{locationCode}")
	public ResponseEntity<?> updateHourlyForecast(@PathVariable("locationCode") String locationCode,
			@RequestBody @jakarta.validation.Valid List<HourlyWeatherDTO> listDTO) throws BadRequestException {

		if (listDTO.isEmpty()) {
			throw new BadRequestException("Hourly forecast data cannot be empty");
		}

		List<HourlyWeather> listHourlyWeather = listDTO2ListEntity(listDTO);

		List<HourlyWeather> updatedHourlyWeather = hourlyWeatherService.updateByLocationCode(locationCode,
				listHourlyWeather);

		HourlyWeatherListDTO updatedDto = listEntity2ListDTO(updatedHourlyWeather);

		return ResponseEntity.ok(addLinksByLocationCode(updatedDto, locationCode));
	}

	private List<HourlyWeather> listDTO2ListEntity(List<HourlyWeatherDTO> listDTO) {
		List<HourlyWeather> listEntity = new ArrayList<>();

		listDTO.forEach(dto -> {
			HourlyWeather entity = modelMapper.map(dto, HourlyWeather.class);
			listEntity.add(entity);
		});

		return listEntity;
	}

	private HourlyWeatherListDTO listEntity2ListDTO(List<HourlyWeather> hourlyForecast) {
		Location location = hourlyForecast.get(0).getId().getLocation();

		HourlyWeatherListDTO listDTO = new HourlyWeatherListDTO();
		listDTO.setLocation(location.toString());

		hourlyForecast.forEach(hourlyWeather -> {
			HourlyWeatherDTO dto = modelMapper.map(hourlyWeather, HourlyWeatherDTO.class);
			listDTO.addWeatherHourlyDTO(dto);
		});

		return listDTO;
	}

	private HourlyWeatherListDTO addLinksByIP(HourlyWeatherListDTO dto) {
		dto.add(linkTo(methodOn(HourlyWeatherApiController.class).listHourlyForecastByIPAddress(null))
				.withSelfRel());
		
		dto.add(linkTo(methodOn(RealtimeWeatherApiController.class).getRealtimeWeatherByIPAddress(null))
				.withRel("realtime_weather"));
		
		dto.add(linkTo(methodOn(DailyWeatherApiController.class).listDailyForecastByIPAddress(null))
				.withRel("daily_forecast"));
		
		dto.add(linkTo(methodOn(FullWeatherApiController.class).getFullWeatherByIPAddress(null))
				.withRel("full_forecast"));
		
		return dto;
	}
	
	private HourlyWeatherListDTO addLinksByLocationCode(HourlyWeatherListDTO dto, String locationCode) {
		dto.add(linkTo(methodOn(HourlyWeatherApiController.class).listHourlyForecastByLocationCode(locationCode, null))
				.withSelfRel());
		
		dto.add(linkTo(methodOn(RealtimeWeatherApiController.class).getRealtimeWeatherByLocationCode(locationCode))
				.withRel("realtime_weather"));
		
		dto.add(linkTo(methodOn(DailyWeatherApiController.class).listDailyForecastByLocationCode(locationCode))
				.withRel("daily_forecast"));
		
		dto.add(linkTo(methodOn(FullWeatherApiController.class).getFullWeatherByLocationCode(locationCode))
				.withRel("full_forecast"));
		
		return dto;
	}
}
