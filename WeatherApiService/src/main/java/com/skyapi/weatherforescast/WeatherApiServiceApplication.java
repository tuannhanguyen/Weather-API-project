package com.skyapi.weatherforescast;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.skyapi.weatherforescast.common.DailyWeather;
import com.skyapi.weatherforescast.common.HourlyWeather;
import com.skyapi.weatherforescast.daily.DailyWeatherDTO;
import com.skyapi.weatherforescast.hourly.HourlyWeatherDTO;

@SpringBootApplication
public class WeatherApiServiceApplication {

    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        var typeMap1 = mapper.typeMap(HourlyWeather.class, HourlyWeatherDTO.class);
        typeMap1.addMapping(src -> src.getId().getHourOfDay(), HourlyWeatherDTO::setHourOfDay);

        var typeMap2 = mapper.typeMap(HourlyWeatherDTO.class, HourlyWeather.class);
        typeMap2.addMapping(src -> src.getHourOfDay(), (dest, v) -> dest.getId().setHourOfDay(v != null ? (int) v : 0));

        var typeMap3 = mapper.typeMap(DailyWeather.class, DailyWeatherDTO.class);
        typeMap3.addMapping(src -> src.getId().getDayOfMonth(), DailyWeatherDTO::setDayOfMonth);
        typeMap3.addMapping(src -> src.getId().getMonth(), DailyWeatherDTO::setMonth);

        return mapper;
    }

    public static void main(String[] args) {
        SpringApplication.run(WeatherApiServiceApplication.class, args);
    }

}
