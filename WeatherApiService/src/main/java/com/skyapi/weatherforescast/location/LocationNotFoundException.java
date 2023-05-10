package com.skyapi.weatherforescast.location;

public class LocationNotFoundException extends RuntimeException {

    public LocationNotFoundException(String locationCode) {
        super("No location found with the give code: " + locationCode);
    }

    public LocationNotFoundException(String countryCode, String cityName) {
        super("No location found with the give country code: " + countryCode + " and city name: " + cityName);
    }

}
