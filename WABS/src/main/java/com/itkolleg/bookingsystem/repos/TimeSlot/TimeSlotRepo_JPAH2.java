package com.itkolleg.bookingsystem.repos.timeslot;

import com.itkolleg.bookingsystem.domains.Timeslot;
import com.itkolleg.bookingsystem.exceptions.DatabaseOperationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 *
 */
@Slf4j
@Component
@ComponentScan({"com.itkolleg.repos"})
public class TimeslotRepo_JPAH2 implements TimeslotRepo {

    final TimeslotJPARepo timeslotJPARepo;

    public TimeslotRepo_JPAH2(TimeslotJPARepo timeslotJPARepo) {
        this.timeslotJPARepo = timeslotJPARepo;
    }

    /**
     * Create a new Time Slot
     *
     */
    @Override
    public Timeslot addTimeslot(Timeslot timeslot) {
        return this.timeslotJPARepo.save(timeslot);
    }

    /**
     * Retrieves a list of all time slots in the database.
     *it exists, empty Optional otherwise.
     */
    @Override
    public List<Timeslot> getAllTimeslots() {
        return this.timeslotJPARepo.findAll();
    }

    /**
     * Retrieves a time slot by its id.
     *
     * @param id The id of the time slot.
     * @return An Optional of the time slot if it exists, empty Optional otherwise.
     */
    @Override
    public Optional<Timeslot> getTimeslotById(Long id) {
        return this.timeslotJPARepo.findById(id);
    }

    /**
     * Retrieves a time slot by its name.
     *
     * @param name The name of the time slot.
     * @return An Optional of the time slot if it exists, empty Optional otherwise.
     */
    @Override
    public Optional<Timeslot> getTimeslotByName(String name) {
        return this.timeslotJPARepo.findByName(name);
    }

    /**
     * Retrieves a time slot by its start time.
     *
     * @param startTime The start time of the time slot.
     * @return An Optional of the time slot if it exists, empty Optional otherwise.
     */
    @Override
    public Optional<Timeslot> getTimeslotByStartTime(LocalTime startTime) {
        return this.timeslotJPARepo.findByStartTime(startTime);
    }

    /**
     * Retrieves a time slot by its end time.
     *
     * @param endTime The end time of the time slot.
     * @return An Optional of the time slot if it exists, empty Optional otherwise.
     */
    @Override
    public Optional<Timeslot> getTimeslotByEndTime(LocalTime endTime) {
        return Optional.empty();
    }

    /**
     * Updates a time slot.
     *
     * @param timeslot The time slot with updated information.
     * @return An Optional of the updated time slot if it exists, empty Optional otherwise.
     * @throws DatabaseOperationException If the operation fails.
     */
    @Override
    public Optional<Timeslot> updateTimeslot(Timeslot timeslot) throws DatabaseOperationException {
        return Optional.empty();
    }

    /**
     * Deletes a time slot by its id.
     *
     * @param id The id of the time slot.
     * @throws DatabaseOperationException If the operation fails.
     */
    @Override
    public void deleteTimeslotById(Long id) throws DatabaseOperationException {
        this.timeslotJPARepo.deleteById(id);
    }


    /**
     * Deletes a time slot by its name.
     *
     * @param name The name of the time slot.
     * @throws DatabaseOperationException If the operation fails.
     */
    @Override
    public void deleteTimeslotByName(String name) throws DatabaseOperationException {
        this.timeslotJPARepo.deleteTimeSlotByName(name);
    }

    /**
     * Deletes a specific time slot.
     *
     * @param toDelete The time slot to delete.
     * @throws DatabaseOperationException If the operation fails.
     */
    @Override
    public void delete(Timeslot toDelete) throws DatabaseOperationException {
        this.timeslotJPARepo.delete(toDelete);
    }

}