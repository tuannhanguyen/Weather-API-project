package com.skyapi.weatherforescast.common;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
    private String locationCode;

    private int temperature;
    private int humidity;
    private int precipation;
    private int windSpeed;

    @Column(length = 50)
    private String status;

    private Date lastUpdated;

    @OneToOne
    @JoinColumn(name = "location_code")
    @MapsId
    private Location location;
}
