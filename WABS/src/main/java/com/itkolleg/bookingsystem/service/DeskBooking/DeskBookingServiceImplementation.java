package com.itkolleg.bookingsystem.service.deskbooking;

import com.itkolleg.bookingsystem.domains.booking.DeskBooking;
import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Role;
import com.itkolleg.bookingsystem.exceptions.CustomIllegalArgumentException;
import com.itkolleg.bookingsystem.exceptions.DeskNotAvailableException;
import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.repos.deskbooking.DeskBookingRepo;
import com.itkolleg.bookingsystem.repos.holiday.HolidayRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation for handling desk booking operations.
 *
 * @author Sonja Lechner
 * @version 1.0
 * @since 2023-05-26
 */
@Service
@Slf4j
public class DeskBookingServiceImplementation implements DeskBookingService {

    private final DeskBookingRepo deskBookingRepo;
    private final HolidayRepo holidayRepo;

    public DeskBookingServiceImplementation(DeskBookingRepo deskBookingRepo, HolidayRepo holidayRepo) {
        this.deskBookingRepo = deskBookingRepo;
        this.holidayRepo = holidayRepo;
    }

    /**
     * Creates a new desk booking.
     *
     * @param booking The desk booking to be created.
     * @return The added desk booking.
     * @throws DeskNotAvailableException If the desk is not available for the booking period.
     * @throws ResourceNotFoundException If a required resource is not found.
     * @throws IllegalArgumentException  If the booking date is in the past, on a weekend, or on a non-booking allowed day.
     */
    @Override
    public DeskBooking addDeskBooking(DeskBooking booking) throws DeskNotAvailableException, ResourceNotFoundException, CustomIllegalArgumentException {
//
        Role role = booking.getEmployee().getRole();

        checkDeskAvailability(booking);
        checkBookingDate(booking.getDate());
        checkRoleBasedBooking(booking.getDate(), role);
        checkHolidayBooking(booking.getDate());

        return this.deskBookingRepo.addBooking(booking);
    }

    /**
     * Retrieves all desk bookings from the database.
     *
     * @return A list of all desk bookings.
     * @throws ResourceNotFoundException If no desk bookings are found.
     */
    @Override
    public List<DeskBooking> getAllBookings() throws ResourceNotFoundException {
        // Fetch all bookings from the repository
        return deskBookingRepo.getAllBookings();
    }


    /**
     * Searches for desk bookings based on employee and date criteria.
     *
     * @param employee The employee to search for (can be null).
     * @param date     The date to search for (can be null).
     * @return The list of desk bookings matching the search criteria.
     * @throws ResourceNotFoundException If the booking with the specified ID is not found.
     */
    @Override
    public List<DeskBooking> searchBookings(Long employee, LocalDate date) throws ResourceNotFoundException{
        List<DeskBooking> bookings = new ArrayList<>();
        if (employee != null && date != null) {
            // Search for bookings by employee and date range
            bookings = deskBookingRepo.getBookingsByEmployeeAndDate(employee, date);
        } else if (employee != null) {
            // Search for bookings by employee
            bookings = deskBookingRepo.getBookingsByEmployee(employee);
        } else if (date != null) {
            // Search for bookings by date range
            bookings = deskBookingRepo.getBookingByDate(date);
        }
        return bookings;
    }

    /**
    * Searches for desk bookings based on provided criteria.
    *
    * <p>This method allows for flexible searching based on the provided parameters.
    * If all parameters are provided, it will search for bookings matching all criteria.
    * If only one or two parameters are provided, it will search based on the available criteria.
    * If no parameters are provided, it will return an empty list.</p>
    *
    * @param employeeId The ID of the employee to search for. Can be null.
    * @param deskId The ID of the desk to search for. Can be null.
    * @param date The date of the booking to search for. Can be null.
    * @return A list of desk bookings that match the provided criteria. If no matches are found, an empty list is returned.
    * @throws ResourceNotFoundException If the specified resources are not found based on the search criteria.
     */
    @Override
    public List<DeskBooking> searchBookings(Long employeeId, Long deskId, LocalDate date) throws ResourceNotFoundException {
        // If all parameters are provided, search by employeeId, deskId, and date
        if (employeeId != null && deskId != null && date != null) {
            return deskBookingRepo.getBookingsByEmployeeIdAndDeskIdAndDate(employeeId, deskId, date);
        }

        // If only employeeId is provided, search by employeeId
        if (employeeId != null) {
            return deskBookingRepo.getBookingsByEmployee(employeeId);
        }

        // If only deskId is provided, search by deskId
        if (deskId != null) {
            return deskBookingRepo.getBookingByDesk(deskId);
        }

        // If only date is provided, search by date
        if (date != null) {
            return deskBookingRepo.getBookingByDate(date);
        }

        // If none of the parameters are provided, return an empty list
        return new ArrayList<>();
    }

