package com.itkolleg.bookingsystem.domains;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * DTO for {@link Timeslot}
 */
@Value
public class TimeslotDto implements Serializable {
    LocalTime startTime;
    LocalTime endTime;
    String name;
}