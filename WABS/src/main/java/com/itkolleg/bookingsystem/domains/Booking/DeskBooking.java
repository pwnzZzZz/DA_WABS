package com.itkolleg.bookingsystem.domains.booking;


import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.Timeslot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Represents a booking specifically for a desk.
 * This class extends the generic booking class to include a desk, providing more specific details about the booking.
 * It includes lifecycle hooks to log information during the persistence operations.

 * Note:
 * - The `@Slf4j` annotation is used to enable logging capabilities.
 * - The `@ToString.Include` annotation ensures that the desk's details are included when the `toString()` method is called.
 * - The `equals` and `hashCode` methods are overridden to provide a custom implementation for comparing `DeskBooking` objects.
 * - Lifecycle hooks like `@PrePersist`, `@PostPersist`, `@PreUpdate`, and `@PostUpdate` are used to log information before and after
 *   certain JPA operations.

 * Important:
 * - Ensure that the desk associated with the booking is available for the specified time slot before creating a booking.
 * - Always check the booking status of the desk before updating or deleting a booking.
 *
 * @author Sonja Lechner
 * @version 1.3
 * @since 2023-08-28
 */
@Entity
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class DeskBooking extends Booking {

    /**
     * The desk associated with the booking.
     * It must not be null.
     */
    @NotNull(message = "desk must not be null")
    @ToString.Include
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Desk desk;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeskBooking that = (DeskBooking) o;
        return Objects.equals(desk, that.desk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(desk);
    }
    @PrePersist
    public void onPrePersist() {
        log.info("Attempting to create a desk booking for desk: {}", desk.getDeskNr());
    }

    @PostPersist
    public void onPostPersist() {
        log.info("Successfully created a desk booking with ID: {} for desk: {}", getId(), desk.getDeskNr());
    }

    @PreUpdate
    protected void onPreUpdate() {
        updatedOn = LocalDateTime.now();
        log.info("Attempting to update a desk booking with ID: {} for desk: {}", getId(), desk.getDeskNr());
    }

    @PostUpdate
    public void onPostUpdate() {
        log.info("Successfully updated a desk booking with ID: {} for desk: {}", getId(), desk.getDeskNr());
    }


    /**
     * Constructor for creating a desk booking with specific employee, desk, date, start and end times, and timestamp.
     * @param employee The employee associated with the booking.
     * @param desk The desk associated with the booking.
     * @param date The date for the booking.
     * @param start The start time for the booking.
     * @param endTime The end time for the booking.
     */
    public DeskBooking(Employee employee, Desk desk, LocalDate date, LocalTime start, LocalTime endTime) {
        super(employee,
                date,
                start,
                endTime);
    }

    /**
     * Constructor for creating a desk booking with a specific employee, desk, date, and time slot.
     * @param employee The employee associated with the booking.
     * @param desk The desk associated with the booking.
     * @param date The date for the booking.
     * @param timeSlot The time slot for the booking.
     */
    public DeskBooking(Employee employee, Desk desk, LocalDate date, Timeslot timeSlot) {
        super(employee,
                date,
                timeSlot);
        this.desk = desk;
    }

}