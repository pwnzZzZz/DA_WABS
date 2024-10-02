package com.itkolleg.bookingsystem.repos.ressourcebooking;

import com.itkolleg.bookingsystem.domains.booking.RessourceBooking;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.Ressource;
import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.exceptions.employeeExceptions.EmployeeNotFoundException;
import com.itkolleg.bookingsystem.exceptions.ressourceExceptions.RessourceNotAvailableException;
import com.itkolleg.bookingsystem.repos.employee.EmployeeJPARepo;
import com.itkolleg.bookingsystem.repos.ressource.RessourceJPARepo;
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
 * Die gegebene Klasse ist eine Implementierung des Interfaces "RessourceBookingRepo". Sie stellt Methoden zum Hinzufügen, Abrufen, Aktualisieren und Löschen von Ressourcenbuchungen (Resource booking) bereit.
 * Die Klasse arbeitet mit den JPA-Repositorien "RessourceBookingJPARepo", "RessourceJPARepo" und "EmployeeJPARepo" zusammen, um auf die Datenbank zuzugreifen.
 *
 * @author Manuel Payer
 * @version 1.0
 * @since 29.06.2023
 */
@Component
@ComponentScan({"com.itkolleg.repos"})
public class RessourceBookingRepo_JPAH2 implements RessourceBookingRepo {
    private static final Logger logger = LoggerFactory.getLogger(RessourceBookingRepo_JPAH2.class);
    private final RessourceBookingJPARepo ressourceBookingJPARepo;
    private final RessourceJPARepo ressourceJPARepo;
    private final EmployeeJPARepo employeeJPARepo;

    /**
     * Konstruktor der Klasse RessourceBookingRepo_JPAH2 und benötigt folgende Parameter:
     *
     * @param ressourceBookingJPARepo vom Typ RessourecBookingRepo_JPAH2
     * @param ressourceJPARepo        vom Typ RessourceJPARepo
     * @param employeeJPARepo         vom Typ EmployeeJPARepo
     */
    public RessourceBookingRepo_JPAH2(RessourceBookingJPARepo ressourceBookingJPARepo, RessourceJPARepo ressourceJPARepo, EmployeeJPARepo employeeJPARepo) {
        this.ressourceBookingJPARepo = ressourceBookingJPARepo;
        this.ressourceJPARepo = ressourceJPARepo;
        this.employeeJPARepo = employeeJPARepo;
    }

    /**
     * Die Methode addBooking fügt eine Ressourcenbuchung hinzu. Sie akzeptiert ein Objekt vom Typ ressourcebooking als Parameter und gibt ein Objekt vom selben Typ zurück.
     * Die Methode kann zwei Ausnahmen auslösen: ResourceNotFoundException und RessourceNotAvailableException.
     *
     * @param ressourceBooking vom Typ ressourcebooking
     * @return Das Objekt vom Typ ressourcebooking, das erfolgreich zur Datenbank hinzugefügt wurde.
     * @throws ResourceNotFoundException      Wird ausgelöst, wenn die ressource nicht gefunden werden kann.
     * @throws RessourceNotAvailableException Wird ausgelöst, wenn die ressource für den Buchungszeitraum nicht verfügbar ist.
     */
    @Override
    public RessourceBooking addBooking(RessourceBooking ressourceBooking) throws ResourceNotFoundException, RessourceNotAvailableException {
        // Check for null values
        if (ressourceBooking == null || ressourceBooking.getRessource() == null) {
            throw new IllegalArgumentException("The ressourcebooking or ressource cannot be null.");
        }

        // Load the associated ressource entity from the database
        Long ressourceid = ressourceBooking.getRessource().getId();
        Ressource ressource = this.ressourceJPARepo.findRessourceById(ressourceid);

        // Check if the ressource is available for the booking period
        if (!isRessourceAvailable(ressource, ressourceBooking.getDate(), ressourceBooking.getStart(), ressourceBooking.getEndTime())) {
            throw new RessourceNotAvailableException("ressource not available for booking period");
        }

        // Create a new booking entity
        RessourceBooking booking = new RessourceBooking();
        booking.setEmployee(ressourceBooking.getEmployee());
        booking.setRessource(ressource);
        booking.setDate(ressourceBooking.getDate());
        booking.setStart(ressourceBooking.getStart());
        booking.setEndTime(ressourceBooking.getEndTime());

        // Set created and updated timestamps
        booking.setCreatedOn(LocalDateTime.now());
        booking.setUpdatedOn(LocalDateTime.now());

        // Save the booking
        try {
            return this.ressourceBookingJPARepo.save(booking);
        } catch (Exception e) {
            throw new RuntimeException("Error saving the booking to the database", e);
        }
    }

