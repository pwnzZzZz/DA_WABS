package com.itkolleg.bookingsystem.domains;

/**
 * This enum represents the error codes used in the booking system.
 * Each error code corresponds to a specific type of error that can occur within the system.
 * These codes can be used for logging, error handling, and providing feedback to the user.
 *
 * @author Sonja Lechner
 * @version 1.2
 * @since 2023-08-17
 */
public enum ErrorCode {
    METHOD_ARGUMENT_NOT_VALID("1001"),
    RESOURCE_NOT_FOUND("1002"),
    DESK_NOT_AVAILABLE("1003"),
    DATA_ACCESS_ERROR("1004"),
    RESOURCE_DELETION_FAILURE("1005"),
    GENERAL_ERROR("9999");

    private final String code;

    /**
     * Constructs an ErrorCode enum constant with the specified code.
     * @param code The error code.
     */
    ErrorCode(String code) {
        this.code = code;
    }

    /**
     * Returns the code associated with the error.
     * @return The error code.
     */
    public String getCode() {
        return code;
    }
}
