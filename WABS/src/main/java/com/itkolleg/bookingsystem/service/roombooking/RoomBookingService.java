package com.itkolleg.bookingsystem.service.roombooking;

import com.itkolleg.bookingsystem.domains.booking.RoomBooking;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.Room;
import com.itkolleg.bookingsystem.exceptions.roomExceptions.RoomDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.roomExceptions.RoomNotAvailableException;
import com.itkolleg.bookingsystem.exceptions.roomExceptions.RoomNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RoomBookingService {

    Logger logger = LoggerFactory.getLogger(RoomBookingService.class);

    RoomBooking addRoomBooking(RoomBooking roomBooking) throws RoomNotAvailableException, RoomNotFoundException;

    List<RoomBooking> getAllBookings();

    List<RoomBooking> getBookingsByEmployeeId(Long employeeId);

    List<RoomBooking> getBookingsByEmployee(Employee employee);

    List<RoomBooking> getBookingsByRoom(Room room);

    List<RoomBooking> getBookingsByDate(LocalDate localDate);

    RoomBooking getBookingById(Long bookingId) throws RoomNotFoundException;

    RoomBooking updateBookingById(Long bookingId, RoomBooking updatedBooking) throws RoomNotFoundException, RoomNotAvailableException;

    RoomBooking updateBooking(RoomBooking booking) throws RoomNotFoundException, RoomNotAvailableException;

    List<RoomBooking> findByRoomAndBookingEndAfterAndBookingStartBefore(Room room, LocalDate date, LocalTime start, LocalTime endTime);

    void deleteBookingById(Long BookingId) throws RoomNotFoundException, RoomDeletionNotPossibleException;

    List<Room> getAvailableRooms(LocalDate date, LocalTime start, LocalTime endTime) throws ExecutionException, InterruptedException;

    boolean isRoomAvailable(Room room, LocalDate date, LocalTime startDateTime, LocalTime endtime);

    void deleteBooking(Long id) throws RoomNotFoundException, RoomDeletionNotPossibleException;

    List<RoomBooking> getMyBookingHistory(Long employeeId);

    RoomBooking save(RoomBooking roomBooking);
}



