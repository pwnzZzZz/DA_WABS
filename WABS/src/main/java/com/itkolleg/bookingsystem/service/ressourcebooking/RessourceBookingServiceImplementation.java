package com.itkolleg.bookingsystem.service.ressourcebooking;


import com.itkolleg.bookingsystem.domains.booking.RessourceBooking;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.exceptions.ressourceExceptions.RessourceNotAvailableException;
import com.itkolleg.bookingsystem.repos.ressource.DBAccessRessource;
import com.itkolleg.bookingsystem.repos.ressourcebooking.RessourceBookingRepo;
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
 * Die Klasse RessourceBookingServiceImplementation implementiert das RessourceBookingService-interface und stellt somit die konkrete Implementierung der Service-Methoden für ressource-Buchungen bereit.
 *
 * @author Manuel Payer
 * @version 1.0
 * @since 29.06.2023
 */
@Service
public class RessourceBookingServiceImplementation implements RessourceBookingService {

    private final RessourceBookingRepo ressourceBookingRepo;
    private final DBAccessRessource ressourceRepo;

    /**
     * Konstruktor der Klasse ReesourceBookingRepo. Benötigt foglende Parameter:
     *
     * @param ressourceBookingRepo vom Typ RessourceBookingRepo
     * @param ressourceRepo        vom Typ RessourceRepo
     */
    public RessourceBookingServiceImplementation(RessourceBookingRepo ressourceBookingRepo, DBAccessRessource ressourceRepo) {
        this.ressourceBookingRepo = ressourceBookingRepo;
        this.ressourceRepo = ressourceRepo;
    }

    /**
     * Fügt eine Ressourcenbuchung hinzu.
     *
     * @param booking Das ressourcebooking-Objekt, das die zu erstellende Buchung repräsentiert.
     * @return Das erstellte ressourcebooking-Objekt.
     * @throws RessourceNotAvailableException Wird ausgelöst, wenn die ressource für den Buchungszeitraum nicht verfügbar ist.
     * @throws ResourceNotFoundException      Wird ausgelöst, wenn die ressource nicht gefunden wurde.
     * @throws IllegalArgumentException       Wird ausgelöst, wenn eine Buchung für ein vergangenes Datum erstellt werden soll.
     */
    @Override
    public RessourceBooking addRessourceBooking(RessourceBooking booking) throws RessourceNotAvailableException, ResourceNotFoundException {

        List<RessourceBooking> bookings = this.ressourceBookingRepo.getBookingsByRessourceAndDateAndBookingTimeBetween(booking.getRessource(), booking.getDate(), booking.getStart(), booking.getEndTime());
        LocalDate currentDate = LocalDate.now();
        System.out.println("booking date: " + booking.getDate());
        System.out.println("Current date: " + LocalDate.now());
        //Check if ressource is available for the date and time chosen
        if (!bookings.isEmpty()) {
            throw new RessourceNotAvailableException("ressource not available for booking period");
        }
        // Check if booking is for a past date
        if (booking.getDate().isBefore(currentDate)) {
            throw new IllegalArgumentException("Cannot create booking for a past date");
        }
        return this.ressourceBookingRepo.addBooking(booking);
    }

    /**
     * Gibt eine Liste aller RessourceBookings zurück.
     *
     * @return Eine Liste aller RessourceBookings.
     */
    @Override
    public List<RessourceBooking> getAllBookings() {
        return this.ressourceBookingRepo.getAllBookings();
    }

    /**
     * Gibt eine Liste von RessourceBookings zurück, die mit der angegebenen Mitarbeiter-ID verknüpft sind.
     *
     * @param employeeId Die ID des Mitarbeiters.
     * @return Eine Liste von RessourceBookings, die mit dem angegebenen Mitarbeiter verknüpft sind.
     */
    @Override
    public List<RessourceBooking> getBookingsByEmployeeId(Long employeeId) {
        return this.ressourceBookingRepo.getBookingsByEmployeeId(employeeId);
    }

    /**
     * Gibt eine Liste von RessourceBookings zurück, die mit dem angegebenen Mitarbeiter verknüpft sind.
     *
     * @param employee Der Mitarbeiter.
     * @return Eine Liste von RessourceBookings, die mit dem angegebenen Mitarbeiter verknüpft sind.
     */
    @Override
    public List<RessourceBooking> getBookingsByEmployee(Employee employee) {
        return this.ressourceBookingRepo.getBookingsByEmployee(employee);
    }

    /**
     * Gibt eine Liste von RessourceBookings zurück, die mit der angegebenen ressource verknüpft sind.
     *
     * @param ressource Die ressource.
     * @return Eine Liste von RessourceBookings, die mit der angegebenen ressource verknüpft sind.
     */
    @Override
    public List<RessourceBooking> getBookingsByRessource(Ressource ressource) {
        return this.ressourceBookingRepo.getBookingsByRessource(ressource);
    }

