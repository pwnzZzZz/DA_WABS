package com.itkolleg.bookingsystem.service.room;

import com.itkolleg.bookingsystem.domains.Room;
import com.itkolleg.bookingsystem.exceptions.roomExceptions.RoomDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.roomExceptions.RoomNotFoundException;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RoomService {
    Room addRoom(Room room) throws ExecutionException, InterruptedException;

    List<Room> getAllRooms() throws ExecutionException, InterruptedException;

    Room getRoomById(Long id) throws RoomNotFoundException, ExecutionException, InterruptedException;

    Room updateRoom(Room room) throws RoomNotFoundException, ExecutionException, InterruptedException;

    void deleteRoomById(Long id) throws RoomDeletionNotPossibleException;
}
