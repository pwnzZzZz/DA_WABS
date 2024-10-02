package com.itkolleg.bookingsystem.service.room;

import com.itkolleg.bookingsystem.domains.Room;
import com.itkolleg.bookingsystem.exceptions.roomExceptions.RoomDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.roomExceptions.RoomNotFoundException;
import com.itkolleg.bookingsystem.repos.room.DBAccessRoom;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * The RoomServiceImplementation class is responsible for providing room-related services.
 * It implements the RoomService interface.
 */
@Service
public class RoomServiceImplementation implements RoomService {
    private final DBAccessRoom dbAccessRoom;

    /**
     * Constructs a new RoomServiceImplementation with the specified DBAccessRoom object.
     *
     * @param dbAccessRoom the DBAccessRoom object to be used for accessing the database.
     */
    public RoomServiceImplementation(DBAccessRoom dbAccessRoom) {
        this.dbAccessRoom = dbAccessRoom;
    }

    /**
     * Adds a room to the system.
     *
     * @param room the room to be added.
     * @return the added room.
     * @throws ExecutionException   if an execution exception occurs while adding the room.
     * @throws InterruptedException if the current thread is interrupted while waiting.
     */
    @Override
    public Room addRoom(Room room) throws ExecutionException, InterruptedException {
        return this.dbAccessRoom.addRoom(room);
    }

    /**
     * Retrieves all rooms from the system.
     *
     * @return a list of all rooms.
     * @throws ExecutionException   if an execution exception occurs while retrieving the rooms.
     * @throws InterruptedException if the current thread is interrupted while waiting.
     */
    @Override
    public List<Room> getAllRooms() throws ExecutionException, InterruptedException {
        return this.dbAccessRoom.getAllRooms();
    }

    /**
     * Retrieves a room by its ID.
     *
     * @param id the ID of the room to retrieve.
     * @return the room with the specified ID.
     * @throws RoomNotFoundException if the room with the specified ID is not found.
     * @throws ExecutionException     if an execution exception occurs while retrieving the room.
     * @throws InterruptedException if the current thread is interrupted while waiting.
     */
    @Override
    public Room getRoomById(Long id) throws RoomNotFoundException, ExecutionException, InterruptedException {
        return this.dbAccessRoom.getRoomById(id);
    }

    /**
     * Updates a room in the system.
     *
     * @param room the room to be updated.
     * @return the updated room.
     * @throws RoomNotFoundException if the room to be updated is not found.
     * @throws ExecutionException     if an execution exception occurs while updating the room.
     * @throws InterruptedException if the current thread is interrupted while waiting.
     */
    @Override
    public Room updateRoom(Room room) throws RoomNotFoundException, ExecutionException, InterruptedException {
        return this.dbAccessRoom.updateRoom(room);
    }

    /**
     * Deletes a room by its ID.
     *
     * @param id the ID of the room to delete.
     * @throws RoomDeletionNotPossibleException if the room cannot be deleted.
     */
    @Override
    public void deleteRoomById(Long id) throws RoomDeletionNotPossibleException {
        this.dbAccessRoom.deleteRoomById(id);
    }
}