    /**
     * Gibt eine Liste von RessourceBookings zurück, die mit dem angegebenen Datum verknüpft sind.
     *
     * @param date Das Datum.
     * @return Eine Liste von RessourceBookings, die mit dem angegebenen Datum verknüpft sind.
     */
    @Override
    public List<RessourceBooking> getBookingsByDate(LocalDate date) {
        LocalTime startOfDay = LocalTime.from(date.atStartOfDay());
        LocalTime endOfDay = startOfDay.plusHours(24).minusSeconds(1);
        return this.ressourceBookingRepo.getBookingByDateAndByStartBetween(date, startOfDay, endOfDay);
    }

    /**
     * Ruft das ressourcebooking mit der angegebenen Buchungs-ID ab.
     *
     * @param bookingId Die ID der Buchung.
     * @return Das ressourcebooking mit der angegebenen ID.
     * @throws ResourceNotFoundException Wenn keine Buchung mit der angegebenen ID gefunden wurde.
     */
    @Override
    public RessourceBooking getBookingById(Long bookingId) throws ResourceNotFoundException {
        Optional<RessourceBooking> optionalBooking = this.ressourceBookingRepo.getBookingByBookingId(bookingId);
        if (optionalBooking.isPresent()) {
            return optionalBooking.get();
        } else {
            throw new ResourceNotFoundException("booking with ID " + bookingId + " not found.");
        }
    }

    /**
     * Aktualisiert das ressourcebooking mit der angegebenen Buchungs-ID.
     *
     * @param bookingId      Die ID der Buchung.
     * @param updatedBooking Das aktualisierte ressourcebooking-Objekt.
     * @return Das aktualisierte ressourcebooking.
     * @throws ResourceNotFoundException      Wenn keine Buchung mit der angegebenen ID gefunden wurde.
     * @throws RessourceNotAvailableException Wenn die ressource für den aktualisierten Buchungszeitraum nicht verfügbar ist.
     */
    public RessourceBooking updateBookingById(Long bookingId, RessourceBooking updatedBooking) throws ResourceNotFoundException, RessourceNotAvailableException {
        Optional<RessourceBooking> booking = this.ressourceBookingRepo.getBookingByBookingId(bookingId);
        if (booking.isEmpty()) {
            throw new ResourceNotFoundException("booking not found for id: " + bookingId);
        }

        Ressource ressource = booking.get().getRessource();
        LocalDate date = booking.get().getDate();
        LocalTime start = booking.get().getStart();
        LocalTime endTime = booking.get().getEndTime();
        if (isRessourceAvailable(ressource, date, start, endTime)) {
            updatedBooking.setId(bookingId);
        }
        return this.ressourceBookingRepo.updateBooking(updatedBooking);
    }

    /**
     * Aktualisiert das ressourcebooking mit den neuen Informationen.
     *
     * @param booking Das aktualisierte ressourcebooking-Objekt.
     * @return Das aktualisierte ressourcebooking.
     * @throws RessourceNotAvailableException Wenn die ressource für den aktualisierten Buchungszeitraum nicht verfügbar ist.
     * @throws ResourceNotFoundException      Wenn keine Buchung mit der angegebenen ID gefunden wurde.
     */
    @Override
    public RessourceBooking updateBooking(RessourceBooking booking) throws RessourceNotAvailableException, ResourceNotFoundException {

        try {
            RessourceBooking existingBooking = this.ressourceBookingRepo.getBookingByBookingId(booking.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("booking not found for id: " + booking.getId()));
            // Check if the ressource is available for the updated booking period
            List<RessourceBooking> bookings = ressourceBookingRepo.getBookingsByRessourceAndDateAndBookingTimeBetween(booking.getRessource(), booking.getDate(), booking.getStart(), booking.getEndTime());
            bookings.remove(existingBooking);
            if (!bookings.isEmpty()) {
                throw new RessourceNotAvailableException("ressource not available for booking period");
            }
            existingBooking.setEmployee(booking.getEmployee());
            existingBooking.setRessource(booking.getRessource());
            existingBooking.setDate(booking.getDate());
            existingBooking.setStart(booking.getStart());
            existingBooking.setEndTime(booking.getEndTime());
            existingBooking.setCreatedOn(LocalDateTime.now());
            return this.ressourceBookingRepo.updateBooking(existingBooking);
        } catch (DataAccessException e) {
            throw new ResourceNotFoundException("Database access error occurred for id: " + booking.getId(), e);
        }

    }

