package com.mapmate.backend.driver;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DriverLocationService {

    private final DriverLocationRepository driverLocationRepository;
    private final DriverRepository driverRepository;

    public DriverLocationService(DriverLocationRepository driverLocationRepository,
                                 DriverRepository driverRepository) {
        this.driverLocationRepository = driverLocationRepository;
        this.driverRepository = driverRepository;
    }

    public DriverLocationResponse upsertLocation(Long driverId, LocationUpdateRequest request) {
        if (!driverRepository.existsById(driverId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Driver " + driverId + " does not exist");
        }

        driverLocationRepository.upsertLocation(
                driverId, request.getLat(), request.getLng(), request.getStatus());

        return driverLocationRepository.findByDriverId(driverId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Location write succeeded but could not be read back"));
    }

    public DriverLocationResponse getLocation(Long driverId) {
        return driverLocationRepository.findByDriverId(driverId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No location recorded yet for driver " + driverId));
    }

    public List<NearbyDriverResponse> findNearby(double lat, double lng, double radiusKm) {
        if (radiusKm <= 0 || radiusKm > 50) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "radiusKm must be between 0 and 50");
        }
        double radiusMeters = radiusKm * 1000.0;
        return driverLocationRepository.findNearby(lat, lng, radiusMeters);
    }
}


