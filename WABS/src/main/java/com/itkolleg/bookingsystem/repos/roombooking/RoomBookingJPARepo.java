package com.itkolleg.bookingsystem.repos.roombooking;

import com.itkolleg.bookingsystem.domains.booking.RoomBooking;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface RoomBookingJPARepo extends JpaRepository<RoomBooking, Long> {
    List<RoomBooking> getBookingsByRoom(Room room);

    List<RoomBooking> getBookingsByRoomId(Long roomId);

    List<RoomBooking> getBookingsByDate(LocalDate date);

    List<RoomBooking> getBookingsByEmployee(Employee employee);

    List<RoomBooking> getBookingsByEmployeeId(Long employeeId);

    List<RoomBooking> getBookingsByRoomAndDate(Room room, LocalDate date);

    List<RoomBooking> getBookingsByRoomAndEmployee(Room room, Employee employee);

    List<RoomBooking> getBookingsByEmployeeAndDate(Employee employee, LocalDate date);

    List<RoomBooking> getBookingsByEmployeeAndDateAndRoom(Employee employee, LocalDate date, Room room);

    List<RoomBooking> getBookingsByDateAndStartBetween(LocalDate date, LocalTime start, LocalTime endTime);

    List<RoomBooking> getBookingsByRoomAndDateAndStartBetween(Room room, LocalDate date, LocalTime start, LocalTime endTime);

    List<RoomBooking> getBookingsByEmployeeIdAndDateAndRoomId(Long employeeId, LocalDate date, Long roomId);

}
