package com.skyapi.weatherforescast.location;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforescast.common.Location;

@RestController
@RequestMapping("/v1/locations")
public class LocationApiController {

    @Autowired
    private LocationService service;
    @Autowired ModelMapper mapper;

//    public LocationApiController(LocationService service) {
//        super();
//        this.service = service;
//    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody @Valid LocationDTO dto) {
        Location addedLocation = service.add(dto2Entity(dto));
        URI uri = URI.create("/v1/locations/" + addedLocation.getCode());

        return ResponseEntity.created(uri).body(entity2DTO(addedLocation));
    }

    @GetMapping
    public ResponseEntity<?> listLocations() {
        List<Location> locations = service.findUntrashed();

        if (locations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(listEntity2ListDTO(locations));
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getLocation(@PathVariable("code") String code) {
        Location location = service.get(code);

        if (location == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(entity2DTO(location));
    }

    @PutMapping
    public ResponseEntity<?> updateLocation(@RequestBody @Valid LocationDTO dto) {
        try {
            Location locationUpdated = service.update(dto2Entity(dto));

            return ResponseEntity.ok(entity2DTO(locationUpdated));
        } catch (LocationNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteLocation(@PathVariable("code") String code) {
        try {
            service.delete(code);

            return ResponseEntity.noContent().build();
        } catch (LocationNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private LocationDTO entity2DTO(Location location) {
        return mapper.map(location, LocationDTO.class);
    }

    private Location dto2Entity(LocationDTO dto) {
        return mapper.map(dto, Location.class);
    }

    private List<LocationDTO> listEntity2ListDTO(List<Location> listLocation) {
        List<LocationDTO> listDTO = new ArrayList<>();

        listLocation.forEach(entity -> {
            LocationDTO dto = mapper.map(entity, LocationDTO.class);
            listDTO.add(dto);
        });

        return listDTO;
    }

}
