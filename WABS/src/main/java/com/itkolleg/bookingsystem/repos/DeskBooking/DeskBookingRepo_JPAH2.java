package com.itkolleg.bookingsystem.repos.deskbooking;

import com.itkolleg.bookingsystem.domains.Timeslot;
import com.itkolleg.bookingsystem.domains.booking.DeskBooking;
import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.exceptions.DeskNotAvailableException;
import com.itkolleg.bookingsystem.repos.desk.DeskJPARepo;
import com.itkolleg.bookingsystem.repos.employee.EmployeeJPARepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The JPA implementation of the DeskBookingRepo interface.
 * This class provides methods to interact with the desk booking repository.
 *
 * @author Sonja Lechner
 * @version 1.3
 * @since 2023-08-28
 */
@Slf4j
@Component
@ComponentScan({"com.itkolleg.repos"})
public class DeskBookingRepo_JPAH2 implements DeskBookingRepo {

    private final DeskBookingJPARepo deskBookingJPARepo;
    private final DeskJPARepo deskJPARepo;
    private final EmployeeJPARepo employeeJPARepo;

    /**
     * Constructor to initialize the JPA repositories.
     *
     * @param deskBookingJPARepo The desk booking JPA repository.
     * @param deskJPARepo        The desk JPA repository.
     * @param employeeJPARepo    The employee JPA repository.
     */
    public DeskBookingRepo_JPAH2(DeskBookingJPARepo deskBookingJPARepo, DeskJPARepo deskJPARepo, EmployeeJPARepo employeeJPARepo) {
        this.deskBookingJPARepo = deskBookingJPARepo;
        this.deskJPARepo = deskJPARepo;
        this.employeeJPARepo = employeeJPARepo;
    }


    /**
     * Adds a desk booking to the repository.
     *
     * @param deskBooking The desk booking to add.
     * @return The added desk booking.
     * @throws DeskNotAvailableException If the desk is not available for the booking period or if the employee already has a booking on the specified date.
     * @throws ResourceNotFoundException If the desk or employee is not found.
     * @throws IllegalArgumentException If any of the required fields are null.
     */
    @Override
    public DeskBooking addBooking(DeskBooking deskBooking) throws ResourceNotFoundException, DeskNotAvailableException{
        // The repo layer should not be concerned with business logic validations.
        // It should only handle database operations and related exceptions.

        // Check if the desk exists
        Desk desk = deskJPARepo.findDeskById(deskBooking.getDesk().getId());
        if (desk == null) {
            throw new ResourceNotFoundException("Desk with ID " + deskBooking.getDesk().getId() + " was not found");
        }

        // Check if the employee already has a booking on this date
        List<DeskBooking> existingBookings = deskBookingJPARepo.findBookingsByEmployeeIdAndDate(deskBooking.getEmployee().getId(), deskBooking.getDate());
        if (!existingBookings.isEmpty()) {
            throw new DeskNotAvailableException(deskBooking.getEmployee().getFname() + " " + deskBooking.getEmployee().getLname() + " already has a booking on this date!");
        }

        // Save the booking
        try {
            return this.deskBookingJPARepo.save(deskBooking);
        } catch (Exception e) {
            throw new RuntimeException("Error saving the booking to the database", e);
        }
    }




    /**
     * Retrieves all desk bookings from the repository.
     *
     * @return A list of all desk bookings in the database.
     * @throws ResourceNotFoundException If no desk bookings are found.
     */
    @Override
    public List<DeskBooking> getAllBookings() throws ResourceNotFoundException {
        // Fetch all bookings from the database
        List<DeskBooking> bookings = deskBookingJPARepo.findAll();

        //check if the list is empty
        if (bookings.isEmpty()) {
            throw new ResourceNotFoundException("No desk bookings found.");
        }
        // Filter out bookings that are in the past
        return bookings.stream()
                .filter(booking -> !booking.getDate().isBefore(LocalDate.now()))
                .collect(Collectors.toList());
    }


