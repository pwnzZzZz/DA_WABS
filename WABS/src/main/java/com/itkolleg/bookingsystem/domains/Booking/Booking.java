package com.itkolleg.bookingsystem.domains.booking;

import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.Timeslot;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Represents a booking within the booking system.
 * This class serves as the foundational entity for all types of bookings in the system. It captures essential details
 * such as the associated employee, date, time slot, and timestamps for creation and updates.
 * Features:
 * - Validation: Ensures that the booking date is not in the past.
 * - Auditing: Uses JPA's auditing capabilities to automatically set timestamps for creation and updates.
 * - Flexibility: Supports creating bookings with specific start and end times or based on predefined time slots.
 * Note:
 * - The `@Slf4j` annotation provides logging capabilities.
 * - The `@EntityListeners(AuditingEntityListener.class)` annotation enables JPA's auditing features.
 * Important:
 * - Always ensure that the associated resources (like desks or rooms) are available for the specified time slot before creating a booking.
 * @author Sonja Lechner
 * @version 1.3
 * @since 2023-08-24
 */
@Entity
@Getter
@Setter
@ToString (callSuper = true)
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Slf4j
public abstract class Booking {

    /**
     * Unique identifier for each booking.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /**
     * The employee associated with the booking.
     * It must not be null.
     */
    @NotNull(message = "employee must not be null")
    @ManyToOne(fetch = FetchType.EAGER)
    private Employee employee;

    /**
     * The date for the booking.
     * It must be a present or future date.
     */

    @NotNull
    @FutureOrPresent(message = "The date must not be in the past")
    private LocalDate date;

    /**
     * The time slot for the booking.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    private Timeslot timeSlot;

    /**
     * The timestamp of when the booking was made.
     * It must be a present or past date/time.
     */

    @CreatedDate
    @Column(updatable = false)
    @PastOrPresent(message = "The date created must not be in the future")
    protected LocalDateTime createdOn;

    /**
     * The timestamp of when the booking was updated last.
     * It must be a present or past date/time.
     */

    @LastModifiedDate
    @Column(insertable = false)
    protected LocalDateTime updatedOn;

    /**
     * The start time of the booking.
     */
    private LocalTime start;

    /**
     * The end time of the booking.
     */
    private LocalTime endTime;


    /**
     * Constructor for creating a booking with a specific employee, date, start and end times.
     */
    public Booking(@NotNull Employee employee, @NotNull LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.employee = employee;
        this.date = date;
        this.start = startTime;
        this.endTime = endTime;
    }

    /**
     * Constructor for creating a booking with a specific employee, date, time slot, and timestamp.
     * The start and end times are determined by the time slot.
     */
    public Booking(@NotNull Employee employee, @NotNull LocalDate date, Timeslot timeSlot) {
        this.employee = employee;
        this.date = date;
        this.timeSlot = timeSlot;
        this.start = timeSlot.getStartTime();
        this.endTime = timeSlot.getEndTime();
    }

    /**
     * This method checks if the start time is before the end time.
     * It is used for data validation purposes.
     *
     * @return True if the start time is before the end time, false otherwise.
     */

    @AssertTrue(message = "Start time must be before end time")
    public boolean isValidTime() {
        return start.isBefore(endTime);
    }

    /**
     * Returns the timestamp of when the booking was created.
     * @return The timestamp of when the booking was created.
     */
    public LocalDateTime getCreatedOn() {
        return this.createdOn;
    }

    /**
     * Sets the timestamp of when the booking was created.
     * @param createdOn The timestamp of when the booking was created.
     */
    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * Returns the timestamp of when the booking was last updated.
     * @return The timestamp of when the booking was last updated.
     */
    public LocalDateTime getUpdatedOn() {
        return this.updatedOn;
    }

    /**
     * Sets the timestamp of when the booking was last updated.
     * @param updatedOn The timestamp of when the booking was last updated.
     */
    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }
}