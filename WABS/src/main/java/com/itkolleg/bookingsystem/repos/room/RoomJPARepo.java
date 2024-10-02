package com.itkolleg.bookingsystem.repos.room;

import com.itkolleg.bookingsystem.domains.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomJPARepo extends JpaRepository<Room, Long> {
    Room findRoomById(Long id);
}
