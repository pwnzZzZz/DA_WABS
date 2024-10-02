package com.itkolleg.bookingsystem.repos.roombooking;

import com.itkolleg.bookingsystem.domains.booking.RoomBooking;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.Room;
import com.itkolleg.bookingsystem.exceptions.employeeExceptions.EmployeeNotFoundException;
import com.itkolleg.bookingsystem.exceptions.roomExceptions.RoomDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.roomExceptions.RoomNotAvailableException;
import com.itkolleg.bookingsystem.exceptions.roomExceptions.RoomNotFoundException;
import com.itkolleg.bookingsystem.repos.employee.EmployeeJPARepo;
import com.itkolleg.bookingsystem.repos.room.RoomJPARepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



/**
 * The RoomBookingRepo_JPAH2 class is responsible for managing room bookings in the booking system using JPA and H2 database.
 */
@Component
@ComponentScan({"com.itkolleg.repos"})
public class RoomBookingRepo_JPAH2 implements RoomBookingRepo {
    private static final Logger logger = LoggerFactory.getLogger(RoomBookingRepo_JPAH2.class);
    private final RoomBookingJPARepo roomBookingJPARepo;
    private final RoomJPARepo roomJPARepo;
    private final EmployeeJPARepo employeeJPARepo;



    /**
     * Constructs a RoomBookingRepo_JPAH2 with the specified repositories.
     *
     * @param roomBookingJPARepo The repository for room bookings.
     * @param roomJPARepo        The repository for rooms.
     * @param employeeJPARepo    The repository for employees.
     */
    public RoomBookingRepo_JPAH2(RoomBookingJPARepo roomBookingJPARepo, RoomJPARepo roomJPARepo, EmployeeJPARepo employeeJPARepo) {
        this.roomBookingJPARepo = roomBookingJPARepo;
        this.roomJPARepo = roomJPARepo;
        this.employeeJPARepo = employeeJPARepo;
    }

    /**
     * Adds a room booking to the system.
     *
     * @param booking The room booking to be added.
     * @return The added room booking.
     * @throws RoomNotAvailableException If the room is not available for the booking period.
     * @throws RoomNotFoundException    If the room is not found.
     */
    @Override
    public RoomBooking addBooking(RoomBooking booking) throws RoomNotAvailableException, RoomNotFoundException {
        if (booking == null || booking.getRoom() == null) {

            throw new IllegalArgumentException("The roombooking or room cannot be null!");
        }

        Long roomId = booking.getRoom().getId();
        Room room = this.roomJPARepo.findRoomById(roomId);

        if (!isRoomAvailable(room, booking.getDate(), booking.getStart(), booking.getEndTime())) {
            throw new RoomNotAvailableException("room not available for booking period!");
        }
        RoomBooking roomBooking = new RoomBooking();
        roomBooking.setEmployee(booking.getEmployee());
        roomBooking.setRoom(room);
        roomBooking.setDate(booking.getDate());
        roomBooking.setStart(booking.getStart());
        roomBooking.setEndTime(booking.getEndTime());
        roomBooking.setCreatedOn(LocalDateTime.now());
        roomBooking.setUpdatedOn(LocalDateTime.now());

        try {
            return this.roomBookingJPARepo.save(roomBooking);
        } catch (Exception e) {
            throw new RuntimeException("Error saving the booking to the database", e);
        }
    }



    /**
     * Retrieves all rooms in the system.
     *
     * @return A list of all rooms.
     */
    @Override
    public List<Room> getAllRooms() {
        return this.roomJPARepo.findAll();
    }


    /**
     * Retrieves all room bookings in the system.
     *
     * @return A list of all room bookings.
     */
    @Override
    public List<RoomBooking> getAllBookings() {
        return this.roomBookingJPARepo.findAll();
    }

    /**
     * Retrieves a room booking by its ID.
     *
     * @param id The ID of the room booking.
     * @return An optional containing the room booking, or empty if not found.
     */
    @Override
    public Optional<RoomBooking> getBookingByBookingId(Long id) {
        return this.roomBookingJPARepo.findById(id);
    }
    /**
     * Retrieves all room bookings for a specific room.
     *
     * @param room The room to filter the bookings.
     * @return A list of room bookings for the specified room.
     */
    @Override
    public List<RoomBooking> getBookingsByRoom(Room room) {
        return this.roomBookingJPARepo.getBookingsByRoom(room);
    }