    /**
     * Searches for desk bookings based on the provided criteria.
     *
     * @param employee Optional employee criteria.
     * @param desk     Optional desk criteria.
     * @param date     Optional date criteria.
     * @param start    Optional start time criteria.
     * @param endTime  Optional end time criteria.
     * @param timeslot Optional timeslot criteria.
     * @return A list of desk bookings that match the provided criteria.
     */
    @Override
    public List<DeskBooking> searchBookings(Optional<Employee> employee, Optional<Desk> desk, Optional<LocalDate> date, Optional<LocalTime> start, Optional<LocalTime> endTime, Optional<Timeslot> timeslot) {
        // Retrieve all bookings from the repository
        List<DeskBooking> allBookings = deskBookingJPARepo.findAll();

        // Filter bookings based on provided criteria
        Stream<DeskBooking> filteredBookings = allBookings.stream();

        if (employee.isPresent()) {
            filteredBookings = filteredBookings.filter(booking -> booking.getEmployee().equals(employee.get()));
        }

        if (desk.isPresent()) {
            filteredBookings = filteredBookings.filter(booking -> booking.getDesk().equals(desk.get()));
        }

        if (date.isPresent()) {
            filteredBookings = filteredBookings.filter(booking -> booking.getDate().equals(date.get()));
        }

        if (start.isPresent()) {
            filteredBookings = filteredBookings.filter(booking -> !booking.getStart().isBefore(start.get()));
        }

        if (endTime.isPresent()) {
            filteredBookings = filteredBookings.filter(booking -> !booking.getEndTime().isAfter(endTime.get()));
        }

        // Assuming Timeslot has a start and end time
        if (timeslot.isPresent()) {
            filteredBookings = filteredBookings.filter(booking ->
                    !booking.getStart().isBefore(timeslot.get().getStart()) &&
                            !booking.getEndTime().isAfter(timeslot.get().getEnd()));
        }

        return filteredBookings.collect(Collectors.toList());
    }



    /**
     * Searches for desk bookings based on the given parameters.
     *
     * @param employeeId The ID of the employee.
     * @param deskId     The ID of the desk.
     * @param date       The date.
     * @return The list of desk bookings that match the search criteria.
     */
    public List<DeskBooking> searchBookings(Long employeeId, Long deskId, LocalDate date){

        // If the date is in the past, return an empty list
        if (date != null && date.isBefore(LocalDate.now())) {
            return new ArrayList<>();
        }

        // If all parameters are null, return all bookings
        if (employeeId == null && deskId == null && date == null) {
            return deskBookingJPARepo.findAll();
        }

        // If only employee ID and date are provided, search by employee ID and date
        if (employeeId != null && date != null) {
            return deskBookingJPARepo.findBookingsByEmployeeIdAndDate(employeeId, date);
        }

        // If only desk ID and date are provided, search by desk ID and date
        if (deskId != null && date != null) {
            return deskBookingJPARepo.findBookingsByDeskIdAndDate(deskId, date);
        }

        // If only employee ID is provided, search by employee ID
        if (employeeId != null) {
            return deskBookingJPARepo.findBookingsByEmployeeId(employeeId);
        }

        // If only desk ID is provided, search by desk ID
        if (deskId != null) {
            return deskBookingJPARepo.findBookingsByDeskId(deskId);
        }

        return new ArrayList<>();
    }


    /**
     * Retrieves a desk booking based on the provided booking ID.
     *
     * @param bookingId The unique identifier of the desired desk booking.
     * @return An Optional containing the desk booking if found, or an empty Optional otherwise.
     * @throws ResourceNotFoundException If no desk booking is found for the provided booking ID.
     */
    @Override
    public Optional<DeskBooking> getBookingByBookingId(Long bookingId) throws ResourceNotFoundException {
        // Fetch all bookings from the database with the Desk Booking ID provided
        Optional<DeskBooking> bookingOptional = deskBookingJPARepo.findBookingsById(bookingId);

        //check if the list is empty
        if (bookingOptional.isEmpty()) {
            String message = "Desk booking with the ID: " + bookingId + " was not found!";
            log.error(message);
            throw new ResourceNotFoundException(message);
        }
        return bookingOptional;
    }


