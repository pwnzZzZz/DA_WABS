package com.itkolleg.bookingsystem.repos.room;

import com.itkolleg.bookingsystem.domains.booking.RoomBooking;
import com.itkolleg.bookingsystem.domains.Room;
import com.itkolleg.bookingsystem.exceptions.roomExceptions.RoomDeletionNotPossibleException;
import com.itkolleg.bookingsystem.exceptions.roomExceptions.RoomNotFoundException;
import com.itkolleg.bookingsystem.repos.roombooking.RoomBookingRepo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**

 Implementation of the DBAccessRoom interface using JPA for database access.
 */
@Component
public class DBAccessRoomJPHA2 implements DBAccessRoom {

    private final RoomJPARepo roomJPARepo;
    private final RoomBookingRepo roomBookingRepo;

    /**

     Constructs a DBAccessRoomJPHA2 object with the specified RoomJPARepo and RoomBookingRepo.
     @param roomJPARepo The RoomJPARepo used for room database operations.
     @param roomBookingRepo The RoomBookingRepo used for room booking database operations.
     */
    public DBAccessRoomJPHA2(RoomJPARepo roomJPARepo, RoomBookingRepo roomBookingRepo) {
        this.roomJPARepo = roomJPARepo;
        this.roomBookingRepo = roomBookingRepo;
    }
    /**

     Adds a room to the database.
     @param room The room to be added.
     @return The added room.
     @throws ExecutionException If an execution error occurs.
     @throws InterruptedException If the operation is interrupted.
     */
    @Override
    public Room addRoom(Room room) throws ExecutionException, InterruptedException {
        return this.roomJPARepo.save(room);
    }
    /**

     Retrieves all rooms from the database.
     @return A list of all rooms.
     @throws ExecutionException If an execution error occurs.
     @throws InterruptedException If the operation is interrupted.
     */
    @Override
    public List<Room> getAllRooms() throws ExecutionException, InterruptedException {
        return this.roomJPARepo.findAll();
    }
    /**

     Retrieves a room by its ID from the database.
     @param id The ID of the room.
     @return The room with the specified ID.
     @throws RoomNotFoundException If the room is not found.
     @throws ExecutionException If an execution error occurs.
     @throws InterruptedException If the operation is interrupted.
     */
    @Override
    public Room getRoomById(Long id) throws RoomNotFoundException, ExecutionException, InterruptedException {
        Optional<Room> roomOptional = this.roomJPARepo.findById(id);
        if (roomOptional.isPresent()) {
            return roomOptional.get();
        } else {
            throw new RoomNotFoundException("room not found for ID: " + id);
        }
    }
    /**

     Updates a room in the database.
     @param room The room to be updated.
     @return The updated room.
     @throws RoomNotFoundException If the room is not found.
     @throws ExecutionException If an execution error occurs.
     @throws InterruptedException If the operation is interrupted.
     */
    @Override
    public Room updateRoom(Room room) throws RoomNotFoundException, ExecutionException, InterruptedException {
        return this.roomJPARepo.save(room);
    }
    /**

     Deletes a room from the database by its ID.

     @param id The ID of the room to be deleted.

     @throws RoomDeletionNotPossibleException If the room deletion is not possible.
     */
    @Override
    public void deleteRoomById(Long id) throws RoomDeletionNotPossibleException {
        List<RoomBooking> bookings = this.roomBookingRepo.getBookingsByRoomId(id);

        if (!bookings.isEmpty()) {
            throw new RoomDeletionNotPossibleException("room already booked");
        }
        this.roomJPARepo.deleteById(id);
    }
}