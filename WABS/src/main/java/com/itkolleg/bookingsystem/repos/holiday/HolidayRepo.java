package com.itkolleg.bookingsystem.repos.holiday;

import com.itkolleg.bookingsystem.domains.PublicHoliday;

import java.time.LocalDate;
import java.util.List;

public interface HolidayRepo {

    void addHoliday(PublicHoliday publicHoliday);

    List<PublicHoliday> getAllHolidays();

    void deleteHoliday(Long id);

    boolean isBookingAllowedOnHoliday(LocalDate date);

    PublicHoliday findByDate(LocalDate date);

}
