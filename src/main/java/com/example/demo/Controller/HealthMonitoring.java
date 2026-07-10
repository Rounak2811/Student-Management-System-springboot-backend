package com.example.demo.Controller; // Make sure this matches your actual package path

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class HealthMonitoring {
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/health")
    public String keepAwake() {
        // This forces Spring Boot to send an active query to Aiven
        jdbcTemplate.execute("SELECT 1");
        return "Backend and Database are completely awake!";
    }
}