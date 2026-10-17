package com.mapmate.backend.driver;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class DriverLocationResponse {
  private final Long driverId;
  private final Double lat;
  private final Double lng;
  private final DriverStatus status;
  private final Instant LastSeen;
}

