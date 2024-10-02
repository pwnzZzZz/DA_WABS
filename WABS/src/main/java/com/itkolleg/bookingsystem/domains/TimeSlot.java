package com.itkolleg.bookingsystem.domains;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;

/**
 * Represents a time slot within the system.
 *
 * <p>
 * A time slot is a predefined period during which a booking can be made. This class provides predefined time slots
 * representing typical booking periods: morning (AM), afternoon (PM), and full day (ALL_DAY).
 * </p>
 *
 * <p>
 * Each time slot has a start time, an end time, and a name. The start and end times define the duration of the time slot.
 * The name provides a short description or identifier for the time slot (e.g., "AM", "PM", "ALL_DAY").
 * </p>
 *
 * <p>
 * The {@code @Entity} annotation indicates that instances of this class will be stored in the database.
 * </p>
 *
 * Note:
 * - The {@code @NoArgsConstructor} and {@code @AllArgsConstructor} annotations provide no-argument and all-argument constructors, respectively.
 * - The {@code @Getter}, {@code @Setter}, and {@code @ToString} annotations from Lombok generate getter, setter, and toString methods for the class attributes.
 * - The {@code @GeneratedValue} annotation ensures that a unique identifier is generated for each time slot when it's persisted in the database.
 *
 * @author Sonja Lechner
 * @version 1.0
 * @since 2023-05-24
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Slf4j
public class Timeslot {

    /**
     * The unique identifier for the time slot.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /**
     * The start time of the time slot.
     */
    private LocalTime startTime;

    /**
     * The end time of the time slot.
     */
    private LocalTime endTime;

    /**
     * The name or description of the time slot.
     */
    private String name;

    /**
     * Constructor to create a Timeslot.
     *
     * @param startTime The start time of the slot.
     * @param endTime The end time of the slot.
     * @param name The name of the slot.
     */
    public Timeslot(LocalTime startTime, LocalTime endTime, String name) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
    }

    /**
     * Get the start time of the time slot as a string.
     *
     * @return The start time as a string.
     */
    public String getStartTimeAsString() {
        return this.startTime.toString();
    }

    /**
     * Get the end time of the time slot as a string.
     *
     * @return The end time as a string.
     */
    public String getEndTimeAsString() {
        return this.endTime.toString();
    }

    /**
     * Get the start time of the time slot.
     *
     * @return The start time of the time slot.
     */
    public LocalTime getStart() { return this.startTime;
    }

    /**
     * Get the end time of the time slot.
     *
     * @return The end time of the time slot.
     */
    public LocalTime getEnd() { return this.endTime;
    }
}