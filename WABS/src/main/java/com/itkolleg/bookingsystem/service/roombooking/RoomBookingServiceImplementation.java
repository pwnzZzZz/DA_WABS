package com.itkolleg.bookingsystem.service.roombooking;

import com.itkolleg.bookingsystem.domains.booking.RoomBooking;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.Room;
import com.itkolleg.bookingsystem.exceptions.roomExceptions.RoomDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.roomExceptions.RoomNotAvailableException;
import com.itkolleg.bookingsystem.exceptions.roomExceptions.RoomNotFoundException;
import com.itkolleg.bookingsystem.repos.employee.EmployeeDBAccess;
import com.itkolleg.bookingsystem.repos.room.DBAccessRoom;
import com.itkolleg.bookingsystem.repos.roombooking.RoomBookingRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * The RoomBookingServiceImplementation class is responsible for implementing the RoomBookingService interface
 * and providing the functionality to manage room bookings.
 */
@Service
public class RoomBookingServiceImplementation implements RoomBookingService {
    static final Logger logger = LoggerFactory.getLogger(RoomBookingServiceImplementation.class);

    private final RoomBookingRepo roomBookingRepo;
    private final DBAccessRoom dbAccessRoom;
    private final EmployeeDBAccess employeeDBAccess;


    /**
     * Constructs a RoomBookingServiceImplementation with the specified dependencies.
     *
     * @param roomBookingRepo   the repository for room bookings
     * @param dbAccessRoom      the database access for rooms
     * @param employeeDBAccess  the database access for employees
     */
    public RoomBookingServiceImplementation(RoomBookingRepo roomBookingRepo, DBAccessRoom dbAccessRoom, EmployeeDBAccess employeeDBAccess) {
        this.roomBookingRepo = roomBookingRepo;
        this.dbAccessRoom = dbAccessRoom;
        this.employeeDBAccess = employeeDBAccess;
    }

    /**
     * Adds a new room booking.
     *
     * @param roomBooking the room booking to add
     * @return the added room booking
     * @throws RoomNotAvailableException if the room is not available for the booking period
     * @throws RoomNotFoundException    if the room is not found
     */
    @Override
    public RoomBooking addRoomBooking(RoomBooking roomBooking) throws RoomNotAvailableException, RoomNotFoundException {
        List<RoomBooking> bookings = this.roomBookingRepo.getBookingsByRoomAndDateAndBookingTimeBetween(roomBooking.getRoom(), roomBooking.getDate(), roomBooking.getStart(), roomBooking.getEndTime());
        LocalDate currentDate = LocalDate.now();
        System.out.println("booking date: " + roomBooking.getDate());
        System.out.println("Current date: " + LocalDate.now());

        if (!bookings.isEmpty()) {
            throw new RoomNotAvailableException("room not available for booking period");
        }

        if (roomBooking.getDate().isBefore(currentDate)) {
            throw new IllegalArgumentException("Cannot create booking a past date");
        }
        return this.roomBookingRepo.addBooking(roomBooking);
    }
    /**
     * Retrieves all room bookings.
     *
     * @return a list of all room bookings
     */
    @Override
    public List<RoomBooking> getAllBookings() {
        return this.roomBookingRepo.getAllBookings();
    }
    /**
     * Retrieves room bookings by employee ID.
     *
     * @param employeeId the employee ID
     * @return a list of room bookings by the employee
     */
    @Override
    public List<RoomBooking> getBookingsByEmployeeId(Long employeeId) {
        return this.roomBookingRepo.getBookingsByEmployeeId(employeeId);
    }

    /**
     * Retrieves room bookings by employee.
     *
     * @param employee the employee
     * @return a list of room bookings by the employee
     */
    @Override
    public List<RoomBooking> getBookingsByEmployee(Employee employee) {
        return this.roomBookingRepo.getBookingsByEmployee(employee);
    }

    /**
     * Retrieves room bookings by room.
     *
     * @param room the room
     * @return a list of room bookings for the room
     */
    @Override
    public List<RoomBooking> getBookingsByRoom(Room room) {
        return this.roomBookingRepo.getBookingsByRoom(room);
    }

    /**
     * Retrieves room bookings by date.
     *
     * @param localDate the date
     * @return a list of room bookings for the date
     */
    @Override
    public List<RoomBooking> getBookingsByDate(LocalDate localDate) {
        LocalTime startOfDay = LocalTime.from(localDate.atStartOfDay());
        LocalTime endOfDay = startOfDay.plusHours(24).minusSeconds(1);
        return this.roomBookingRepo.getBookingsByDateAndByStartBetween(localDate, startOfDay, endOfDay);
    }

    /**
     * Retrieves a room booking by ID.
     *
     * @param bookingId the booking ID
     * @return the room booking with the specified ID
     * @throws RoomNotFoundException if the room booking is not found
     */
    @Override
    public RoomBooking getBookingById(Long bookingId) throws RoomNotFoundException {
        Optional<RoomBooking> bookingOptional = this.roomBookingRepo.getBookingByBookingId(bookingId);
        if (bookingOptional.isPresent()) {
            return bookingOptional.get();
        } else {
            throw new RoomNotFoundException("booking not found for id: " + bookingId);

        }
    }

