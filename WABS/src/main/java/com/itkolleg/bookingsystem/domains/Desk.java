package com.itkolleg.bookingsystem.domains;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a desk within the system.
 * This class captures essential details about a desk, including its unique identifier, number of monitors,
 * associated ports, and booking status. It provides utility methods to manage ports and check desk availability.
 * Features:
 * - Validation: Ensures that the desk number is not empty and the number of monitors is within a valid range.
 * - Port Management: Supports adding and removing ports associated with the desk.
 * - Availability Check: Provides a method to check if the desk is available for booking.
 * Note:
 * - The `@Slf4j` annotation provides logging capabilities.
 * - The `@EqualsAndHashCode(of = "id")` ensures that only the id is used for equals and hashCode methods.
 * Important:
 * - Always ensure that the desk is available before creating a booking.
 *
 * @author Sonja Lechner
 * @version 1.0
 * @since 2023-05-24
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Slf4j
public class Desk {
    /**
     * A list of ports available at the desk.
     */
    @NotEmpty(message = "Ports must not be empty")
    @Size(min = 1, message = "At least one port is required")
    @ElementCollection
    private List<Port> ports = new ArrayList<>();

    /**
     * The identifier for the desk.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /**
     * The desk number.
     */
    @NotEmpty(message = "desk number must not be empty")
    @Size(max = 10, message = "desk number must not exceed 10 characters")
    private String deskNr;

    /**
     * The number of monitors at the desk.
     */
    @Digits(integer = 2, fraction = 0, message = "Number of monitors must be a positive integer (0 - 99)")
    @Min(value = 0, message = "Number of monitors can't be less than 0")
    @Max(value = 10, message = "Number of monitors can't exceed 10")
    private int nrOfMonitors;


    /**
     * Overrides the default equals method to compare Desk objects based on their unique identifier.
     * <p>
     * This method checks if the provided object is of the same type and then compares their unique identifiers.
     * Two Desk objects are considered equal if they have the same unique identifier.
     * </p>
     * @param o The object to be compared with the current Desk object.
     * @return true if the provided object is a Desk and has the same unique identifier, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Desk desk = (Desk) o;
        return Objects.equals(id, desk.id);
    }

    /**
     * Overrides the default hashCode method to generate a hash code based on the Desk's unique identifier.
     * <p>
     * This method uses the unique identifier of the Desk to generate a hash code.
     * This ensures that Desks with the same unique identifier will have the same hash code.
     * </p>
     * @return A hash code value for the Desk object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Constructor for creating a desk with a specific desk number, number of monitors, and a list of ports.
     */
    public Desk(@NonNull String deskNr, int nrOfMonitors, List<Port> ports) {
        this.deskNr = deskNr;
        this.nrOfMonitors = nrOfMonitors;
        if (ports != null) {
            this.ports = ports;
        }
    }

    /**
     * Method to add a port to the list of ports for the desk.
     */
    public void addPort(Port port) {
        if (ports != null && !this.ports.contains(port)) {
            this.ports.add(port);
            log.info("Port " + port + " was created and added to desk: " + deskNr + ".");
        }
    }

    /**
     * Method to delete all ports from the list of ports for the desk.
     */
    public void deleteAllPorts() {
        if (!this.ports.isEmpty()) {
            this.ports.clear();
            log.info("All ports have been removed from desk: " + deskNr + ".");
        }
    }
}
