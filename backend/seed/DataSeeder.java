package com.mapmate.backend.seed;

import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("seed")   // only runs when you start with --spring.profiles.active=seed
public class DataSeeder implements CommandLineRunner {

    private final JdbcTemplate jdbc;
    private static final int N = 10_000;
    private static final double CENTRE_LAT = 19.0760;
    private static final double CENTRE_LNG = 72.8777;
    private static final double SPREAD = 0.27;

    public DataSeeder(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void run(String... args) {
        Random rnd = new Random();
        String[] statuses = {"AVAILABLE","AVAILABLE","AVAILABLE","AVAILABLE",
                             "AVAILABLE","AVAILABLE","AVAILABLE","AVAILABLE",
                             "OFFLINE","ON_TRIP"};

        System.out.println("Seeding " + N + " drivers...");

        for (int i = 1; i <= N; i++) {
            // Insert user
            jdbc.update(
                "INSERT INTO users (email, password_hash, full_name, phone, role) " +
                "VALUES (?,?,?,?,?) ON CONFLICT DO NOTHING",
                "driver" + i + "@seed.com", "seeded", "Driver " + i,
                "+91" + (9000000000L + i), "DRIVER"
            );

            // Get the user id
            Long uid = jdbc.queryForObject(
                "SELECT id FROM users WHERE email = ?",
                Long.class, "driver" + i + "@seed.com"
            );

            // Insert driver
            jdbc.update(
                "INSERT INTO drivers (id, vehicle_model, vehicle_plate) " +
                "VALUES (?,?,?) ON CONFLICT DO NOTHING",
                uid, "Model-" + i, String.format("MH%05d", i)
            );

            // Insert location
            double lat = CENTRE_LAT + (rnd.nextDouble() * 2 - 1) * SPREAD;
            double lng = CENTRE_LNG + (rnd.nextDouble() * 2 - 1) * SPREAD;
            String status = statuses[rnd.nextInt(statuses.length)];

            jdbc.update(
                "INSERT INTO driver_locations (driver_id, location, status, last_seen) " +
                "VALUES (?, ST_SetSRID(ST_MakePoint(?,?),4326)::geography, ?, now()) " +
                "ON CONFLICT (driver_id) DO UPDATE SET " +
                "location=EXCLUDED.location, status=EXCLUDED.status, last_seen=EXCLUDED.last_seen",
                uid, lng, lat, status
            );

            if (i % 1000 == 0) System.out.println("  Inserted " + i + "...");
        }

        System.out.println("Seeding complete.");
    }
}


