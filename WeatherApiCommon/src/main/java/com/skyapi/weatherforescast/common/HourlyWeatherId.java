package com.skyapi.weatherforescast.common;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Embeddable
public class HourlyWeatherId implements Serializable {

    private int hourOfDay;

    @ManyToOne
    @JoinColumn(name = "location_code")
    private Location location;
}
