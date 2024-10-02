package com.itkolleg.bookingsystem.exceptions;

/**
 * This class represents a custom exception which is thrown when validation on user input fails.
 *
 * @author Sonja Lechner
 * @version 1.0
 * @since 2023-05-24
 */
public class ValidationFailureException extends RuntimeException {

    private final String field;
    private final String errorMessage;

    /**
     * Constructs a new exception with the specified field name and error message.
     *
     * @param field        the name of the field that failed validation
     * @param errorMessage the detail error message
     */
    public ValidationFailureException(String field, String errorMessage) {
        super(errorMessage);
        this.field = field;
        this.errorMessage = errorMessage;
    }

    /**
     * Constructs a new exception with a general message, using the message as the field name and error message.
     *
     * @param message the detail message, used for both field name and error message
     */
    public ValidationFailureException(String message) {
        super(message);
        this.field = message;
        this.errorMessage = message;
    }

    /**
     * Get the field name associated with this validation error.
     *
     * @return the field name
     */
    public String getField() {
        return field;
    }

    /**
     * Get the error message for this validation error.
     *
     * @return the error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }
}
