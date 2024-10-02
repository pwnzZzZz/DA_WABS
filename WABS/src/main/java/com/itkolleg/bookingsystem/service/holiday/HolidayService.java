package com.itkolleg.bookingsystem.service.holiday;

import com.itkolleg.bookingsystem.domains.PublicHoliday;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public interface HolidayService {
    /**
     * Logger instance for logging.
     */
    Logger logger = LoggerFactory.getLogger(HolidayService.class);

    void addHoliday(PublicHoliday publicHoliday);

    List<PublicHoliday> getAllHolidays();

    boolean isBookingAllowedOnHoliday(LocalDate date);

}