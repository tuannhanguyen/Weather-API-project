package com.skyapi.weatherforescast.location;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
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
import com.skyapi.weatherforescast.location.LocationRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class LocationRepositoryTest {

    @Autowired private LocationRepository repo;

    @Test
    public void testAddHourlyWetherData() throws IOException {
        Location location = repo.findByCode("NYC_USA");

        List<HourlyWeather> listHourlyWeather = location.getListHourlyWeather();

        HourlyWeather forecast1 = new HourlyWeather();
        HourlyWeatherId id1 = new HourlyWeatherId();
        id1.setHourOfDay(8);
        id1.setLocation(location);
        forecast1.setId(id1);
        forecast1.setTemperature(20);
        forecast1.setPrecipitation(60);
        forecast1.setStatus("Cloudy");

        HourlyWeather forecast2 = new HourlyWeather();
        HourlyWeatherId id2 = new HourlyWeatherId();
        id2.setHourOfDay(9);
        id2.setLocation(location);
        forecast2.setId(id2);
        forecast2.setTemperature(20);
        forecast2.setPrecipitation(60);
        forecast2.setStatus("Cloudy");

        listHourlyWeather.add(forecast1);
        listHourlyWeather.add(forecast2);

        Location updatedLocation = repo.save(location);

        assertThat(updatedLocation.getListHourlyWeather()).isNotEmpty();
    }

}
