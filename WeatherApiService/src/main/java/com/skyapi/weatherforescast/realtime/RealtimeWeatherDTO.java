package com.skyapi.weatherforescast.realtime;

import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RealtimeWeatherDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String location;

    @Range(min = -50, max = 50, message = "Temperature must be in the range of -50 to 50 Celsius degree")
    private int temperature;

    @Range(min = 0, max = 100, message = "Humidity must be in the range of 0 to 100 percentage")
    private int humidity;

    @Range(min = -50, max = 50, message = "Precipitation must be in the range of 0 to 100 Celsius degree")
    private int precipation;

    @JsonProperty("wind_speed")
    @Range(min = 0, max = 200, message = "Wind speed must be in the range of 0 to 200 Celsius km/h")
    private int windSpeed;

    @NotBlank(message = "Status must not be empty")
    @Length(min = 3, max = 50, message = "Status must be in betwwen 3-50 characters")
    private String status;

    @JsonProperty("last_updated")
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date lastUpdated;

}