    /**
     * Diese Methode liefert eine Liste aller Buchungen zurück.
     *
     * @return Liste von ressourcebooking
     */
    @Override
    public List<RessourceBooking> getAllBookings() {
        return this.ressourceBookingJPARepo.findAll();
    }

    /**
     * Die Methode getBookingsByBookingId sucht nach einer Ressourcenbuchung anhand einer angegebenen Buchungs-ID.
     * Sie akzeptiert eine Buchungs-ID vom Typ Long als Parameter und gibt ein Optional-Objekt vom Typ ressourcebooking zurück.
     * Die Methode kann eine ResourceNotFoundException auslösen.
     *
     * @param id vom Typ Long
     * @return Ein Optional-Objekt vom Typ ressourcebooking, das die gefundenen Buchung enthält, falls eine Buchung mit der angegebenen ID gefunden wurde. Andernfalls enthält das Optional-Objekt den Wert null.
     */
    public Optional<RessourceBooking> getBookingsByBookingId(Long id) {
        return this.ressourceBookingJPARepo.findById(id);
    }

    /**
     * Die Methode getBookingsByEmployeeId sucht nach Ressourcenbuchungen basierend auf einer angegebenen Mitarbeiter-ID.
     * Sie akzeptiert eine Mitarbeiter-ID vom Typ Long als Parameter und gibt eine Liste von Ressourcenbuchungen zurück.
     *
     * @param employeeId Die ID des Mitarbeiters, für den die Buchungen gesucht werden sollen.
     * @return Eine Liste von ressourcebooking-Objekten, die die gefundenen Buchungen für den angegebenen Mitarbeiter enthalten. Wenn keine Buchungen für den Mitarbeiter gefunden werden, wird eine leere Liste zurückgegeben.
     */
    @Override
    public List<RessourceBooking> getBookingsByEmployeeId(Long employeeId) {
        return this.ressourceBookingJPARepo.getBookingsByEmployeeId(employeeId);
    }

    /**
     * Die Methode getBookingsByRessource sucht nach Ressourcenbuchungen basierend auf einer angegebenen ressource. Sie akzeptiert ein ressource-Objekt als Parameter und gibt eine Liste von Ressourcenbuchungen zurück.
     *
     * @param ressource Das ressource-Objekt, für das die Buchungen gesucht werden sollen.
     * @return Eine Liste von ressourcebooking-Objekten, die die gefundenen Buchungen für die angegebene ressource enthalten. Wenn keine Buchungen für die ressource gefunden werden, wird eine leere Liste zurückgegeben.
     */
    @Override
    public List<RessourceBooking> getBookingsByRessource(Ressource ressource) {
        return this.ressourceBookingJPARepo.getBookingsByRessource(ressource);
    }

    /**
     * Die Methode getBookingsByRessourceId sucht nach Ressourcenbuchungen basierend auf einer angegebenen Ressourcen-ID.
     * Sie akzeptiert eine Ressourcen-ID als Parameter und gibt eine Liste von Ressourcenbuchungen zurück.
     *
     * @param id Die ID der ressource, für die die Buchungen gesucht werden sollen.
     * @return Eine Liste von ressourcebooking-Objekten, die die gefundenen Buchungen für die angegebene Ressourcen-ID enthalten. Wenn keine Buchungen für die ressource gefunden werden, wird eine leere Liste zurückgegeben.
     */
    public List<RessourceBooking> getBookingsByRessourceId(Long id) {
        return this.ressourceBookingJPARepo.getBookingsByRessourceId(id);
    }

    /**
     * Die Methode getBookingByBookingId sucht nach einer Ressourcenbuchung anhand einer bestimmten Buchungs-ID.
     * Sie akzeptiert eine Buchungs-ID als Parameter und gibt eine Optionale ressourcebooking-Instanz zurück.
     *
     * @param id Die ID der Buchung, die gesucht werden soll.
     * @return Eine Optionale ressourcebooking-Instanz, die die gefundene Buchung für die angegebene Buchungs-ID enthält. Wenn keine Buchung mit der angegebenen ID gefunden wird, wird eine leere Option zurückgegeben.
     */
    @Override
    public Optional<RessourceBooking> getBookingByBookingId(Long id) {
        return this.ressourceBookingJPARepo.findById(id);
    }