    /**
     * Updates a room booking by ID.
     *
     * @param bookingId      the booking ID
     * @param updatedBooking the updated room booking
     * @return the updated room booking
     * @throws RoomNotFoundException       if the room booking is not found
     * @throws RoomNotAvailableException   if the room is not available for the booking period
     */
    @Override
    public RoomBooking updateBookingById(Long bookingId, RoomBooking updatedBooking) throws RoomNotFoundException, RoomNotAvailableException {
        Optional<RoomBooking> booking = this.roomBookingRepo.getBookingByBookingId(bookingId);
        if (booking.isEmpty()) {
            throw new RoomNotFoundException("booking not found for id: " + bookingId);
        }
        Room room = booking.get().getRoom();
        LocalDate date = booking.get().getDate();
        LocalTime start = booking.get().getStart();
        LocalTime endTime = booking.get().getEndTime();
        if (isRoomAvailable(room, date, start, endTime)) {
            updatedBooking.setId(bookingId);
        }

        return this.roomBookingRepo.updateBooking(updatedBooking);


    }

    /**
     * Updates a room booking.
     *
     * @param booking the updated room booking
     * @return the updated room booking
     * @throws RoomNotFoundException       if the room booking is not found
     * @throws RoomNotAvailableException   if the room is not available for the booking period
     */
    @Override
    public RoomBooking updateBooking(RoomBooking booking) throws RoomNotFoundException, RoomNotAvailableException {
        try {
            RoomBooking existingBooking = this.roomBookingRepo.getBookingByBookingId(booking.getId())
                    .orElseThrow(() -> new RoomNotFoundException("booking not found for id: " + booking.getId()));

            List<RoomBooking> bookings = roomBookingRepo.getBookingsByRoomAndDateAndBookingTimeBetween(booking.getRoom(), booking.getDate(), booking.getStart(), booking.getEndTime());
            bookings.remove(existingBooking);
            if (!bookings.isEmpty()) {
                throw new RoomNotAvailableException("room not available for booking period!");
            }
            existingBooking.setEmployee(booking.getEmployee());
            existingBooking.setRoom(booking.getRoom());
            existingBooking.setDate(booking.getDate());
            existingBooking.setStart(booking.getStart());
            existingBooking.setEndTime(booking.getEndTime());
            existingBooking.setCreatedOn(LocalDateTime.now());
            return this.roomBookingRepo.updateBooking(existingBooking);
        } catch (DataAccessException e) {
            throw new RoomNotFoundException("booking not found for id: " + booking.getId());
        }
    }

    /**
     * Finds room bookings by room, date, start time, and end time.
     *
     * @param room     the room
     * @param date     the date
     * @param start    the start time
     * @param endTime  the end time
     * @return a list of room bookings that match the criteria
     */
    public List<RoomBooking> findByRoomAndBookingEndAfterAndBookingStartBefore(Room room, LocalDate date, LocalTime start, LocalTime endTime) {
        return this.roomBookingRepo.getBookingsByRoomAndDateAndBookingTimeBetween(room, date, start, endTime);
    }
    /**
     * Deletes a room booking by ID.
     *
     * @param BookingId the booking ID
     * @throws RoomNotFoundException            if the room booking is not found
     * @throws RoomDeletionNotPossibleException if the room deletion is not possible
     */
    @Override
    public void deleteBookingById(Long BookingId) throws RoomNotFoundException, RoomDeletionNotPossibleException {
        Optional<RoomBooking> booking = this.roomBookingRepo.getBookingByBookingId(BookingId);
        if (booking.isEmpty()) {
            throw new RoomDeletionNotPossibleException("room not found!");
        }
        roomBookingRepo.deleteBookingById(BookingId);
    }

    /**
     * Retrieves the available rooms for a specific date and time range.
     *
     * @param date     the date
     * @param start    the start time
     * @param endTime  the end time
     * @return a list of available rooms
     * @throws ExecutionException    if an error occurs during execution
     * @throws InterruptedException if the execution is interrupted
     */
    @Override
    public List<Room> getAvailableRooms(LocalDate date, LocalTime start, LocalTime endTime) throws ExecutionException, InterruptedException {
        return this.dbAccessRoom.getAllRooms().stream()
                .filter(room -> roomBookingRepo.getBookingsByRoomAndDateAndBookingTimeBetween(room, date, start, endTime).isEmpty()).collect(Collectors.toList());
    }
    /**
     * Checks if a room is available for booking at a specific date and time range.
     *
     * @param room          the room
     * @param date          the date
     * @param startDateTime the start time
     * @param endtime       the end time
     * @return true if the room is available, false otherwise
     */
    @Override
    public boolean isRoomAvailable(Room room, LocalDate date, LocalTime startDateTime, LocalTime endtime) {
        List<RoomBooking> bookings = this.roomBookingRepo.getBookingsByRoomAndDateAndBookingTimeBetween(room, date, startDateTime, endtime);
        return bookings.isEmpty();
    }

    /**
     * Deletes a room booking.
     *
     * @param id of the room booking to delete
     * @throws RoomNotFoundException if the room booking is not found
     */
    @Override
    public void deleteBooking(Long id) throws RoomNotFoundException, RoomDeletionNotPossibleException {
        this.roomBookingRepo.deleteBookingById(id);
    }

    @Override
    public List<RoomBooking> getMyBookingHistory(Long employeeId) {
        return this.roomBookingRepo.getBookingsByEmployeeId(employeeId);
    }

    @Override
    public RoomBooking save(RoomBooking roomBooking) {
        return this.roomBookingRepo.save(roomBooking);
    }
}