    /**
     * Retrieves all desk bookings associated with a specific employee for today or future dates.
     *
     * @param employeeId  The unique identifier of the employee for which bookings are to be retrieved.
     * @return The list of desk bookings associated with the specified employee.
     * @throws ResourceNotFoundException If no bookings are found for the specified desk ID.
     */
    public List<DeskBooking> getBookingsByEmployee(Long employeeId) throws ResourceNotFoundException{
        // Fetch all bookings from the database on the given date for the given employee
        List<DeskBooking> bookings = deskBookingJPARepo.findBookingsByEmployeeId(employeeId);

        // Filter out bookings that are in the past
        List<DeskBooking> filteredBookings = bookings.stream()
                .filter(booking -> !booking.getDate().isBefore(LocalDate.now()))
                .collect(Collectors.toList());

        //check if the list is empty
        if (filteredBookings.isEmpty()) {
            throw new ResourceNotFoundException("No bookings found for desk ID: " + employeeId + " for today or future dates.");
        }

        return filteredBookings;
    }


    /**
     * Retrieves all desk bookings associated with a specific desk for today or future dates.
     *
     * @param deskId The unique identifier of the desk for which bookings are to be retrieved.
     * @return A list of desk bookings associated with the specified desk.
     * @throws ResourceNotFoundException If no bookings are found for the specified desk ID.
     */
    @Override
    public List<DeskBooking> getBookingByDesk(Long deskId) throws ResourceNotFoundException {
        // Fetch all bookings from the database for the desk provided
        List<DeskBooking> bookings = deskBookingJPARepo.findBookingsByDeskId(deskId);

        if (deskId == null) {
            throw new IllegalArgumentException("Desk ID cannot be null");
        }

        // Filter out bookings that are in the past
        List<DeskBooking> filteredBookings = bookings.stream()
                .filter(booking -> !booking.getDate().isBefore(LocalDate.now()))
                .collect(Collectors.toList());

        //check if the list is empty
        if (filteredBookings.isEmpty()) {
            throw new ResourceNotFoundException("No bookings found for desk ID: " + deskId + " for today or future dates.");
        }

        return filteredBookings;
    }


    /**
     * Retrieves all desk bookings associated with a specific desk for a specific date.
     *
     * @param date The date for which bookings are to be retrieved.
     * @return A list of desk bookings for the specific date.
     * @throws ResourceNotFoundException If no bookings are found for the specified date.
     */
    @Override
    public List<DeskBooking> getBookingByDate(LocalDate date) throws ResourceNotFoundException {
        // Fetch all bookings from the database for the given date
        List<DeskBooking> bookings = deskBookingJPARepo.findBookingsByDate(date);

        //check if the list is empty
        if (bookings.isEmpty()) {
            throw new ResourceNotFoundException("No bookings found for date: " + date + ".");
        }
        return bookings;
    }

    /**
     * Retrieves all desk bookings for a specific employee and date.
     *
     * @param employeeId The unique identifier of the employee for which bookings are to be retrieved.
     * @param date       The date to retrieve bookings for.
     * @return The list of desk bookings for the specified employee and date.
     * @throws ResourceNotFoundException If no bookings are found for the specified employee and date.
     */
    @Override
    public List<DeskBooking> getBookingsByEmployeeAndDate(Long employeeId, LocalDate date) throws ResourceNotFoundException {
        // Fetch all bookings from the database for the employee on the given date.
        List<DeskBooking> bookings = deskBookingJPARepo.findBookingsByEmployeeIdAndDate(employeeId, date);

        // Check if the list is empty
        if (bookings.isEmpty()) {
            throw new ResourceNotFoundException("No bookings found for employee ID: " + employeeId + " on date: " + date);
        }
        return bookings;
    }