    /**
     * Die Methode getBookingsByEmployee sucht nach Ressourcenbuchungen für einen bestimmten Mitarbeiter.
     * Sie akzeptiert einen Mitarbeiter als Parameter und gibt eine Liste von ressourcebooking-Objekten zurück, die dem angegebenen Mitarbeiter zugeordnet sind.
     *
     * @param employee Der Mitarbeiter, für den die Buchungen gesucht werden sollen.
     * @return Eine Liste von ressourcebooking-Objekten, die den gefundenen Buchungen für den angegebenen Mitarbeiter entsprechen. Wenn keine Buchungen für den Mitarbeiter gefunden werden, wird eine leere Liste zurückgegeben.
     */
    @Override
    public List<RessourceBooking> getBookingsByEmployee(Employee employee) {
        return this.ressourceBookingJPARepo.getBookingsByEmployee(employee);
    }

    /**
     * Die Methode getBookingsByEmployeeAndDate sucht nach Ressourcenbuchungen für einen bestimmten Mitarbeiter an einem bestimmten Datum.
     * Sie akzeptiert einen Mitarbeiter und ein Datum als Parameter und gibt eine Liste von ressourcebooking-Objekten zurück, die dem angegebenen Mitarbeiter an dem angegebenen Datum zugeordnet sind.
     *
     * @param employee Der Mitarbeiter, für den die Buchungen gesucht werden sollen.
     * @param date     Das Datum, für das die Buchungen gesucht werden sollen.
     * @return Eine Liste von ressourcebooking-Objekten, die den gefundenen Buchungen für den angegebenen Mitarbeiter und das angegebene Datum entsprechen.
     * Wenn keine Buchungen für den Mitarbeiter und das Datum gefunden werden, wird eine leere Liste zurückgegeben.
     */
    @Override
    public List<RessourceBooking> getBookingsByEmployeeAndDate(Employee employee, LocalDate date) {
        return this.ressourceBookingJPARepo.getBookingsByEmployeeAndDate(employee, date);
    }

    /**
     * Die Methode getBookingsByRessourceAndDate sucht nach Ressourcenbuchungen für eine bestimmte ressource an einem bestimmten Datum.
     * Sie akzeptiert eine ressource und ein Datum als Parameter und gibt eine Liste von ressourcebooking-Objekten zurück, die der angegebenen ressource an dem angegebenen Datum zugeordnet sind.
     *
     * @param ressource Die ressource, für die die Buchungen gesucht werden sollen.
     * @param date      Das Datum, für das die Buchungen gesucht werden sollen.
     * @return Eine Liste von ressourcebooking-Objekten, die den gefundenen Buchungen für die angegebene ressource und das angegebene Datum entsprechen.
     * Wenn keine Buchungen für die ressource und das Datum gefunden werden, wird eine leere Liste zurückgegeben.
     */
    @Override
    public List<RessourceBooking> getBookingsByRessourceAndDate(Ressource ressource, LocalDate date) {
        return this.ressourceBookingJPARepo.getBookingsByRessourceAndDate(ressource, date);
    }

    /**
     * Die Methode getBookingByDate sucht nach Ressourcenbuchungen für ein bestimmtes Datum. Sie akzeptiert ein Datum als Parameter und gibt eine Liste von ressourcebooking-Objekten zurück, die dem angegebenen Datum zugeordnet sind.
     *
     * @param date Das Datum, für das die Buchungen gesucht werden sollen.
     * @return Eine Liste von ressourcebooking-Objekten, die den gefundenen Buchungen für das angegebene Datum entsprechen. Wenn keine Buchungen für das Datum gefunden werden, wird eine leere Liste zurückgegeben.
     */
    @Override
    public List<RessourceBooking> getBookingByDate(LocalDate date) {
        return this.ressourceBookingJPARepo.getBookingsByDate(date);
    }