    /**
     * Retrieves a desk booking by its ID.
     *
     * @param bookingId The unique identifier of the desk booking.
     * @return An Optional containing the desk booking if found, empty otherwise.
     * @throws ResourceNotFoundException If the booking with the specified ID is not found.
     */
    @Override
    public Optional<DeskBooking> getBookingById(Long bookingId) throws ResourceNotFoundException {
        // Fetch the booking from the repository using the provided ID
        return Optional.ofNullable(deskBookingRepo.getBookingByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking with ID " + bookingId + " not found.")));
    }

    /**
     * Retrieves desk bookings associated with a specific employee.
     *
     * @param employeeId The unique identifier of the employee.
     * @return A list of desk bookings associated with the given employee.
     * @throws ResourceNotFoundException If no bookings are found for the specified employee ID.
     */
    @Override
    public List<DeskBooking> getBookingsByEmployeeId(Long employeeId) throws ResourceNotFoundException {
        // Fetch bookings by employee ID from the repository
        return deskBookingRepo.getBookingsByEmployee(employeeId);
    }


    /**
     * Retrieves all desk bookings for a specific employee.
     *
     * @param employee The employee object.
     * @return The list of desk bookings for the employee.
     * @throws ResourceNotFoundException If the booking with the specified ID is not found.
     */
    @Override
    public List<DeskBooking> getBookingsByEmployee(Long employee) throws ResourceNotFoundException{
        // Fetch bookings by employee ID from the repository
        return deskBookingRepo.getBookingsByEmployee(employee);
    }

    /**
     * @param nick The Nick of the logged-in User
     * @return aA list of desk bookings associated with the given employee with the specified nick.
     */
    @Override
    public List<DeskBooking> getBookingsByEmployeeNick(String nick){
        return deskBookingRepo.getBookingsByEmployeeNick(nick);
    }

    /**
     * Retrieves desk bookings for a specific employee on a given date.
     *
     * @param employee The employee for whom the desk bookings are being retrieved.
     * @param date The specific date for which the bookings are being retrieved.
     * @return A list of desk bookings associated with the given employee on the specified date.
     * @throws ResourceNotFoundException If no bookings are found for the specified employee and date.
     */
    @Override
    public List<DeskBooking> getBookingsByEmployeeAndDate(Long employee, LocalDate date) throws ResourceNotFoundException {
        // Fetch bookings by employee and date from the repository
        return deskBookingRepo.getBookingsByEmployeeAndDate(employee, date);
    }

    /**
     * Retrieves desk bookings for a specific desk on a given date.
     *
     * @param desk The desk for which the bookings are being retrieved.
     * @param date The specific date for which the bookings are being retrieved.
     * @return A list of desk bookings associated with the given desk on the specified date.
     * @throws ResourceNotFoundException If no bookings are found for the specified desk and date.
     */
    @Override
    public List<DeskBooking> getBookingsByDeskAndDate(Long desk, LocalDate date) throws ResourceNotFoundException {
        // Fetch bookings by desk and date from the repository
        return deskBookingRepo.getBookingsByDeskAndDate(desk, date);
    }

    /**
     * Retrieves all desk bookings associated with a specific desk.
     *
     * @param desk The desk for which the bookings are being retrieved.
     * @return A list of desk bookings associated with the given desk.
     * @throws ResourceNotFoundException If no bookings are found for the specified desk.
     */
    @Override
    public List<DeskBooking> getBookingByDesk(Long desk) throws ResourceNotFoundException {
        // Fetch bookings by desk from the repository
        return deskBookingRepo.getBookingByDesk(desk);
    }

    /**
     * Retrieves desk bookings for a specific date.
     *
     * @param date The date for which the desk bookings are being retrieved.
     * @return A list of desk bookings scheduled for the specified date.
     * @throws ResourceNotFoundException If no bookings are found for the specified date.
     */
    @Override
    public List<DeskBooking> getBookingsByDate(LocalDate date) throws ResourceNotFoundException {
        // Define the start and end times for the entire day
        LocalTime startOfDay = LocalTime.MIDNIGHT;
        LocalTime endOfDay = LocalTime.MAX;

        // Fetch the bookings from the repository for the provided date and time range
        List<DeskBooking> bookings = deskBookingRepo.getBookingsByDateAndTimeRange(date, startOfDay, endOfDay);

        // Check if bookings are found, if not throw an exception
        if (bookings.isEmpty()) {
            throw new ResourceNotFoundException("No bookings found for date " + date);
        }

        return bookings;
    }


    /**
     * Updates a desk booking by its ID.
     *
     * @param bookingId The ID of the desk booking to update.
     * @param updatedBooking The updated desk booking object.
     * @return The updated desk booking.
     * @throws ResourceNotFoundException If the booking with the specified ID is not found.
     * @throws DeskNotAvailableException If the desk is not available for the updated date and time period.
     */
    public DeskBooking updateBookingById(Long bookingId, DeskBooking updatedBooking) throws ResourceNotFoundException, DeskNotAvailableException {
        // Fetch the existing booking from the repository
        Optional<DeskBooking> existingBooking = deskBookingRepo.getBookingByBookingId(bookingId);

        // Check if the booking exists
        if (existingBooking.isEmpty()) {
            throw new ResourceNotFoundException("Booking not found for ID: " + bookingId);
        }

        // Check if the desk is available for the updated date and time
        Desk desk = updatedBooking.getDesk();
        LocalDate date = updatedBooking.getDate();
        LocalTime start = updatedBooking.getStart();
        LocalTime endTime = updatedBooking.getEndTime();

        List<Desk> availableDesks = getAvailableDesks(date, start, endTime, desk.getId());
        if (availableDesks.isEmpty() || !availableDesks.contains(desk)) {
            throw new DeskNotAvailableException("Desk is not available for the specified date and time period.");
        }

        // Set the ID for the updated booking and save it
        updatedBooking.setId(bookingId);
        return deskBookingRepo.updateBooking(updatedBooking);
    }


    /**
     * Updates a desk booking.
     *
     * @param booking The updated desk booking.
     * @return The updated desk booking.
     * @throws DeskNotAvailableException  If the desk is not available for the updated booking period.
     * @throws ResourceNotFoundException   If the booking with the specified ID is not found.
     */
    @Override
    public DeskBooking updateBooking(DeskBooking booking) throws DeskNotAvailableException, ResourceNotFoundException {
        // Fetch the existing booking from the repository
        DeskBooking existingBooking = deskBookingRepo.getBookingByBookingId(booking.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found for ID: " + booking.getId()));

        // Check if the desk is available for the updated booking period
        List<DeskBooking> conflictingBookings = deskBookingRepo.getBookingsByDeskAndDateAndBookingTimeRange(booking.getId(), booking.getDate(), booking.getStart(), booking.getEndTime());
        conflictingBookings.removeIf(b -> b.getId().equals(existingBooking.getId())); // Remove the existing booking from the list

        if (!conflictingBookings.isEmpty()) {
            throw new DeskNotAvailableException("Desk is not available for the specified booking period.");
        }

        // Update the existing booking details
        existingBooking.setEmployee(booking.getEmployee());
        existingBooking.setDesk(booking.getDesk());
        existingBooking.setDate(booking.getDate());
        existingBooking.setStart(booking.getStart());
        existingBooking.setEndTime(booking.getEndTime());
        existingBooking.setCreatedOn(LocalDateTime.now());

        // Save the updated booking
        return deskBookingRepo.updateBooking(existingBooking);
    }


    /**
     * Deletes a desk booking by its ID.
     *
     * @param bookingId The ID of the desk booking to delete.
     * @throws ResourceDeletionFailureException If the deletion of the booking fails.
     * @throws ResourceNotFoundException If the booking with the specified ID is not found.
     */
    @Override
    public void deleteBookingById(Long bookingId) throws ResourceDeletionFailureException, ResourceNotFoundException {
        // Check if the booking exists
        if (!deskBookingRepo.existsById(bookingId)) {
            throw new ResourceNotFoundException("Booking not found for ID: " + bookingId);
        }
        try {
            deskBookingRepo.deleteBookingById(bookingId);
        } catch (Exception e) {
            throw new ResourceDeletionFailureException("Failed to delete booking with ID: " + bookingId, e);
        }
    }

    /**
     * Retrieves the booking history for a specific employee from the last two weeks.
     *
     * @param employeeId The ID of the employee.
     * @return A list of desk bookings from the last two weeks associated with the given employee ID.
     * @throws ResourceNotFoundException If no bookings are found for the specified employee ID in the last two weeks.
     */
    @Override
    public List<DeskBooking> getBookingHistoryByEmployeeId(Long employeeId) throws ResourceNotFoundException {
        LocalDate twoWeeksAgo = LocalDate.now().minusWeeks(2);
        List<DeskBooking> bookings = deskBookingRepo.getBookingsByEmployee(employeeId)
                .stream()
                .filter(booking -> booking.getDate().isBefore(LocalDate.now()) && booking.getDate().isAfter(twoWeeksAgo))
                .collect(Collectors.toList());

        if (bookings.isEmpty()) {
            throw new ResourceNotFoundException("No bookings found for employee ID: " + employeeId + " in the last two weeks.");
        }

        return bookings;
    }


    /**
     * Retrieves desk bookings for a specific date within a given time range.
     *
     * @param date The date to search bookings for.
     * @param startOfDay The start time of the booking period.
     * @param endOfDay The end time of the booking period.
     * @return A list of desk bookings that match the given criteria.
     * @throws ResourceNotFoundException If no bookings are found for the specified date and time range.
     */
    @Override
    public List<DeskBooking> getBookingByDateAndByStartBetween(LocalDate date, LocalTime startOfDay, LocalTime endOfDay) throws ResourceNotFoundException {
        List<DeskBooking> bookings = deskBookingRepo.getBookingsByDateAndTimeRange(date, startOfDay, endOfDay);

        if (bookings.isEmpty()) {
            throw new ResourceNotFoundException("No bookings found for the specified date and time range.");
        }

        return bookings;
    }

    /**
     * Persists a desk booking to the database.
     *
     * @param booking The desk booking entity to be saved.
     * @return The saved desk booking entity.
     * @throws ResourceNotFoundException If the booking with the specified ID is not found.
     * @throws DeskNotAvailableException If the desk is already booked for the specified period.
     */
    @Override
    public DeskBooking save(DeskBooking booking) throws ResourceNotFoundException, DeskNotAvailableException {
        // Check if the desk is available for the booking period
        List<DeskBooking> existingBookings = deskBookingRepo.getBookingsByDeskAndDateAndBookingTimeRange(booking.getDesk().getId(), booking.getDate(), booking.getStart(), booking.getEndTime());
        if (!existingBookings.isEmpty()) {
            throw new DeskNotAvailableException("The desk is already booked for the specified period.");
        }

        return deskBookingRepo.save(booking);
    }



    /**
     * Fetches the available desks for a given date and time range.
     * If a deskId is provided, it checks the availability of that specific desk.
     *
     * @param date The date for which to check desk availability.
     * @param start The start time for the booking.
     * @param end The end time for the booking.
     * @param deskId Optional specific desk ID to check its availability.
     * @return A list of available desks for the specified date and time range.
     */
    @Override
    public List<Desk> getAvailableDesks(LocalDate date, LocalTime start, LocalTime end, Long deskId) {
        return deskBookingRepo.getAvailableDesks(date, start, end, deskId);
    }

    private void checkDeskAvailability(DeskBooking booking) throws DeskNotAvailableException, ResourceNotFoundException {
        List<DeskBooking> bookings = deskBookingRepo.getBookingsByDeskAndDateAndBookingTimeRange(booking.getId(), booking.getDate(), booking.getStart(), booking.getEndTime());
        if (!bookings.isEmpty()) {
            throw new DeskNotAvailableException("Desk not available for booking period");
        }
    }

    private void checkBookingDate(LocalDate bookingDate) throws CustomIllegalArgumentException {
        LocalDate currentDate = LocalDate.now();
        if (bookingDate.isBefore(currentDate)) {
            throw new CustomIllegalArgumentException("Cannot create booking for a past date", "addDeskBooking");
        }
        DayOfWeek dayOfWeek = bookingDate.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            throw new CustomIllegalArgumentException("Cannot create booking for a weekend", "addDeskBooking");
        }
    }

    private void checkRoleBasedBooking(LocalDate bookingDate, Role role) throws CustomIllegalArgumentException {
        LocalDate currentDate = LocalDate.now();
        LocalDate maxAdvanceBookingDate = (role == Role.ROLE_N_EMPLOYEE) ? currentDate.plusWeeks(1) : currentDate.plusWeeks(12);
        if (bookingDate.isAfter(maxAdvanceBookingDate)) {
            throw new CustomIllegalArgumentException("Cannot book more than " +
                    (role == Role.ROLE_N_EMPLOYEE ? "1 week" : "12 weeks") + " in advance", "addDeskBooking");
        }
    }

    private void checkHolidayBooking(LocalDate bookingDate) throws CustomIllegalArgumentException {
        if (!holidayRepo.isBookingAllowedOnHoliday(bookingDate)) {
            throw new CustomIllegalArgumentException("No work-area bookings are allowed on this public holiday!", "addDeskBooking");
        }
    }

}