    /**
     * Retrieves all desk bookings for a specific employee and date.
     *
     * @param employeeId The unique identifier of the employee for which bookings are to be retrieved.
     * @param deskId     The unique identifier of the desk for which bookings are to be retrieved.
     * @return The list of desk bookings for the specified employee and date.
     * @throws ResourceNotFoundException If no bookings are found for the specified employee and date.
     */
    @Override
    public List<DeskBooking> getBookingsByEmployeeAndDesk(Long employeeId, Long deskId) throws ResourceNotFoundException {
        // Fetch all bookings from the database for the employee and desk
        List<DeskBooking> bookings = deskBookingJPARepo.findBookingsByEmployeeIdAndDeskId(employeeId, deskId);

        // Check if the list is empty
        if (bookings.isEmpty()) {
            throw new ResourceNotFoundException("No bookings found for employee ID: " + employeeId + " for desk: " + deskId);
        }
        return bookings;
    }

    /**
     * Retrieves desk bookings for a specific desk, date, and booking time range.
     *
     * @param deskId The desk to retrieve bookings for.
     * @param date The date to retrieve bookings for.
     * @param start The start time of the booking time range.
     * @param endTime The end time of the booking time range.
     * @return The list of desk bookings for the specified desk, date, and booking time range.
     */
    @Override
    public List<DeskBooking> getBookingsByDeskAndDateAndBookingTimeRange(Long deskId, LocalDate date, LocalTime start, LocalTime endTime){
        // Fetch all bookings from the database on the given date for the desk provided that start between the times given
        List<DeskBooking> bookings = deskBookingJPARepo.findBookingsByDeskIdDateAndTimeRange(deskId, date, start, endTime);

        // Check if the list is empty
        if (bookings.isEmpty()) {
            return Collections.emptyList();  // return an empty list instead of throwing an exception
        }
        return bookings;
    }

    /**
     * Retrieves all desk bookings for a specific desk on a given date.
     * The method filters out bookings that are in the past, ensuring that only current and future bookings are returned.
     *
     * @param deskId The unique identifier of the desk for which bookings are to be retrieved.
     * @param date The specific date for which bookings are to be retrieved.
     * @return A list of desk bookings associated with the specified desk on the given date.
     * @throws ResourceNotFoundException If no bookings are found for the specified desk and date.
     */
    @Override
    public List<DeskBooking> getBookingsByDeskAndDate(Long deskId, LocalDate date) throws ResourceNotFoundException {
        // Fetch all bookings from the database on the given date for the desk provided
        List<DeskBooking> bookings = deskBookingJPARepo.findBookingsByDeskIdAndDate(deskId, date);

        // Check if the list is empty
        if (bookings.isEmpty()) {
            throw new ResourceNotFoundException("No bookings found for desk ID: " + deskId + " on date: " + date);
        }

        return bookings;
    }

    /**
     * Retrieves desk bookings for a specific date within a given booking time range.
     * The method returns bookings that start between the provided start and end times on the given date.
     *
     * @param date        The specific date for which bookings are to be retrieved.
     * @param bookingStart The start time of the booking time range.
     * @param bookingEnd   The end time of the booking time range.
     * @return A list of desk bookings that match the specified criteria.
     * @throws ResourceNotFoundException If no bookings are found for the specified date and time range.
     */
    @Override
    public List<DeskBooking> getBookingsByDateAndTimeRange(LocalDate date, LocalTime bookingStart, LocalTime bookingEnd) throws ResourceNotFoundException {
        // Fetch all bookings on the given date that have a start time between the provided start and end times
        List<DeskBooking> bookings = deskBookingJPARepo.findByDateAndStartBetween(date, bookingStart, bookingEnd);

        // Check if the list is empty
        if (bookings.isEmpty()) {
            throw new ResourceNotFoundException("No bookings found for date: " + date + " between times: " + bookingStart + " and " + bookingEnd);
        }

        return bookings;
    }


    /**
     * Retrieves a list of desk bookings based on the provided employee ID, desk ID, and date.
     * This method fetches all bookings associated with a specific employee, for a specific desk,
     * on a specific date. It's useful for scenarios where you want to see if a particular employee
     * has booked a specific desk on a given date.
     *
     * @param employeeId The unique identifier of the employee for which bookings are to be retrieved.
     * @param deskId The unique identifier of the desk for which bookings are to be retrieved.
     * @param date The specific date for which bookings are to be retrieved.
     * @return A list of desk bookings that match the specified criteria.
     */
    @Override
    public List<DeskBooking> getBookingsByEmployeeIdAndDeskIdAndDate(Long employeeId, Long deskId, LocalDate date) throws ResourceNotFoundException{
        // Fetch all bookings for the employee & desks on the given date.
        List<DeskBooking> bookings = deskBookingJPARepo.findBookingsByEmployeeIdAndDeskIdAndDate(employeeId, deskId, date);

        // Check if the list is empty
        if (bookings.isEmpty()) {
            throw new ResourceNotFoundException("No bookings found for employee ID: " + employeeId + " and desk ID: " + deskId + " on date: " + date + ".");
        }

        return bookings;
    }