    /**
     * Die Methode updateBookingById wird verwendet, um eine vorhandene Ressourcenbuchung anhand ihrer ID zu aktualisieren.
     * Sie akzeptiert die ID der Buchung, sowie ein aktualisiertes ressourcebooking-Objekt als Parameter und gibt das aktualisierte ressourcebooking-Objekt zurück.
     *
     * @param ressourceBookingId      Die ID der Buchung, die aktualisiert werden soll.
     * @param updatedRessourceBooking Das aktualisierte ressourcebooking-Objekt, das die neuen Werte für die Buchung enthält.
     * @return Das aktualisierte ressourcebooking-Objekt, nachdem die Aktualisierung durchgeführt wurde.
     * @throws ResourceNotFoundException wenn die ressource nicht gefunden wurde.
     */
    @Override
    public RessourceBooking updateBookingById(Long ressourceBookingId, RessourceBooking updatedRessourceBooking) throws ResourceNotFoundException {
        // Checking for mandatory fields on the updated booking
        if (updatedRessourceBooking.getRessource() == null || updatedRessourceBooking.getStart() == null || updatedRessourceBooking.getEndTime() == null || updatedRessourceBooking.getCreatedOn() == null) {
            throw new IllegalArgumentException("Updated booking must have valid desk, employee, Date, StartTime, EndTime and Creation Date.");
        }

        return this.ressourceBookingJPARepo.findById(ressourceBookingId).map(existingBooking -> {
            Ressource fetchedRessource;
            try {
                fetchedRessource = this.ressourceJPARepo.findById(updatedRessourceBooking.getRessource().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("The ressource with the ID: " + updatedRessourceBooking.getRessource().getId() + " was not found!"));
            } catch (ResourceNotFoundException e) {
                logger.error(e.getMessage());
                throw new RuntimeException("Failed to update booking due to missing ressource. Original error: " + e.getMessage());
            }

            Employee fetchedEmployee;
            try {
                fetchedEmployee = this.employeeJPARepo.findById(updatedRessourceBooking.getEmployee().getId())
                        .orElseThrow(() -> new EmployeeNotFoundException("The employee with the ID: " + updatedRessourceBooking.getEmployee().getId() + " was not found!"));
            } catch (EmployeeNotFoundException e) {
                logger.error(e.getMessage());
                throw new RuntimeException("Failed to update booking due to missing employee. Original error: " + e.getMessage());
            }
            existingBooking.setRessource(fetchedRessource);
            existingBooking.setEmployee(fetchedEmployee);
            existingBooking.setDate(updatedRessourceBooking.getDate());
            existingBooking.setStart(updatedRessourceBooking.getStart());
            existingBooking.setEndTime(updatedRessourceBooking.getEndTime());
            existingBooking.setUpdatedOn(LocalDateTime.now());
            return existingBooking;
        }).orElseThrow(() -> new ResourceNotFoundException("The ressource booking with the ID: " + ressourceBookingId + " was not found!"));
    }

    /**
     * Die Methode updateBooking wird verwendet, um eine vorhandene Ressourcenbuchung zu aktualisieren. Sie akzeptiert ein aktualisiertes ressourcebooking-Objekt als Parameter und gibt das aktualisierte ressourcebooking-Objekt zurück.
     *
     * @param updatedBooking Das aktualisierte ressourcebooking-Objekt, das die neuen Werte für die Buchung enthält.
     * @return Das aktualisierte ressourcebooking-Objekt, nachdem die Aktualisierung durchgeführt wurde.
     * @throws ResourceNotFoundException Wenn die ressource nicht gefunden wurde.
     */
    @Override
    public RessourceBooking updateBooking(RessourceBooking updatedBooking) throws ResourceNotFoundException {
        if (updatedBooking.getId() == null) {
            throw new IllegalArgumentException("Id cannot be null when updating");
        }
        return this.ressourceBookingJPARepo.saveAndFlush(updatedBooking);
    }

    /**
     * Die Methode deleteBookingById wird verwendet, um eine Ressourcenbuchung anhand ihrer ID zu löschen.
     *
     * @param id Die ID der Ressourcenbuchung, die gelöscht werden soll.
     * @throws ResourceDeletionFailureException Wird ausgelöst, wenn die Ressourcenbuchung mit der angegebenen ID nicht gefunden werden kann und daher nicht gelöscht werden kann.
     */
    @Override
    public void deleteBookingById(Long id) throws ResourceDeletionFailureException {
        Optional<RessourceBooking> bookingOptional = this.ressourceBookingJPARepo.findById(id);
        if (bookingOptional.isPresent()) {
            this.ressourceBookingJPARepo.deleteById(id);
        } else {
            throw new ResourceDeletionFailureException("The ressource booking with the ID: " + id + " was not found!");
        }
    }

