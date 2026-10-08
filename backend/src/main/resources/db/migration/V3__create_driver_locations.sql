CREATE TABLE driver_locations (
    driver_id BIGINT PRIMARY KEY REFERENCES drivers (id) ON DELETE CASCADE,
    location  geography(Point, 4326) NOT NULL,
    status    VARCHAR(20) NOT NULL DEFAULT 'OFFLINE' CHECK (status IN ('OFFLINE', 'AVAILABLE', 'OFFERED', 'ON_TRIP')),
    last_seen TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_driver_locations_location ON driver_locations USING GIST (location);

CREATE INDEX idx_driver_locations_status_seen ON driver_locations (status, last_seen);

