package com.itkolleg.bookingsystem.repos.holiday;


import com.itkolleg.bookingsystem.domains.PublicHoliday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface HolidayJPARepo extends JpaRepository<PublicHoliday, Long> {
    PublicHoliday findByDate(LocalDate date);
}
