package com.itkolleg.bookingsystem.repos.holiday;

import com.itkolleg.bookingsystem.domains.PublicHoliday;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@ComponentScan({"com.itkolleg.repos"})
public class HolidayRepo_JPAH2 implements HolidayRepo {
    private final HolidayJPARepo holidayJPARepo;

    public HolidayRepo_JPAH2(HolidayJPARepo holidayJPARepo) {
        this.holidayJPARepo = holidayJPARepo;
    }

    @Override
    public void addHoliday(PublicHoliday publicHoliday) {
        this.holidayJPARepo.save(publicHoliday);

    }

    @Override
    public List<PublicHoliday> getAllHolidays() {
        return this.holidayJPARepo.findAll();
    }

    @Override
    public void deleteHoliday(Long id) {
        this.holidayJPARepo.deleteById(id);
    }

    @Override
    public boolean isBookingAllowedOnHoliday(LocalDate date) {
        PublicHoliday publicHoliday = this.holidayJPARepo.findByDate(date);
        // If holidays object is null (no entry in holidays table), consider it a normal day and allow bookings
        return publicHoliday == null || publicHoliday.isBookingAllowed();
    }

    @Override
    public PublicHoliday findByDate(LocalDate date) {
        return this.holidayJPARepo.findByDate(date);
    }
}
