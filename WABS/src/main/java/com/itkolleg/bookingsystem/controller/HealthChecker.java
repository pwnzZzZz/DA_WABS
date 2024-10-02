package com.itkolleg.bookingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
/**
 * This class is a custom health checker for the booking system.
 * It implements the HealthIndicator interface from Spring Boot Actuator
 * and checks the health of the data source by establishing a connection.
 *
 * @author Sonja Lechner
 * @version 1.0
 * @since 2023-05-24
 */
@Component
public class HealthChecker implements HealthIndicator {

    @Autowired
    private DataSource dataSource;

    /**
     *
     * Checks the health of the data source by establishing a connection.
     * @return The health status of the data source.
     */
    @Override
    public Health health() {
        try {
            Connection connection = dataSource.getConnection();
            connection.close();
            return Health.up().build();
        } catch (SQLException e) {
            return Health.down().withDetail("Error", e.getMessage()).build();
        }
    }
}