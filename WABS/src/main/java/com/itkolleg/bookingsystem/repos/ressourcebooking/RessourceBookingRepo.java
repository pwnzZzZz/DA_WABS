package com.itkolleg.bookingsystem.repos.ressourcebooking;

import com.itkolleg.bookingsystem.domains.booking.RessourceBooking;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.exceptions.ressourceExceptions.RessourceNotAvailableException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


public interface RessourceBookingRepo {
    RessourceBooking addBooking(RessourceBooking booking) throws RessourceNotAvailableException, ResourceNotFoundException;

    List<RessourceBooking> getAllBookings();

    Optional<RessourceBooking> getBookingByBookingId(Long id);

    List<RessourceBooking> getBookingsByEmployeeId(Long employeeId);

    List<RessourceBooking> getBookingsByRessource(Ressource ressource);

    List<RessourceBooking> getBookingsByRessourceId(Long id);

    List<RessourceBooking> getBookingsByEmployee(Employee employee);

    List<RessourceBooking> getBookingsByEmployeeAndDate(Employee employee, LocalDate date);

    List<RessourceBooking> getBookingsByRessourceAndDate(Ressource ressource, LocalDate date);

    List<RessourceBooking> getBookingByDate(LocalDate date);

    RessourceBooking updateBookingById(Long id, RessourceBooking updatedBooking) throws ResourceNotFoundException;

    RessourceBooking updateBooking(RessourceBooking updatedBooking) throws ResourceNotFoundException;

    void deleteBookingById(Long id) throws ResourceDeletionFailureException;

    List<Ressource> getAvailableRessources(LocalDate date, LocalTime start, LocalTime end);

    boolean isRessourceAvailable(Ressource ressource, LocalDate date, LocalTime start, LocalTime end);

    List<RessourceBooking> getBookingsByRessourceAndDateAndBookingTimeBetween(Ressource ressource, LocalDate date, LocalTime startDateTime, LocalTime endDateTime);

    List<RessourceBooking> getBookingByDateAndByStartBetween(LocalDate date, LocalTime startOfDay, LocalTime endOfDay);

    RessourceBooking save(RessourceBooking booking);
}