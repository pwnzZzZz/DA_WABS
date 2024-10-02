package com.itkolleg.bookingsystem.repos.timeslot;

import com.itkolleg.bookingsystem.domains.Timeslot;
import com.itkolleg.bookingsystem.exceptions.DatabaseOperationException;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * interface for handling operations related to time slots.
 * Includes methods to add, retrieve, update, and delete time slots.
 *
 * @author Sonja Lechner
 * @version 1.0
 * @since 2023-05-24
 */
public interface TimeslotRepo {

    /**
     * Adds a new time slot.
     *
     * @param timeslot The time slot to add.
     * @return The added time slot.
     * @throws DatabaseOperationException If the operation fails.
     */
    Timeslot addTimeslot(Timeslot timeslot) throws DatabaseOperationException;

    /**
     * Retrieves all time slots.
     *
     * @return A list of all time slots.
     * @throws DatabaseOperationException If the operation fails.
     */
    List<Timeslot> getAllTimeslots() throws DatabaseOperationException;

    /**
     * Retrieves a time slot by its id.
     *
     * @param id The id of the time slot.
     * @return An Optional of the time slot if it exists, empty Optional otherwise.
     */
    Optional<Timeslot> getTimeslotById(Long id);

    /**
     * Retrieves a time slot by its name.
     *
     * @param name The name of the time slot.
     * @return An Optional of the time slot if it exists, empty Optional otherwise.
     */
    Optional<Timeslot> getTimeslotByName(String name);

    /**
     * Retrieves a time slot by its start time.
     *
     * @param startTime The start time of the time slot.
     * @return An Optional of the time slot if it exists, empty Optional otherwise.
     */
    Optional<Timeslot> getTimeslotByStartTime(LocalTime startTime);

    /**
     * Retrieves a time slot by its end time.
     *
     * @param endTime The end time of the time slot.
     * @return An Optional of the time slot if it exists, empty Optional otherwise.
     */
    Optional<Timeslot> getTimeslotByEndTime(LocalTime endTime);

    /**
     * Updates a time slot.
     *
     * @param timeslot The time slot with updated information.
     * @return An Optional of the updated time slot if it exists, empty Optional otherwise.
     * @throws DatabaseOperationException If the operation fails.
     */
    Optional<Timeslot> updateTimeslot(Timeslot timeslot) throws DatabaseOperationException;

    /**
     * Deletes a time slot by its id.
     *
     * @param Id The id of the time slot.
     * @throws DatabaseOperationException If the operation fails.
     */
    void deleteTimeslotById(Long Id) throws DatabaseOperationException;

    /**
     * Deletes a time slot by its name.
     *
     * @param name The name of the time slot.
     * @throws DatabaseOperationException If the operation fails.
     */
    void deleteTimeslotByName(String name) throws DatabaseOperationException;

    /**
     * Deletes a specific time slot.
     *
     * @param toDelete The time slot to delete.
     * @throws DatabaseOperationException If the operation fails.
     */
    void delete(Timeslot toDelete) throws DatabaseOperationException;

}