    /**
     * Updates a desk booking by its ID.
     *
     * @param deskBookingId       The ID of the desk booking to update.
     * @param updatedDeskBooking  The updated desk booking details.
     * @return The updated desk booking.
     * @throws ResourceNotFoundException If the booking or associated entities are not found.
     */
    public DeskBooking updateBookingById(Long deskBookingId, DeskBooking updatedDeskBooking) throws ResourceNotFoundException {
        // Validate the provided updated booking details
        validateUpdatedBooking(updatedDeskBooking);

        // Fetch the existing booking using the provided ID
        return deskBookingJPARepo.findById(deskBookingId).map(existingBooking -> {
            // Fetch the associated desk and employee details for the updated booking
            Desk fetchedDesk;
            try {
                fetchedDesk = fetchDesk(updatedDeskBooking.getDesk().getId());
            } catch (ResourceNotFoundException e) {
                throw new RuntimeException(e);
            }
            Employee fetchedEmployee;
            try {
                fetchedEmployee = fetchEmployee(updatedDeskBooking.getEmployee().getId());
            } catch (ResourceNotFoundException e) {
                throw new RuntimeException(e);
            }

            // Update the existing booking with the new details
            existingBooking.setDesk(fetchedDesk);
            existingBooking.setEmployee(fetchedEmployee);
            existingBooking.setDate(updatedDeskBooking.getDate());
            existingBooking.setStart(updatedDeskBooking.getStart());
            existingBooking.setEndTime(updatedDeskBooking.getEndTime());
            existingBooking.setUpdatedOn(LocalDateTime.now());
            return existingBooking;
        }).orElseThrow(() -> new ResourceNotFoundException("The desk booking with the ID: " + deskBookingId + " was not found!"));
    }

    /**
     * Validates the provided desk booking details.
     *
     * @param updatedDeskBooking The desk booking details to validate.
     * @throws IllegalArgumentException If any mandatory field is missing.
     */
    private void validateUpdatedBooking(DeskBooking updatedDeskBooking) {
        // Check if all required fields are present in the updated booking
        if (updatedDeskBooking.getDesk() == null || updatedDeskBooking.getEmployee() == null || updatedDeskBooking.getDate() == null
                || updatedDeskBooking.getStart() == null || updatedDeskBooking.getEndTime() == null || updatedDeskBooking.getCreatedOn() == null) {
            throw new IllegalArgumentException("Updated booking must have valid desk, employee, Date, StartTime, EndTime, and Creation Date.");
        }
    }

    /**
     * Fetches the desk details using the provided desk ID.
     *
     * @param deskId The ID of the desk to fetch.
     * @return The fetched desk details.
     * @throws ResourceNotFoundException If the desk is not found.
     */
    private Desk fetchDesk(Long deskId) throws ResourceNotFoundException {
        // Fetch the desk from the repository using the provided ID
        return deskJPARepo.findById(deskId)
                .orElseThrow(() -> new ResourceNotFoundException("The desk with the ID: " + deskId + " was not found!"));
    }

