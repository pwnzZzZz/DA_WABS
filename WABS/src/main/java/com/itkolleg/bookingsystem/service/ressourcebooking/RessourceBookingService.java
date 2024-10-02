package com.itkolleg.bookingsystem.service.ressourcebooking;

import com.itkolleg.bookingsystem.domains.booking.RessourceBooking;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.exceptions.ressourceExceptions.RessourceNotAvailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RessourceBookingService {

    Logger logger = LoggerFactory.getLogger(RessourceBookingService.class);

    RessourceBooking addRessourceBooking(RessourceBooking ressourceBooking) throws RessourceNotAvailableException, ResourceNotFoundException;

    List<RessourceBooking> getAllBookings();


    List<RessourceBooking> getBookingsByEmployeeId(Long employeeId);

    List<RessourceBooking> getBookingsByRessource(Ressource ressource);

    List<RessourceBooking> getBookingsByEmployee(Employee employee);

    List<RessourceBooking> getBookingsByDate(LocalDate date);

    RessourceBooking getBookingById(Long bookingId) throws ResourceNotFoundException;

    RessourceBooking updateBookingById(Long bookingId, RessourceBooking updatedBooking) throws ResourceNotFoundException, RessourceNotAvailableException;

    RessourceBooking updateBooking(RessourceBooking booking) throws ResourceNotFoundException, RessourceNotAvailableException;

    List<RessourceBooking> findByRessourceAndBookingEndAfterAndBookingStartBefore(Ressource ressource, LocalDate date, LocalTime start, LocalTime endTime);

    void deleteBookingById(Long bookingID) throws ResourceNotFoundException, ResourceDeletionFailureException;

    List<Ressource> getAvailableRessources(LocalDate date, LocalTime start, LocalTime endTime) throws ExecutionException, InterruptedException;

    boolean isRessourceAvailable(Ressource ressource, LocalDate date, LocalTime startDateTime, LocalTime endDateTime);

    void deleteBooking(Long id) throws ResourceNotFoundException, ResourceDeletionFailureException;

    List<RessourceBooking> getMyBookingHistory(Long employeeId);

    RessourceBooking save(RessourceBooking booking);
}