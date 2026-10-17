package com.mapmate.backend.driver;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@Validated
public class DriverLocationController {

    private final DriverLocationService service;

    public DriverLocationController(DriverLocationService service) {
        this.service = service;
    }

    @PutMapping("/{driverId}/location")
    public ResponseEntity<DriverLocationResponse> updateLocation(
            @PathVariable Long driverId,
            @Valid @RequestBody LocationUpdateRequest request) {
        return ResponseEntity.ok(service.upsertLocation(driverId, request));
    }

    @GetMapping("/{driverId}/location")
    public ResponseEntity<DriverLocationResponse> getLocation(
            @PathVariable Long driverId) {
        return ResponseEntity.ok(service.getLocation(driverId));
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<NearbyDriverResponse>> findNearby(
            @RequestParam @NotNull @DecimalMin("-90.0") @DecimalMax("90.0") Double lat,
            @RequestParam @NotNull @DecimalMin("-180.0") @DecimalMax("180.0") Double lng,
            @RequestParam(defaultValue = "5.0") @Positive Double radiusKm) {
        return ResponseEntity.ok(service.findNearby(lat, lng, radiusKm));
    }
}


