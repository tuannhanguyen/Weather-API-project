package com.skyapi.weatherforescast.hourly;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonPropertyOrder("hour_of_day")
public class HourlyWeatherDTO {
    @JsonProperty("hour_of_day")
    private int hourOfDay;

    @Range(min = -50, max = 50, message = "Temperature must be in the range of -50 to 50 Celsius degree")
    private int temperature;

    @Range(min = 0, max = 100, message = "Precipation must be in the range of 0 to 100 percentage")
    private int precipitation;

    @NotBlank
    @Length(min = 3, max = 50, message = "Status must be in between 3-50 characters")
    private String status;
}
