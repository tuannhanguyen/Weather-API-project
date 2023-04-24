package com.skyapi.weatherforescast.hourly;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.skyapi.weatherforescast.common.HourlyWeather;
import com.skyapi.weatherforescast.common.HourlyWeatherId;
import com.skyapi.weatherforescast.common.Location;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class HourlyWeatherRepositoryTests {

    @Autowired private HourlyWeatherRepository repo;

    @Test
    public void testAdd() {
        String locationCode = "HCM_VN";
        int hourOfDay = 11;

        Location location = new Location();
        location.setCode(locationCode);

        HourlyWeatherId id = new HourlyWeatherId();
        id.setLocation(location);
        id.setHourOfDay(hourOfDay);

        HourlyWeather forecast = new HourlyWeather();

        forecast.setId(id);
        forecast.setTemperature(16);
        forecast.setPrecipitation(50);
        forecast.setStatus("Sunny");

        HourlyWeather updatedHourlyWeather = repo.save(forecast);

        assertThat(updatedHourlyWeather.getId().getLocation().getCode()).isEqualTo(locationCode);
        assertThat(updatedHourlyWeather.getId().getHourOfDay()).isEqualTo(hourOfDay);
    }

    @Test
    public void testFindByLocatinCode() {
        String locationCode = "HCM_VN";
        int currentHour = 11;
        List<HourlyWeather> forecast = repo.findByLocationCode(locationCode, currentHour);

        for (HourlyWeather hourlyWeather : forecast) {
            System.out.println(hourlyWeather);
        }

        assertThat(forecast).isNotEmpty();
    }
}
