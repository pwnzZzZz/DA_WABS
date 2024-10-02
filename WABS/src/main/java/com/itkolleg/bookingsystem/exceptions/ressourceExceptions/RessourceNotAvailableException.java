package com.itkolleg.bookingsystem.exceptions.ressourceExceptions;

import java.io.Serial;

public class RessourceNotAvailableException extends Exception {

    /**
     * Serializable classes should define this field with unique value to ensure
     * consistent serialization across different java implementations.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public RessourceNotAvailableException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public RessourceNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}