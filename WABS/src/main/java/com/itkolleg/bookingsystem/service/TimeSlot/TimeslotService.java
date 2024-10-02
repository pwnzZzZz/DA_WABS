package com.itkolleg.bookingsystem.service.timeslot;
import com.itkolleg.bookingsystem.domains.Timeslot;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing time slots.
 *
 * @author Sonja Lechner
 * @version 1.0
 * @since 2023-05-24
 */
public interface TimeslotService {

    /**
     * Adds a new time slot.
     *
     * @param timeslot The new time slot to add.
     * @return The added time slot.
     */
    Timeslot addTimeslot(Timeslot timeslot);

    /**
     * Retrieves all time slots.
     *
     * @return A list of all time slots.
     */
    List<Timeslot> getAllTimeslots();

    /**
     * Retrieves a time slot by name.
     *
     * @param name The name of the time slot.
     * @return An Optional containing the time slot if it exists.
     */
    Optional<Timeslot> getTimeslotByName(String name);

    /**
     * Retrieves a time slot by id.
     *id of the time slot.
     * @return An Optional containing the time slot if it exists.
     */
    Optional<Timeslot> getTimeslotById(Long id);

    /**
     * Retrieves a time slot by start time.
     *
     * @param startTime The start time of the time slot.
     * @return An Optional containing the time slot if it exists.
     */
    Optional<Timeslot> getTimeslotByStartTime(LocalTime startTime);

    /**
     * Retrieves a time slot by end time.
     *
     * @param endTime The end time of the time slot.
     * @return An Optional containing the time slot if it exists.
     */
    Optional<Timeslot> getTimeslotByEndTime(LocalTime endTime);

    /**
     * Updates a time slot.
     *
     * @param timeslot The time slot to update.
     * @return The updated time slot.
     */
    Optional<Timeslot> updateTimeslot(Timeslot timeslot);

    /**
     * Deletes a time slot by ID.
     *
     * @param id The ID of the time slot to delete.
     */
    void deleteTimeslotById(Long id);

    /**
     * Deletes a time slot by name.
     *
     * @param name The name of the time slot to delete.
     */
    void deleteTimeslotByName(String name);

}
