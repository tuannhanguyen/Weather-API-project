package com.skyapi.weatherforescast.location;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skyapi.weatherforescast.common.Location;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LocationService {

    @Autowired
    private LocationRepository repo;

    public Location add(Location location) {
        return repo.save(location);
    }

    public List<Location> findUntrashed() {
        return repo.findUntrashed();
    }

    public Location get(String code) {
        Location location = repo.findByCode(code);

        if (location == null) {
            throw new LocationNotFoundException(code);
        }

        return location;
    }

    public Location update(Location locationInRequest) {
        String code = locationInRequest.getCode();

        Location locationInDb = repo.findByCode(code);

        if (locationInDb == null) {
            throw new LocationNotFoundException(code);
        }

        locationInDb.setCityName(locationInRequest.getCityName());
        locationInDb.setCountryCode(locationInRequest.getCountryCode());
        locationInDb.setCountryName(locationInRequest.getCountryName());
        locationInDb.setRegionName(locationInRequest.getRegionName());
        locationInDb.setEnabled(locationInRequest.isEnabled());

        return repo.save(locationInDb);
    }

    public void delete(String code) throws LocationNotFoundException {
        if (!repo.existsById(code)) {
            throw new LocationNotFoundException(code);
        }

        repo.trashByCode(code);
    }

}