    /**
     * Gibt eine Liste von RessourceBookings zurück, die mit der angegebenen ressource, dem Datum und der Buchungszeit übereinstimmen.
     *
     * @param ressource Die ressource.
     * @param date      Das Datum.
     * @param start     Die Startzeit der Buchung.
     * @param endTime   Die Endzeit der Buchung.
     * @return Eine Liste von RessourceBookings, die mit der angegebenen ressource, dem Datum und der Buchungszeit übereinstimmen.
     */
    @Override
    public List<RessourceBooking> findByRessourceAndBookingEndAfterAndBookingStartBefore(Ressource ressource, LocalDate date, LocalTime start, LocalTime endTime) {
        return ressourceBookingRepo.getBookingsByRessourceAndDateAndBookingTimeBetween(ressource, date, start, endTime);
    }

    /**
     * Löscht die Buchung mit der angegebenen Buchungs-ID.
     *
     * @param bookingId Die ID der Buchung.
     * @throws ResourceDeletionFailureException Wenn das Löschen der Buchung fehlschlägt.
     * @throws ResourceNotFoundException        Wenn keine Buchung mit der angegebenen ID gefunden wurde.
     */
    @Override
    public void deleteBookingById(Long bookingId) throws ResourceDeletionFailureException, ResourceNotFoundException {
        Optional<RessourceBooking> booking = this.ressourceBookingRepo.getBookingByBookingId(bookingId);
        if (booking.isEmpty()) {
            throw new ResourceDeletionFailureException("booking not Found!");
        }
        ressourceBookingRepo.deleteBookingById(bookingId);
    }

    /**
     * Gibt eine Liste von verfügbaren Ressourcen für das angegebene Datum und die angegebene Buchungszeit zurück.
     *
     * @param date    Das Datum.
     * @param start   Die Startzeit der Buchung.
     * @param endTime Die Endzeit der Buchung.
     * @return Eine Liste von verfügbaren Ressourcen für das angegebene Datum und die angegebene Buchungszeit.
     * @throws ExecutionException   Wenn ein Fehler bei der Ausführung auftritt.
     * @throws InterruptedException Wenn der Thread während des Wartens unterbrochen wird.
     */
    @Override
    public List<Ressource> getAvailableRessources(LocalDate date, LocalTime start, LocalTime endTime) throws ExecutionException, InterruptedException {
        return this.ressourceRepo.getAllRessource().stream()
                .filter(ressource -> ressourceBookingRepo.getBookingsByRessourceAndDateAndBookingTimeBetween(ressource, date, start, endTime).isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Überprüft, ob die angegebene ressource für den angegebenen Zeitraum verfügbar ist.
     *
     * @param ressource     Die ressource.
     * @param date          Das Datum.
     * @param startDateTime Die Startzeit des Zeitraums.
     * @param endDateTime   Die Endzeit des Zeitraums.
     * @return True, wenn die ressource verfügbar ist, andernfalls False.
     */
    @Override
    public boolean isRessourceAvailable(Ressource ressource, LocalDate date, LocalTime startDateTime, LocalTime endDateTime) {
        List<RessourceBooking> bookings = this.ressourceBookingRepo.getBookingsByRessourceAndDateAndBookingTimeBetween(ressource, date, startDateTime, endDateTime);
        return bookings.isEmpty();
    }

    /**
     * Löscht die Buchung mit der angegebenen ID.
     *
     * @param id Die ID der Buchung.
     * @throws ResourceDeletionFailureException Wenn das Löschen der Buchung fehlschlägt.
     * @throws ResourceNotFoundException        Wenn keine Buchung mit der angegebenen ID gefunden wurde.
     */
    @Override
    public void deleteBooking(Long id) throws ResourceDeletionFailureException, ResourceNotFoundException {
        this.ressourceBookingRepo.deleteBookingById(id);
    }

    /**
     * Gibt eine Liste von RessourceBookings zurück, die mit dem angegebenen Mitarbeiter verknüpft sind.
     *
     * @param employeeId Die ID des Mitarbeiters.
     * @return Eine Liste von RessourceBookings, die mit dem angegebenen Mitarbeiter verknüpft sind.
     */
    @Override
    public List<RessourceBooking> getMyBookingHistory(Long employeeId) {
        return this.ressourceBookingRepo.getBookingsByEmployeeId(employeeId);
    }

    /**
     * Speichert die angegebene ressourcebooking in der Datenbank.
     *
     * @param booking Das zu speichernde ressourcebooking-Objekt.
     * @return Das gespeicherte ressourcebooking.
     */
    @Override
    public RessourceBooking save(RessourceBooking booking) {
        return this.ressourceBookingRepo.save(booking);
    }
}