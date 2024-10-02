package com.itkolleg.bookingsystem.repos.deskbooking;

import com.itkolleg.bookingsystem.domains.booking.DeskBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeskBookingJPARepo extends JpaRepository<DeskBooking, Long> {

    Optional<DeskBooking> findBookingsById(Long bookingId);

    List<DeskBooking> findBookingsByDeskId(Long deskId);

    List<DeskBooking> findBookingsByDate(LocalDate date);

    List<DeskBooking> findBookingsByEmployeeId(Long employeeId);

    List<DeskBooking> findBookingsByEmployeeIdAndDate(Long employeeId, LocalDate date);

    List<DeskBooking> findBookingsByDeskIdAndDate(Long deskId, LocalDate date);

    List<DeskBooking> findBookingsByEmployeeIdAndDeskId(Long employeeId, Long DeskId);

    List<DeskBooking> findByDateAndStartBetween(LocalDate date, LocalTime start, LocalTime end);

    @Query("SELECT d FROM DeskBooking d WHERE d.employee.id = :employeeId AND d.date = :date AND d.desk.id = :deskId")
    List<DeskBooking> findBookingsByEmployeeIdAndDeskIdAndDate(@Param("employeeId") Long employeeId, @Param("deskId") Long deskId, @Param("date") LocalDate date);

    @Query("SELECT d FROM DeskBooking d WHERE d.desk.id = :deskId AND d.date = :date AND d.start BETWEEN :start AND :end")
    List<DeskBooking> findBookingsByDeskIdDateAndTimeRange(@Param("deskId") Long deskId, @Param("date") LocalDate date, @Param("start") LocalTime start, @Param("end") LocalTime end);

    @Query("SELECT d FROM DeskBooking d WHERE d.employee.id = :employeeId AND d.desk.id = :deskId AND d.date = :date AND d.start BETWEEN :start AND :end")
    List<DeskBooking> findBookingsByEmployeeDeskDateAndTimeRange(@Param("employeeId") Long employeeId, @Param("deskId") Long deskId, @Param("date") LocalDate date, @Param("start") LocalTime start, @Param("end") LocalTime end);
}


