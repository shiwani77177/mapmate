package com.mapmate.backend.health;

import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api")
public class PingController {
  private final JdbcTemplate jdbc;

  public PingController(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  @GetMapping("/ping")
  public Map<String, Object> ping() {
    String postgis = jdbc.queryForObject("SELECT postgis_version()", String.class);
    return Map.of("status", "UP", "postgis", postgis);
  }

}