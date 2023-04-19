package com.skyapi.weatherforescast;

import java.io.IOException;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;
import com.skyapi.weatherforescast.common.Location;

@Service
public class GeolocationService {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(GeolocationService.class);

    private String DBPath = "ip2locdb/IP2LOCATION-LITE-DB3.BIN";
    private IP2Location ipLocator = new IP2Location();

    public GeolocationService() {
        try {
            ipLocator.Open(DBPath);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public Location getLocation(String ipAddress) throws GeolocationException {
        try {
            IPResult result = ipLocator.IPQuery(ipAddress);

            if (!result.getStatus().equals("OK")) {
                throw new GeolocationException("Geolocation failed with status: " + result.getStatus());
            }

            return new Location(result.getCity(), result.getRegion(), result.getCountryLong(), result.getCountryShort());
        } catch (IOException e) {
            throw new GeolocationException("Error querying IP database", e);
        }
    }


}
