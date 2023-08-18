package com.skyapi.weatherforescast.full;

import com.skyapi.weatherforescast.realtime.RealtimeWeatherDTO;

public class RealtimeWeatherFieldFilter {

    @Override
    public boolean equals(Object other) {

        RealtimeWeatherDTO dto = (RealtimeWeatherDTO) other;
        return dto == null;
    }

}
