package com.itkolleg.bookingsystem.domains;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents detailed information about an error that occurred within the booking system.
 * This class captures the title and detailed message of the error, providing clarity and context to the end-user.
 * It can be integrated with exception handling mechanisms to provide structured error responses.
 * Features:
 * - Validation: Ensures that the error title and message are not empty.
 * - Flexibility: Can be extended or integrated with other error handling mechanisms as needed.
 * Note:
 * - Consider using this class in conjunction with custom exception classes for comprehensive error handling.
 *
 * @author Sonja Lechner
 * @version 1.0
 * @since 2023-05-24
 */

@Slf4j
public class ErrorDetails {

    /**
     * The title of the error, providing a brief overview.
     */
    @NotEmpty(message = "Error title must not be empty")
    @Getter
    @Setter
    private String title;

    /**
     * The detailed message of the error, providing specific information about what went wrong.
     */
    @NotEmpty(message = "Error message must not be empty")
    @Getter
    @Setter
    private String message;

    /**
     * Default constructor.
     */
    public ErrorDetails() {
    }

    /**
     * Constructor to initialize an error with a specific title and detailed message.
     *
     * @param title   The title of the error.
     * @param message The detailed message of the error.
     */
    public ErrorDetails(String title, String message) {
        this.title = title;
        this.message = message;
    }
}
