package com.itkolleg.bookingsystem.service.deskbooking;

import com.itkolleg.bookingsystem.domains.booking.DeskBooking;
import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.exceptions.CustomIllegalArgumentException;
import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.exceptions.DeskNotAvailableException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


public interface DeskBookingService {

    DeskBooking addDeskBooking(DeskBooking deskBooking) throws DeskNotAvailableException, ResourceNotFoundException, CustomIllegalArgumentException;

    List<DeskBooking> getAllBookings() throws ResourceNotFoundException;

    List<DeskBooking> searchBookings(Long employee, LocalDate date) throws ResourceNotFoundException;

    List<DeskBooking> searchBookings(Long employeeId, Long deskId, LocalDate date) throws ResourceNotFoundException;

    List<DeskBooking> getBookingsByEmployeeId(Long employeeId) throws ResourceNotFoundException;

    List<DeskBooking> getBookingByDesk(Long desk) throws ResourceNotFoundException;

    List<DeskBooking> getBookingsByEmployee(Long employee) throws ResourceNotFoundException;

    List<DeskBooking> getBookingsByEmployeeNick(String nick) throws ResourceNotFoundException;

    List<DeskBooking> getBookingsByEmployeeAndDate(Long employee, LocalDate date) throws ResourceNotFoundException;

    List<DeskBooking> getBookingsByDeskAndDate(Long  desk, LocalDate date) throws ResourceNotFoundException;

    List<DeskBooking> getBookingsByDate(LocalDate date) throws ResourceNotFoundException;

    Optional<DeskBooking> getBookingById(Long bookingId) throws ResourceNotFoundException;

    DeskBooking updateBookingById(Long bookingId, DeskBooking updatedBooking) throws ResourceNotFoundException, DeskNotAvailableException, CustomIllegalArgumentException;

    DeskBooking updateBooking(DeskBooking booking) throws ResourceNotFoundException, DeskNotAvailableException, CustomIllegalArgumentException;

    void deleteBookingById(Long bookingID) throws ResourceNotFoundException, ResourceDeletionFailureException;

    List<DeskBooking> getBookingHistoryByEmployeeId(Long employeeId ) throws ResourceNotFoundException;

    List<DeskBooking> getBookingByDateAndByStartBetween(LocalDate date, LocalTime startOfDay, LocalTime endOfDay) throws ResourceNotFoundException;

    DeskBooking save(DeskBooking booking) throws ResourceNotFoundException, DeskNotAvailableException;

    List<Desk> getAvailableDesks(LocalDate date, LocalTime start, LocalTime end, Long specificDeskId) throws ResourceNotFoundException;
}