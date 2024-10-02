package com.itkolleg.bookingsystem.domains.booking;
import com.itkolleg.bookingsystem.domains.Employee;
import com.itkolleg.bookingsystem.domains.Room;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;


/**

 Represents a room booking.

 Extends the base class booking.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoomBooking extends Booking {

    @ManyToOne
    private Room room;

    /**

     Constructs a roombooking object with the specified employee, room, date, start time, and end time.
     @param employee The employee object associated with the booking.
     @param room The room object associated with the booking.
     @param date The date of the booking.
     @param start The start time of the booking.
     @param endTime The end time of the booking.
     */
    public RoomBooking(Employee employee, Room room, LocalDate date, LocalTime start, LocalTime endTime) {
        super(employee, date, start, endTime);
        this.room = room;
    }
}