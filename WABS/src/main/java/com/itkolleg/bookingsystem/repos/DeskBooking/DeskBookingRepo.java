package com.itkolleg.bookingsystem.repos.deskbooking;

import com.itkolleg.bookingsystem.domains.Timeslot;
import com.itkolleg.bookingsystem.domains.booking.DeskBooking;
import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.exceptions.CustomIllegalArgumentException;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.DeskNotAvailableException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


public interface DeskBookingRepo {

    DeskBooking addBooking(DeskBooking booking) throws DeskNotAvailableException, ResourceNotFoundException, CustomIllegalArgumentException;

    List<DeskBooking> getAllBookings() throws ResourceNotFoundException;

    List<DeskBooking> searchBookings(Optional<Employee> employee, Optional<Desk> desk, Optional<LocalDate> date, Optional<LocalTime> start, Optional<LocalTime> endTime, Optional<Timeslot> timeslot);

    List<DeskBooking> searchBookings(Long employeeId, Long deskId, LocalDate date) throws ResourceNotFoundException;

    Optional<DeskBooking> getBookingByBookingId(Long bookingId) throws ResourceNotFoundException;

    List<DeskBooking> getBookingsByEmployee(Long employeeId) throws ResourceNotFoundException;

    List<DeskBooking> getBookingByDesk(Long deskId) throws ResourceNotFoundException;

    List<DeskBooking> getBookingsByDeskAndDate(Long deskId, LocalDate date) throws ResourceNotFoundException;

    List<DeskBooking> getBookingByDate(LocalDate date) throws ResourceNotFoundException;

    List<DeskBooking> getBookingsByEmployeeAndDate(Long employeeId, LocalDate date) throws ResourceNotFoundException;

    List<DeskBooking> getBookingsByEmployeeAndDesk(Long employeeId, Long deskId) throws ResourceNotFoundException;

    List<DeskBooking> getBookingsByDeskAndDateAndBookingTimeRange(Long deskId, LocalDate date, LocalTime startDateTime, LocalTime endDateTime) throws ResourceNotFoundException;

    List<DeskBooking> getBookingsByDateAndTimeRange(LocalDate date, LocalTime bookingStart, LocalTime bookingEnd) throws ResourceNotFoundException;

    List<DeskBooking> getBookingsByEmployeeIdAndDeskIdAndDate(Long employeeId, Long deskId, LocalDate date) throws ResourceNotFoundException;

    DeskBooking updateBookingById(Long bookingId, DeskBooking updatedBooking) throws ResourceNotFoundException;

    DeskBooking updateBooking(DeskBooking updatedBooking) throws ResourceNotFoundException, DeskNotAvailableException, CustomIllegalArgumentException;

    void deleteBookingById(Long Id) throws ResourceNotFoundException, ResourceDeletionFailureException;

    List<DeskBooking> getBookingHistoryByEmployeeId(Long employeeId ) throws ResourceNotFoundException;

    DeskBooking save(DeskBooking booking) throws ResourceNotFoundException, DeskNotAvailableException;

    List<Desk> getAvailableDesks(LocalDate date, LocalTime start, LocalTime end, Long specificDeskId);

    boolean existsById(Long bookingId);

    List<DeskBooking> getBookingsByEmployeeNick(String nick);
}