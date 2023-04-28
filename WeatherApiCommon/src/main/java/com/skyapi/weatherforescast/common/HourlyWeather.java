package com.skyapi.weatherforescast.common;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "weather_hourly")
@Data
public class HourlyWeather {

    @EmbeddedId
    private HourlyWeatherId id = new HourlyWeatherId();

    private int temperature;
    private int precipitation;

    @Column(length = 50)
    private String status;

    @Override
    public String toString() {
        return "HourlyWeather [hourOfDay=" + id.getHourOfDay() + ", temperature=" + temperature + ", precipitation=" + precipitation
                + ", status=" + status + "]";
    }
    
    public HourlyWeather getShallowCopy() {
    	HourlyWeather copy = new HourlyWeather();
    	copy.setId(this.getId());
    	
    	return copy;
    }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HourlyWeather other = (HourlyWeather) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

}
