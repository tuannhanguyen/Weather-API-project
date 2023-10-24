package com.skyapi.weatherforescast.base;

import lombok.Data;

@Data
public class RootEntity {
	
// @JsonProperty replaced by PropertyNamingStrategy.SNAKE_CASE in configuration
	
//	@JsonProperty("locations_url")
	private String locationsUrl;
	
//	@JsonProperty("location_by_code_url")
	private String locationByCodeUrl;
	
//	@JsonProperty("realtime_weather_by_ip_url")
	private String realtimeWeatherByIpUrl;
	
//	@JsonProperty("realtime_weather_by_code_url")
	private String realtimeWeatherByCodeUrl;
	
//	@JsonProperty("hourly_forecast_by_ip_url")
	private String hourlyForecastByIpUrl;
	
//	@JsonProperty("hourly_forecast_by_code_url")
	private String hourlyForecastByCodeUrl;
	
//	@JsonProperty("daily_forecast_by_ip_url")
	private String dailyForecastByIpUrl;
	
//	@JsonProperty("daily_forecast_by_code_url")
	private String dailyForecastByCodeUrl;
	
//	@JsonProperty("full_weather_by_ip_url")
	private String fullWeatherByIpUrl;
	
//	@JsonProperty("full_weather_by_code_url")
	private String fullWeatherByCodeUrl;
	
	
}
