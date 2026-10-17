package com.mapmate.backend.driver;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NearbyDriverResponse {
  private final Long driverId;
    private final Double lat;
    private final Double lng;
    private final DriverStatus status;
    private final Double distanceMeters;
}


