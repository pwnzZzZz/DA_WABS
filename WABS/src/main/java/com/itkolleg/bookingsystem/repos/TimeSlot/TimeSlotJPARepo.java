package com.itkolleg.bookingsystem.repos.timeslot;

import com.itkolleg.bookingsystem.domains.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Optional;

/**
 * TimeslotJPARepo provides an API for CRUD operations on TimeSlots.
 * It extends JpaRepository, which provides JPA related methods out of the box for CRUD operations.
 */
@Repository
public interface TimeslotJPARepo extends JpaRepository<Timeslot, Long> {

    /**
     * Retrieves a timeslot by its start time.
     *
     * @param startTime The start time of the timeslot.
     * @return An Optional containing the found timeslot, or empty if no timeslot was found with the given start time.
     */
    Optional<Timeslot> findByStartTime(LocalTime startTime);

    /**
     * Retrieves a timeslot by its end time.
     *
     * @param endTime The end time of the timeslot.
     * @return An Optional containing the found timeslot, or empty if no timeslot was found with the given end time.
     */
    Optional<Timeslot> findByEndTime(LocalTime endTime);

    /**
     * Retrieves a timeslot by its name
     * .
     *
     * @param name The name of the timeslot.
     * @return An Optional containing the found timeslot, or empty if no timeslot was found with the given name.
     */
    Optional<Timeslot> findByName(String name);

    void deleteTimeSlotByName(String name);
}