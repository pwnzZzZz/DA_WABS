package com.itkolleg.bookingsystem.repos.roombooking;

import com.itkolleg.bookingsystem.domains.booking.RoomBooking;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.Room;
import com.itkolleg.bookingsystem.exceptions.roomExceptions.RoomDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.roomExceptions.RoomNotAvailableException;
import com.itkolleg.bookingsystem.exceptions.roomExceptions.RoomNotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface RoomBookingRepo {
    RoomBooking addBooking(RoomBooking booking) throws RoomNotAvailableException, RoomNotFoundException;


    List<Room> getAllRooms();

    List<RoomBooking> getAllBookings();

    Optional<RoomBooking> getBookingByBookingId(Long id);

    List<RoomBooking> getBookingsByRoom(Room room);

    List<RoomBooking> getBookingsByRoomId(Long roomId);

    List<RoomBooking> getBookingsByDate(LocalDate date);

    List<RoomBooking> getBookingsByEmployee(Employee employee);

    List<RoomBooking> getBookingsByEmployeeId(Long employeeId);

    List<RoomBooking> getBookingsByRoomAndDate(Room room, LocalDate date);

    List<RoomBooking> getBookingsByRoomAndDateAndBookingTimeBetween(Room room, LocalDate date, LocalTime start, LocalTime endTime);

    RoomBooking updateBookingById(Long id, RoomBooking updatedBooking) throws RoomNotFoundException;

    RoomBooking updateBooking(RoomBooking updatedBooking) throws RoomNotFoundException;

    void deleteBookingById(Long id) throws RoomDeletionNotPossibleException;

    List<Room> getAvailableRooms(LocalDate date, LocalTime start, LocalTime endTime);

    boolean isRoomAvailable(Room room, LocalDate date, LocalTime start, LocalTime endTime);


    List<RoomBooking> getBookingsByEmployeeAndDate(Employee employee, LocalDate date);

    List<RoomBooking> getBookingsByDateAndByStartBetween(LocalDate date, LocalTime start, LocalTime endTime);

    List<RoomBooking> getBookingsByEmployeeIdAndDateAndRoomId(Long employeeId, LocalDate date, Long roomId);

    RoomBooking save(RoomBooking booking);
}
