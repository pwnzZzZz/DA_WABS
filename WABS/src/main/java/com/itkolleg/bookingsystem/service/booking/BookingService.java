package com.itkolleg.bookingsystem.service.booking;

import com.itkolleg.bookingsystem.domains.booking.Booking;
import com.itkolleg.bookingsystem.domains.Employee;

import java.sql.Date;
import java.util.List;

public interface BookingService<T, I> {
    Booking createBooking(Employee employee, T bookable);

    Booking createBooking(Long employeeID, T bookable);

    List<Booking> getAllBookings();

    List<Booking> getAllBookingsByEmployee(Employee employee);

    List<Booking> getAllBookingsByEmployeeAndDate(Employee employee, Date searchDate);

    List<Booking> getAllBookingsByDate(Date searchDate);

    List<Booking> getAllBookingsByEmployeeID(Long employeeID, Date searchDate);

    List<Booking> getAllBookingsById(Long id);

    List<Booking> getAllBookingsByBookable(T bookable);

    Booking updateBookingByEmployeeId(Long employeeID);

    Booking updateBookingById(Long bookableID);

    Booking updateBookingByEmployee(Long employee);

    Booking updateBookingStartAndEnd(Date Start, Date End);
}
