package com.mapmate.backend.trip;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {
  Optional<Trip> findByRiderIdAndIdempotencyKey(Long riderId, String idempotencyKey);

    List<Trip> findByRiderIdOrderByRequestedAtDesc(Long riderId);

    List<Trip> findByDriverIdAndStatus(Long driverId, TripStatus status);
}

