package com.mapmate.backend.trip;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "trips")
@Getter
@Setter
@NoArgsConstructor
public class Trip {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "rider_id", nullable = false)
  private Long riderId;

  @Column(name = "driver_id")
  private Long driverId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TripStatus status = TripStatus.REQUESTED;

  @Column(name = "pickup_lat", nullable = false)
  private double pickupLat;

  @Column(name = "pickup_lng", nullable = false)
  private double pickupLng;

  @Column(name = "dropoff_lat", nullable = false)
  private double dropoffLat;

  @Column(name = "dropoff_lng", nullable = false)
  private double dropoffLng;

  @Column(name = "estimated_fare")
  private BigDecimal estimatedFare;

  @Column(name = "final_fare")
  private BigDecimal finalFare;

  @Column(name = "idempotency_key")
  private String idempotencyKey;

  @CreationTimestamp
  @Column(name = "requested_at", nullable = false, updatable = false)
  private Instant requestedAt;

  @Column(name = "accepted_at")
  private Instant acceptedAt;

  @Column(name = "completed_at")
  private Instant completedAt;

  @Column(name = "cancelled_at")
  private Instant cancelledAt;
}


