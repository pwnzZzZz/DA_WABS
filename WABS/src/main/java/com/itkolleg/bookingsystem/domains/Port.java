package com.itkolleg.bookingsystem.domains;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents a port within the system.
 *
 * <p>
 * A port is an essential component of a desk setup, providing connectivity options for various devices.
 * The port has a name attribute which describes its type or functionality (e.g., "USB", "HDMI").
 * </p>
 *
 * <p>
 * This class is marked as {@code @Embeddable}, indicating that its instances will be embedded as part of the
 * owning entity, in this case, the Desk. This means that the attributes of the Port will be part of the
 * Desk table in the database schema, rather than a separate table.
 * </p>
 *
 * <p>
 * The {@code @Size} annotation on the name attribute ensures that the name of the port is at least 2 characters long,
 * providing a basic level of validation.
 * </p>
 *
 * Note:
 * - The {@code @NoArgsConstructor} annotation provides a no-argument constructor, which is a requirement for JPA entities and embeddables.
 * - The {@code @Getter} and {@code @Setter} annotations from Lombok generate the getter and setter methods for the class attributes.
 *
 * @author Sonja Lechner
 * @version 1.3
 * @since 2023-07-23
 */
@Embeddable
@NoArgsConstructor
@Getter
@Setter
@Slf4j
public class Port {

    /**
     * Represents the name or type of the port (e.g., "USB", "HDMI").
     * This attribute provides a description of the port's functionality.
     */
    @Size(min = 2, message = "The name of the port must be at least 2 characters long")
    private String name;

    /**
     * Constructor to initialize a port with a specific name.
     *
     * @param name The name or type of the port.
     */
    public Port(String name) {
        this.name = name;
    }

    /**
     * Provides a string representation of the port, which is its name.
     *
     * @return The name or type of the port.
     */
    @Override
    public String toString() {
        return this.name;
    }
}