    /**
     * Retrieves all room bookings for a specific room ID.
     *
     * @param roomId The ID of the room to filter the bookings.
     * @return A list of room bookings for the specified room ID.
     */
    @Override
    public List<RoomBooking> getBookingsByRoomId(Long roomId) {
        return this.roomBookingJPARepo.getBookingsByRoomId(roomId);
    }

    /**
     * Retrieves all room bookings for a specific date.
     *
     * @param date The date to filter the bookings.
     * @return A list of room bookings for the specified date.
     */
    @Override
    public List<RoomBooking> getBookingsByDate(LocalDate date) {
        return this.roomBookingJPARepo.getBookingsByDate(date);
    }
    /**
     * Retrieves all room bookings for a specific employee.
     *
     * @param employee The employee to filter the bookings.
     * @return A list of room bookings for the specified employee.
     */
    @Override
    public List<RoomBooking> getBookingsByEmployee(Employee employee) {
        return this.roomBookingJPARepo.getBookingsByEmployee(employee);
    }

    /**
     * Retrieves all room bookings for a specific employee ID.
     *
     * @param employeeId The ID of the employee to filter the bookings.
     * @return A list of room bookings for the specified employee ID.
     */
    @Override
    public List<RoomBooking> getBookingsByEmployeeId(Long employeeId) {
        return this.roomBookingJPARepo.getBookingsByEmployeeId(employeeId);
    }

    /**
     * Retrieves all room bookings for a specific room and date.
     *
     * @param room The room to filter the bookings.
     * @param date The date to filter the bookings.
     * @return A list of room bookings for the specified room and date.
     */
    @Override
    public List<RoomBooking> getBookingsByRoomAndDate(Room room, LocalDate date) {
        return this.roomBookingJPARepo.getBookingsByRoomAndDate(room, date);
    }

    /**
     * Retrieves all room bookings for a specific room, date, and booking time between start and end.
     *
     * @param room     The room to filter the bookings.
     * @param date     The date to filter the bookings.
     * @param start    The start time of the booking.
     * @param endTime  The end time of the booking.
     * @return A list of room bookings for the specified room, date, and booking time.
     */
    @Override
    public List<RoomBooking> getBookingsByRoomAndDateAndBookingTimeBetween(Room room, LocalDate date, LocalTime start, LocalTime endTime) {
        return this.roomBookingJPARepo.getBookingsByRoomAndDateAndStartBetween(room, date, start, endTime);
    }
    /**
     * Updates a room booking by its ID.
     *
     * @param roomBookingId     The ID of the room booking to be updated.
     * @param updatedRoomBooking The updated room booking.
     * @return The updated room booking.
     * @throws RoomNotFoundException If the room is not found.
     */
    @Override
    public RoomBooking updateBookingById(Long roomBookingId, RoomBooking updatedRoomBooking) throws RoomNotFoundException {
        if (updatedRoomBooking.getRoom() == null || updatedRoomBooking.getStart() == null || updatedRoomBooking.getEndTime() == null || updatedRoomBooking.getCreatedOn() == null) {
            throw new IllegalArgumentException("Updated booking must have valid room, employee, Date, StartTime, EndTime and Creation Date.");
        }
        return this.roomBookingJPARepo.findById(roomBookingId).map(existingBooking -> {
            Room fetchedRoom;
            try {
                fetchedRoom = this.roomJPARepo.findById(updatedRoomBooking.getRoom().getId())
                        .orElseThrow(() -> new RoomNotFoundException("booking not found for id: " + updatedRoomBooking.getRoom().getId()));
            } catch (RoomNotFoundException e) {
                logger.error(e.getMessage());
                throw new RuntimeException("Failed to update booking due to missing room. Original error: " + e.getMessage());
            }
            Employee fetchedEmployee;
            try {
                fetchedEmployee = this.employeeJPARepo.findById(updatedRoomBooking.getEmployee().getId())
                        .orElseThrow(() -> new EmployeeNotFoundException("The employee with the ID: " + updatedRoomBooking.getEmployee().getId() + " was not found!"));
            } catch (EmployeeNotFoundException e) {
                logger.error(e.getMessage());
                throw new RuntimeException("Failed to update booking due to missing employee. Original error: " + e.getMessage());
            }
            existingBooking.setRoom(fetchedRoom);
            existingBooking.setEmployee(fetchedEmployee);
            existingBooking.setDate(updatedRoomBooking.getDate());
            existingBooking.setStart(updatedRoomBooking.getStart());
            existingBooking.setEndTime(updatedRoomBooking.getEndTime());
            existingBooking.setUpdatedOn(LocalDateTime.now());
            return existingBooking;
        }).orElseThrow(() -> new RoomNotFoundException("booking not found for id: " +roomBookingId));
    }

