CREATE TABLE trips (
    id              BIGSERIAL PRIMARY KEY,
    rider_id        BIGINT NOT NULL REFERENCES users (id),
    driver_id       BIGINT REFERENCES drivers (id),
    status          VARCHAR(30) NOT NULL DEFAULT 'REQUESTED'
                    CHECK (status IN ('REQUESTED', 'OFFERED', 'ACCEPTED', 'IN_PROGRESS',
                                      'COMPLETED', 'CANCELLED', 'NO_DRIVERS_AVAILABLE')),
    pickup_lat      DOUBLE PRECISION NOT NULL,
    pickup_lng      DOUBLE PRECISION NOT NULL,
    dropoff_lat     DOUBLE PRECISION NOT NULL,
    dropoff_lng     DOUBLE PRECISION NOT NULL,
    estimated_fare  NUMERIC(10, 2),
    final_fare      NUMERIC(10, 2),
    idempotency_key VARCHAR(64),
    requested_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    accepted_at     TIMESTAMPTZ,
    started_at      TIMESTAMPTZ,
    completed_at    TIMESTAMPTZ,
    cancelled_at    TIMESTAMPTZ,
    CONSTRAINT uq_trips_rider_idempotency UNIQUE (rider_id, idempotency_key)
);

CREATE INDEX idx_trips_rider ON trips (rider_id, requested_at DESC);
CREATE INDEX idx_trips_driver_status ON trips (driver_id, status);