    /**
     * Die Methode getAvailableRessources wird verwendet, um eine Liste von verfügbaren Ressourcen für ein bestimmtes Datum und einen bestimmten Zeitraum zu erhalten.
     *
     * @param date  Das Datum, für das verfügbare Ressourcen gesucht werden.
     * @param start Die Startzeit des gewünschten Zeitraums.
     * @param end   Die Endzeit des gewünschten Zeitraums.
     * @return Die Methode gibt eine Liste von ressource-Objekten zurück, die als verfügbar für den angegebenen Zeitraum betrachtet werden.
     */
    @Override
    public List<Ressource> getAvailableRessources(LocalDate date, LocalTime start, LocalTime end) {
        List<Ressource> allRessources = this.ressourceJPARepo.findAll();
        List<Ressource> availableRessources = new ArrayList<>();

        for (Ressource ressource : allRessources) {
            if (isRessourceAvailable(ressource, date, start, end)) {
                availableRessources.add(ressource);
            }
        }
        return availableRessources;
    }

    /**
     * Die Methode isRessourceAvailable wird verwendet, um zu überprüfen, ob eine bestimmte ressource für ein angegebenes Datum und einen angegebenen Zeitraum verfügbar ist.
     *
     * @param ressource Die ressource, deren Verfügbarkeit überprüft werden soll.
     * @param date      Das Datum, für das die Verfügbarkeit überprüft werden soll.
     * @param start     Die Startzeit des zu überprüfenden Zeitraums.
     * @param end       Die Endzeit des zu überprüfenden Zeitraums.
     * @return
     */
    @Override
    public boolean isRessourceAvailable(Ressource ressource, LocalDate date, LocalTime start, LocalTime end) {
        List<RessourceBooking> overlappingBookings = this.ressourceBookingJPARepo.getBookingsByRessourceAndDateAndStartBetween(ressource, date, start, end);
        return overlappingBookings.isEmpty();
    }

    /**
     * Die Methode getBookingsByRessourceAndDateAndBookingTimeBetween wird verwendet, um alle Buchungen einer bestimmten ressource abzurufen, die für ein angegebenes Datum und eine angegebene Buchungszeit innerhalb eines Zeitbereichs liegen.
     *
     * @param ressource Die ressource, für die die Buchungen abgerufen werden sollen.
     * @param date      Das Datum, für das die Buchungen abgerufen werden sollen.
     * @param start     Die Startzeit des Buchungszeitraums.
     * @param endTime   Die Endzeit des Buchungszeitraums.
     * @return Die Methode gibt eine Liste von ressourcebooking-Objekten zurück, die den angegebenen Kriterien entsprechen.
     */
    @Override
    public List<RessourceBooking> getBookingsByRessourceAndDateAndBookingTimeBetween(Ressource ressource, LocalDate date, LocalTime start, LocalTime endTime) {
        return this.ressourceBookingJPARepo.getBookingsByRessourceAndDateAndStartBetween(ressource, date, start, endTime);
    }

    /**
     * Die Methode getBookingByDateAndByStartBetween wird verwendet, um alle Buchungen für ein bestimmtes Datum abzurufen, bei denen der Startzeitpunkt innerhalb eines angegebenen Zeitbereichs liegt.
     *
     * @param date       Das Datum, für das die Buchungen abgerufen werden sollen.
     * @param startOfDay Der Startzeitpunkt des Zeitbereichs.
     * @param endOfDay   Der Endzeitpunkt des Zeitbereichs.
     * @return Die Methode gibt eine Liste von ressourcebooking-Objekten zurück, die den angegebenen Kriterien entsprechen.
     */
    @Override
    public List<RessourceBooking> getBookingByDateAndByStartBetween(LocalDate date, LocalTime startOfDay, LocalTime endOfDay) {
        return ressourceBookingJPARepo.getBookingsByDateAndStartBetween(date, startOfDay, endOfDay);
    }

    /**
     * Die Methode save wird verwendet, um eine ressource-Buchung zu speichern oder zu aktualisieren.
     *
     * @param booking Das ressource-Buchungsobjekt, das gespeichert oder aktualisiert werden soll.
     * @return Die Methode gibt das gespeicherte oder aktualisierte ressource-Buchungsobjekt vom Typ ressourcebooking zurück.
     */
    @Override
    public RessourceBooking save(RessourceBooking booking) {
        return this.ressourceBookingJPARepo.save(booking);
    }
}