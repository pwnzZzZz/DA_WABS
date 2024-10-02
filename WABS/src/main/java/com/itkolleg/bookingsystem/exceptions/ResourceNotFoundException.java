package com.itkolleg.bookingsystem.exceptions;

import java.io.Serial;

/**
 * This class represents a custom exception which is thrown when a particular resource cannot be found in the system.
 *
 * @author Sonja Lechner
 * @version 1.0
 * @since 2023-05-24
 */
public class ResourceNotFoundException extends Exception {

    /**
     * Serializable classes should define this field with unique value to ensure
     * consistent serialization across different java implementations.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     *
     * @param message the detail message.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause of the exception.
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
