package com.itkolleg.bookingsystem.service.timeslot;

import com.itkolleg.bookingsystem.domains.Timeslot;
import com.itkolleg.bookingsystem.repos.timeslot.TimeslotRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the TimeslotService interface.
 * This class contains the business logic associated with TimeSlots.
 */
@Service
public class TimeslotServiceImplementation implements TimeslotService {

    private static final Logger logger = LoggerFactory.getLogger(TimeslotServiceImplementation.class);
    private final TimeslotRepo timeslotRepo;

    /**
     * Constructs a TimeslotServiceImplementation instance.
     *
     * @param timeslotRepo Repository for accessing timeslot data.
     */
    public TimeslotServiceImplementation(TimeslotRepo timeslotRepo) {
        this.timeslotRepo = timeslotRepo;
    }

    /**
     * Adds a new timeslot.
     *
     * @param timeslot The timeslot to add.
     * @return The added timeslot.
     */
    @Override
    public Timeslot addTimeslot(Timeslot timeslot) {
        return this.timeslotRepo.addTimeslot(timeslot);
    }

    /**
     * Retrieves all TimeSlots.
     *
     * @return A list of all TimeSlots.
     */
    @Override
    public List<Timeslot> getAllTimeslots() {
        return this.timeslotRepo.getAllTimeslots();
    }

    /**
     * Retrieves a time slot by id.
     * id of the time slot.
     *
     * @param id The id of the Time Slot
     * @return An Optional containing the time slot if it exists.
     */
    @Override
    public Optional<Timeslot> getTimeslotById(Long id) {
        return this.timeslotRepo.getTimeslotById(id);
    }

    /**
     * Retrieves a timeslot by its name.
     *
     * @param name The name of the timeslot.
     * @return An Optional containing the found timeslot, or empty if no timeslot was found.
     */
    @Override
    public Optional<Timeslot> getTimeslotByName(String name) {
        return this.timeslotRepo.getTimeslotByName(name);
    }


    /**
     * Retrieves a timeslot by its start time.
     *
     * @param startTime The start time of the timeslot.
     * @return An Optional containing the found timeslot, or empty if no timeslot was found.
     */
    @Override
    public Optional<Timeslot> getTimeslotByStartTime(LocalTime startTime) {
        return this.timeslotRepo.getTimeslotByStartTime(startTime);
    }

    /**
     * Retrieves a timeslot by its end time.
     *
     * @param endTime The end time of the timeslot.
     * @return An Optional containing the found timeslot, or empty if no timeslot was found.
     */
    @Override
    public Optional<Timeslot> getTimeslotByEndTime(LocalTime endTime) {
        return this.timeslotRepo.getTimeslotByEndTime(endTime);
    }

    /**
     * Updates a timeslot.
     *
     * @param timeslot The timeslot to update.
     * @return An Optional containing the updated timeslot, or empty if no timeslot was found.
     */
    @Override
    public Optional<Timeslot> updateTimeslot(Timeslot timeslot) {
        return this.timeslotRepo.updateTimeslot(timeslot);
    }

    /**
     * Deletes a timeslot by its id.
     *
     * @param id The id of the timeslot to delete.
     */
    @Override
    public void deleteTimeslotById(Long id) {
        this.timeslotRepo.deleteTimeslotById(id);
    }

    /**
     * Deletes a timeslot by its name.
     * If the timeslot with the given name does not exist, an error is logged and no action is taken.
     *
     * @param name The name of the timeslot to delete.
     */
    @Override
    public void deleteTimeslotByName(String name) {
        try {
            Timeslot toDelete = this.timeslotRepo.getTimeslotByName(name)
                    .orElseThrow(() -> new IllegalArgumentException("No timeslot found with the given name: " + name));
            this.timeslotRepo.delete(toDelete);
        } catch (IllegalArgumentException e) {
            logger.error("timeslot with name '{}' not found", name, e);
        }
    }
}