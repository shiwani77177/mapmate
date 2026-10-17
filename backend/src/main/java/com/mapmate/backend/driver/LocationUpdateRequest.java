package com.mapmate.backend.driver;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationUpdateRequest {
  
  @NotNull(message = "lat is required")
  @DecimalMin(value = "-90.0", message = "lat must be >= -90")
  @DecimalMax(value = "90.0", message = "lat must be <= 90")
  private Double Lat;

  @NotNull(message = "lng is required")
  @DecimalMin(value = "-180.0", message = "lng must be >= -180")
  @DecimalMax(value = "180.0", message = "lng must be <= 180")
  private Double Lng;

  @NotNull(message = "status is required")
  private DriverStatus status;
}

