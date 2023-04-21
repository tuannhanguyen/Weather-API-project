package com.skyapi.weatherforescast.common;

import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "realtime_weather")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RealtimeWeather {
    @Id @Column(name = "location_code")
    @JsonIgnore
    private String locationCode;

    @Range(min = -50, max = 50, message = "Temperature must be in the range of -50 to 50 Celsius degree")
    private int temperature;

    @Range(min = 0, max = 100, message = "Humidity must be in the range of 0 to 100 percentage")
    private int humidity;

    @Range(min = 0, max = 100, message = "Precipation must be in the range of 0 to 100 percentage")
    private int precipation;

    @JsonProperty("wind_speed")
    @Range(min = 0, max = 200, message = "Wind speed must be in the range of 0 to 200 km/h")
    private int windSpeed;

    @Column(length = 50)
    @NotBlank
    @Length(min = 3, max = 50, message = "Status must be in between 3-50 characters")
    private String status;

    @JsonProperty("last_updated")
    @JsonIgnore
    private Date lastUpdated;

    @OneToOne
    @JoinColumn(name = "location_code")
    @MapsId
    @JsonIgnore
    private Location location;


}
