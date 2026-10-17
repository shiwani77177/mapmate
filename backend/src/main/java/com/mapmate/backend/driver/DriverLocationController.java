package com.mapmate.backend.driver;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/drivers/{driverId}/location")
public class DriverLocationController {
   private final DriverLocationService service;

    public DriverLocationController(DriverLocationService service) {
        this.service = service;
    }

    @PutMapping
    public ResponseEntity<DriverLocationResponse> updateLocation(
            @PathVariable Long driverId,
            @Valid @RequestBody LocationUpdateRequest request) {
        return ResponseEntity.ok(service.upsertLocation(driverId, request));
    }

    @GetMapping
    public ResponseEntity<DriverLocationResponse> getLocation(@PathVariable Long driverId) {
        return ResponseEntity.ok(service.getLocation(driverId));
    }
}


