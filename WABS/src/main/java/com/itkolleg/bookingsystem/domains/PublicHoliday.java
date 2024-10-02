package com.itkolleg.bookingsystem.domains;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;

/**
 * This class represents public holidays in the booking system.
 *
 * @author Sonja Lechner
 * @version 1.1
 * @since 2023-07-17
 */
@Entity
@Getter
@Setter
@Slf4j
public class PublicHoliday {

    /**
     * The unique identifier of the public holidays.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The date of the public holidays.
     */
    @FutureOrPresent(message = "The date of the public holidays must not be in the past")
    private LocalDate date;

    /**
     * The description of the public holidays.
     */
    @NotEmpty(message = "The description of the public holidays must not be empty")
    private String description;

    /**
     * Specifies whether booking is allowed on this public holidays or not.
     */
    private boolean isBookingAllowed;

    /**
     * Default constructor.
     */
    public PublicHoliday() {
    }

    /**
     * Constructor that sets up the date, description, and booking allowance for the public holidays.
     *
     * @param date             The date of the public holidays.
     * @param description      The description of the public holidays.
     * @param isBookingAllowed Specifies whether booking is allowed on this public holidays or not.
     */
    public PublicHoliday(LocalDate date, String description, boolean isBookingAllowed) {
        this.date = date;
        this.description = description;
        this.isBookingAllowed = isBookingAllowed;
    }
}
