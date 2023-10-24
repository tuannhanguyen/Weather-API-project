package com.skyapi.weatherforescast;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.skyapi.weatherforescast.common.DailyWeather;
import com.skyapi.weatherforescast.common.HourlyWeather;
import com.skyapi.weatherforescast.common.Location;
import com.skyapi.weatherforescast.daily.DailyWeatherDTO;
import com.skyapi.weatherforescast.full.FullWeatherDTO;
import com.skyapi.weatherforescast.hourly.HourlyWeatherDTO;

@SpringBootApplication
public class WeatherApiServiceApplication {

    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        configureMappingForHourlyWeather(mapper);

        configureMappingForDailyWeather(mapper);

        configureMappingForFullWeather(mapper);
        
        return mapper;
    }
    
    @Bean
    public ObjectMapper getObjectMapper() {
    	ObjectMapper objectMapper = new ObjectMapper();
    	
    	objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE); // camel case to snake case
    	objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // formated output JSON
    	
    	return objectMapper;
    }

	private void configureMappingForFullWeather(ModelMapper mapper) {
		mapper.typeMap(Location.class, FullWeatherDTO.class)
        	.addMapping(src -> src.toString(),  FullWeatherDTO::setLocation);
	}

	private void configureMappingForDailyWeather(ModelMapper mapper) {
		mapper.typeMap(DailyWeather.class, DailyWeatherDTO.class)
        	.addMapping(src -> src.getId().getDayOfMonth(), DailyWeatherDTO::setDayOfMonth)
        	.addMapping(src -> src.getId().getMonth(), DailyWeatherDTO::setMonth);

        mapper.typeMap(DailyWeatherDTO.class, DailyWeather.class)
        	.addMapping(src -> src.getDayOfMonth(), (dest, v) -> dest.getId().setDayOfMonth(v != null ? (int) v : 0))
        	.addMapping(src -> src.getMonth(), (dest, v) -> dest.getId().setMonth(v != null ? (int) v : 0));
	}

	private void configureMappingForHourlyWeather(ModelMapper mapper) {
		mapper.typeMap(HourlyWeather.class, HourlyWeatherDTO.class)
        	.addMapping(src -> src.getId().getHourOfDay(), HourlyWeatherDTO::setHourOfDay);

        mapper.typeMap(HourlyWeatherDTO.class, HourlyWeather.class)
        	.addMapping(src -> src.getHourOfDay(), (dest, v) -> dest.getId().setHourOfDay(v != null ? (int) v : 0));
	}

    public static void main(String[] args) {
        SpringApplication.run(WeatherApiServiceApplication.class, args);
    }

}