    /**
     * Updates a room booking.
     *
     * @param updatedBooking The updated room booking.
     * @return The updated room booking.
     * @throws RoomNotFoundException If the room is not found.
     */

    @Override
    public RoomBooking updateBooking(RoomBooking updatedBooking) throws RoomNotFoundException {
        if (updatedBooking.getId() == null) {
            throw new IllegalArgumentException("ID cannot be null when updating");
        }
        return this.roomBookingJPARepo.saveAndFlush(updatedBooking);
    }

    /**
     * Deletes a room booking by its ID.
     *
     * @param id The ID of the room booking to be deleted.
     * @throws RoomDeletionNotPossibleException If the room deletion is not possible.
     */
    @Override
    public void deleteBookingById(Long id) throws RoomDeletionNotPossibleException {
        Optional<RoomBooking> bookingOptional = this.roomBookingJPARepo.findById(id);
        if (bookingOptional.isPresent()) {
            this.roomBookingJPARepo.deleteById(id);
        } else {
            throw new RoomDeletionNotPossibleException("The room booking with ID:" + id + "was not found!");
        }
    }

    /**
     * Retrieves available rooms for a specific date and booking time.
     *
     * @param date     The date to filter the available rooms.
     * @param start    The start time of the booking.
     * @param endTime  The end time of the booking.
     * @return A list of available rooms for the specified date and booking time.
     */
    @Override
    public List<Room> getAvailableRooms(LocalDate date, LocalTime start, LocalTime endTime) {
        List<Room> allRooms = this.roomJPARepo.findAll();
        List<Room> availableRooms = new ArrayList<>();

        for (Room room : allRooms) {
            if (isRoomAvailable(room, date, start, endTime)) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }


    /**
     * Checks if a room is available for a specific date and booking time.
     *
     * @param room     The room to check availability.
     * @param date     The date to check availability.
     * @param start    The start time of the booking.
     * @param endTime  The end time of the booking.
     * @return True if the room is available, false otherwise.
     */
    @Override
    public boolean isRoomAvailable(Room room, LocalDate date, LocalTime start, LocalTime endTime) {
        List<RoomBooking> overlappingBookings = this.roomBookingJPARepo.getBookingsByRoomAndDateAndStartBetween(room, date, start, endTime);
        return overlappingBookings.isEmpty();
    }


    /**
     * Retrieves all room bookings for a specific employee and date.
     *
     * @param employee The employee to filter the bookings.
     * @param date     The date to filter the bookings.
     * @return A list of room bookings for the specified employee and date.
     */
    @Override
    public List<RoomBooking> getBookingsByEmployeeAndDate(Employee employee, LocalDate date) {
        return this.roomBookingJPARepo.getBookingsByEmployeeAndDate(employee, date);
    }

    /**
     * Retrieves all room bookings for a specific date and booking time between start and end.
     *
     * @param date     The date to filter the bookings.
     * @param start    The start time of the booking.
     * @param endTime  The end time of the booking.
     * @return A list of room bookings for the specified date and booking time.
     */
    @Override
    public List<RoomBooking> getBookingsByDateAndByStartBetween(LocalDate date, LocalTime start, LocalTime endTime) {
        return this.roomBookingJPARepo.getBookingsByDateAndStartBetween(date, start, endTime);
    }

    /**
     * Retrieves all room bookings for a specific employee ID, date, and room ID.
     *
     * @param employeeId The ID of the employee to filter the bookings.
     * @param date       The date to filter the bookings.
     * @param roomId     The ID of the room to filter the bookings.
     * @return A list of room bookings for the specified employee ID, date, and room ID.
     */
    @Override
    public List<RoomBooking> getBookingsByEmployeeIdAndDateAndRoomId(Long employeeId, LocalDate date, Long roomId) {
        return this.roomBookingJPARepo.getBookingsByEmployeeIdAndDateAndRoomId(employeeId, date, roomId);
    }

    /**
     * Saves a room booking.
     *
     * @param booking The room booking to be saved.
     * @return The saved room booking.
     */
    @Override
    public RoomBooking save(RoomBooking booking) {
        return this.roomBookingJPARepo.save(booking);
    }
}