    /**
     * Fetches the employee details using the provided employee ID.
     *
     * @param employeeId The ID of the employee to fetch.
     * @return The fetched desk details.
     * @throws ResourceNotFoundException If the employee is not found.
     */
    private Employee fetchEmployee(Long employeeId) throws ResourceNotFoundException {
        return employeeJPARepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("The employee with the ID: " + employeeId + " was not found!"));
    }


    /**
     * Updates a desk booking in the repository.
     *
     * @param updatedBooking The desk booking object containing updated details.
     * @return The updated desk booking after saving to the repository.
     * @throws IllegalArgumentException If the ID of the updated booking is null.
     */
    public DeskBooking updateBooking(DeskBooking updatedBooking) {
        // Check if the ID of the updated booking is provided
        if (updatedBooking.getId() == null) {
            throw new IllegalArgumentException("Id cannot be null when updating");
        }

        // Save the updated booking to the repository and immediately flush changes
        return deskBookingJPARepo.saveAndFlush(updatedBooking);
    }

    /**
     * Deletes a desk booking from the repository using its ID.
     *
     * @param bookingId The unique identifier of the desk booking to be deleted.
     * @throws ResourceDeletionFailureException If the specified booking is not found in the repository.
     */
    public void deleteBookingById(Long bookingId) throws ResourceDeletionFailureException {
        // Retrieve the booking using the provided ID
        Optional<DeskBooking> bookingOptional = this.deskBookingJPARepo.findById(bookingId);

        // Check if the booking exists in the repository
        if (bookingOptional.isPresent()) {
            // Delete the booking from the repository
            this.deskBookingJPARepo.deleteById(bookingId);
        } else {
            // Throw an exception if the booking is not found
            throw new ResourceDeletionFailureException("The desk booking with the ID: " + bookingId + " was not found!");
        }
    }

    /**
     * Retrieves the booking history for a specific employee.
     *
     * @param employeeId The unique identifier of the employee for which the booking history is to be retrieved.
     * @return A list of desk bookings associated with the specified employee.
     * @throws ResourceNotFoundException If no bookings are found for the specified employee.
     */
    @Override
    public List<DeskBooking> getBookingHistoryByEmployeeId(Long employeeId) throws ResourceNotFoundException {
        return this.deskBookingJPARepo.findBookingsByEmployeeId(employeeId);
    }

    /**
     * Retrieves available desks for a specific date and time range.
     * This method checks the availability of each desk for the given date and time range
     * and returns a list of desks that are not booked during that period.
     *
     * @param date The date to check for availability.
     * @param start The start time of the time range.
     * @param end The end time of the time range.
     * @param specificDeskId (Optional) The specific desk ID to check availability for.
     * @return A list of available desks for the specified date and time range.
     *         If specificDeskId is provided, the list will contain either the specific desk (if available) or be empty.
     */
    public List<Desk> getAvailableDesks(LocalDate date, LocalTime start, LocalTime end, Long specificDeskId) {
        List<Desk> allDesks;

        allDesks = new ArrayList<>(deskJPARepo.findAll());

        List<Desk> availableDesks = new ArrayList<>();

        try {
            for (Desk desk : allDesks) {
                Long deskId = desk.getId();
                List<DeskBooking> overlappingBookings = deskBookingJPARepo.findBookingsByDeskIdDateAndTimeRange(deskId, date, start, end);

                // Ensure overlappingBookings is not null
                if (overlappingBookings == null || overlappingBookings.isEmpty()) {
                    availableDesks.add(desk);
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        return availableDesks;
    }

     /**
     * Persists a desk booking to the repository.
     * This method is used to save a new desk booking or update an existing one
     * in the repository. If the booking already exists (based on its ID),
     * it will be updated; otherwise, a new booking will be created.
     *
     * @param booking The desk booking entity to be saved or updated.
     * @return The saved or updated desk booking entity.
     */
    public DeskBooking save(DeskBooking booking) {
        // Save or update the provided booking in the repository
        return this.deskBookingJPARepo.save(booking);
    }


    /**
     * Checks if a desk booking exists based on the provided booking ID.
     *
     * @param bookingId The unique identifier of the desk booking to be checked.
     * @return True if the desk booking exists, false otherwise.
     */
    @Override
    public boolean existsById(Long bookingId) {
        //check if booking with the provided ID already exists in the repository
        return deskBookingJPARepo.existsById(bookingId);
    }

    /**
     * @param nick The nick of the employee
     * @return The list of booking associated with the Employee with the nick
     */
    @Override
    public List<DeskBooking> getBookingsByEmployeeNick(String nick) {
        return null;
    }


}
