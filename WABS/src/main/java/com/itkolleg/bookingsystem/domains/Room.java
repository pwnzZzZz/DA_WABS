package com.itkolleg.bookingsystem.domains;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**

 Represents a room in the booking system.
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Room {

    private static final Logger logger = LoggerFactory.getLogger(Room.class);

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String floor;

    private String info;

    /**

     Constructs a room object with the specified ID and floor.
     @param id The ID of the room.
     @param floor The floor of the room.
     */
    public Room(Long id, String floor) {
        this.id = id;
        this.floor = floor;
    }
    /**

     Returns a string representation of the room object.
     @return A string representation of the room object.
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "roomId = " + id + ", " +
                "floor = " + floor + ", " +
                "info = " + info + ")";
    }
}