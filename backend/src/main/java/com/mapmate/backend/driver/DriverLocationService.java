package com.mapmate.backend.driver;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DriverLocationService {

    private final DriverLocationRepository driverLocationRepository;
    private final DriverRepository driverRepository;

    public DriverLocationService(DriverLocationRepository driverLocationRepository, DriverRepository driverRepository) {
        this.driverLocationRepository = driverLocationRepository;
        this.driverRepository = driverRepository;
    }

    public DriverLocationResponse upsertLocation(Long driverId, LocationUpdateRequest request) {
        if (!driverRepository.existsById(driverId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver " + driverId + " does not exist");
        }

        driverLocationRepository.upsertLocation(driverId, request.getLat(), request.getLng(), request.getStatus());

        return driverLocationRepository.findByDriverId(driverId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "Location write succeeded but could not be read back"));
    }

    public DriverLocationResponse getLocation(Long driverId) {
        return driverLocationRepository.findByDriverId(driverId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No location recorded yet for driver " + driverId));
    }
}

