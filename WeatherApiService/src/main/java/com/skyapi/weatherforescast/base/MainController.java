package com.skyapi.weatherforescast.base;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforescast.daily.DailyWeatherApiController;
import com.skyapi.weatherforescast.full.FullWeatherApiController;
import com.skyapi.weatherforescast.hourly.HourlyWeatherApiController;
import com.skyapi.weatherforescast.location.LocationApiController;
import com.skyapi.weatherforescast.realtime.RealtimeWeatherApiController;

@RestController
public class MainController {
	
	@GetMapping("/")
	public ResponseEntity<RootEntity> handleBaseURL() {
		return ResponseEntity.ok(createRootEntity());
	}
	
	private RootEntity createRootEntity() {
		RootEntity rootEntity = new RootEntity();
		
		String locationsUrl = linkTo(methodOn(LocationApiController.class).listLocations()).toString();
		rootEntity.setLocationsUrl(locationsUrl);
		
		String locationByCodeUrl = linkTo(methodOn(LocationApiController.class).getLocation(null)).toString();
		rootEntity.setLocationByCodeUrl(locationByCodeUrl);
		
		String realtimeWeatherByIpUrl = linkTo(methodOn(RealtimeWeatherApiController.class).getRealtimeWeatherByIPAddress(null)).toString();
		rootEntity.setRealtimeWeatherByIpUrl(realtimeWeatherByIpUrl);
		
		String realtimeWeatherByCodeUrl = linkTo(methodOn(RealtimeWeatherApiController.class).getRealtimeWeatherByLocationCode(null, null)).toString();
		rootEntity.setRealtimeWeatherByCodeUrl(realtimeWeatherByCodeUrl);
		
		String hourlyWeatherByIpUrl = linkTo(methodOn(HourlyWeatherApiController.class).listHourlyForecastByIPAddress(null)).toString();
		rootEntity.setHourlyForecastByIpUrl(hourlyWeatherByIpUrl);
		
		String hourlyWeatherByCodeUrl = linkTo(methodOn(HourlyWeatherApiController.class).listHourlyForecastByLocationCode(null, null)).toString();
		rootEntity.setHourlyForecastByCodeUrl(hourlyWeatherByCodeUrl);
		
		String dailyWeatherByIpUrl = linkTo(methodOn(DailyWeatherApiController.class).listDailyForecastByIPAddress(null)).toString();
		rootEntity.setDailyForecastByIpUrl(dailyWeatherByIpUrl);
		
		String dailyWeatherByCodeUrl = linkTo(methodOn(DailyWeatherApiController.class).listDailyForecastByLocationCode(null)).toString();
		rootEntity.setDailyForecastByCodeUrl(dailyWeatherByCodeUrl);
		
		String fullWeatherByIpUrl = linkTo(methodOn(FullWeatherApiController.class).getFullWeatherByIPAddress(null)).toString();
		rootEntity.setFullWeatherByIpUrl(fullWeatherByIpUrl);
		
		String fullWeatherByCodeUrl = linkTo(methodOn(FullWeatherApiController.class).getFullWeatherByLocationCode(null)).toString();
		rootEntity.setFullWeatherByCodeUrl(fullWeatherByCodeUrl);
		
		return rootEntity;
	}

}
