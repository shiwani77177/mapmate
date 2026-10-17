package com.mapmate.backend.driver;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * driver_locations has no JPA entity on purpose: the `geography` column type fights
 * Hibernate, so this table is owned entirely by hand-written SQL via JdbcTemplate.
 */
@Repository
public class DriverLocationRepository {

    private final JdbcTemplate jdbc;

    public DriverLocationRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void upsertLocation(Long driverId, double lat, double lng, DriverStatus status) {
        String sql = """
            INSERT INTO driver_locations (driver_id, location, status, last_seen)
            VALUES (?, ST_SetSRID(ST_MakePoint(?, ?), 4326)::geography, ?, now())
            ON CONFLICT (driver_id) DO UPDATE SET
                location  = EXCLUDED.location,
                status    = EXCLUDED.status,
                last_seen = EXCLUDED.last_seen
            """;
        jdbc.update(sql, driverId, lng, lat, status.name());
    }

    public Optional<DriverLocationResponse> findByDriverId(Long driverId) {
        String sql = """
            SELECT driver_id,
                   ST_Y(location::geometry) AS lat,
                   ST_X(location::geometry) AS lng,
                   status,
                   last_seen
            FROM driver_locations
            WHERE driver_id = ?
            """;
        List<DriverLocationResponse> rows = jdbc.query(sql, (rs, rowNum) -> new DriverLocationResponse(
                rs.getLong("driver_id"),
                rs.getDouble("lat"),
                rs.getDouble("lng"),
                DriverStatus.valueOf(rs.getString("status")),
                rs.getTimestamp("last_seen").toInstant()
        ), driverId);

        return rows.stream().findFirst();
    }

    /**
     * Find all AVAILABLE drivers within radiusMeters of (lat, lng),
     * ordered by nearest first.
     *
     * ST_DWithin on a geography column uses the GiST index automatically.
     * ST_Distance returns metres for geography types.
     */
    public List<NearbyDriverResponse> findNearby(double lat, double lng, double radiusMeters) {
        String sql = """
            SELECT driver_id,
                   ST_Y(location::geometry)                          AS lat,
                   ST_X(location::geometry)                          AS lng,
                   status,
                   ST_Distance(location,
                       ST_SetSRID(ST_MakePoint(?, ?), 4326)::geography) AS distance_meters
            FROM driver_locations
            WHERE status = 'AVAILABLE'
              AND ST_DWithin(location,
                      ST_SetSRID(ST_MakePoint(?, ?), 4326)::geography,
                      ?)
            ORDER BY distance_meters
            LIMIT 50
            """;
        // params: lng,lat for distance point; lng,lat for DWithin point; radiusMeters
        return jdbc.query(sql, (rs, rowNum) -> new NearbyDriverResponse(
                rs.getLong("driver_id"),
                rs.getDouble("lat"),
                rs.getDouble("lng"),
                DriverStatus.valueOf(rs.getString("status")),
                rs.getDouble("distance_meters")
        ), lng, lat, lng, lat, radiusMeters);
    }
}


