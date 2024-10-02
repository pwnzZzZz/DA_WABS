package com.itkolleg.bookingsystem.repos.desk;

import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Port;
import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * This interface defines the repository for desks in the booking system.
 * @author Sonja Lechner
 * @version 1.0
 * since 2023-05-24
 */
public interface DeskRepo {

    /**
     * Adds a desk to the repository.
     * @param desk The desk to add.
     * @return The added desk.
     */
    Desk addDesk(Desk desk);

    /**
     * Retrieves all desks from the repository.
     * @return A list of all desks.
     */
    List<Desk> getAllDesks();

    /**
     Retrieves a page of desks from the repository.
     @param pageable The pageable object.
     @return A page of desks.
     */
    Page<Desk> getAllDesksByPage(Pageable pageable);

    /**
     * Retrieves a desk by its ID.
     * @param id The ID of the desk.
     * @return The desk with the specified ID.
     * @throws ResourceNotFoundException If the desk is not found.
     */
    Desk getDeskById(Long id) throws ResourceNotFoundException;

    /**
     * Updates a desk by its ID.
     * @param id The ID of the desk.
     * @param desk The updated desk.
     * @return The updated desk.
     * @throws ResourceNotFoundException If the desk is not found.
     */
    Desk updateDeskById(Long id, Desk desk) throws ResourceNotFoundException;

    /**
     * Updates a desk.
     * @param desk The updated desk.
     * @return The updated desk.
     * @throws ResourceNotFoundException If the desk is not found.
     */
    Desk updateDesk(Desk desk) throws ResourceNotFoundException;

    /**
     * Deletes a desk by its ID.
     *
     * @param id The ID of the desk.
     * @throws ResourceDeletionFailureException If the desk deletion fails.
     */
   void deleteDeskById(Long id) throws ResourceDeletionFailureException;

    /**
     * Creates a port for a desk.
     * @param deskId The ID of the desk.
     * @param newPort The new port to create.
     * @return The updated desk with the new port.
     */
    Desk createPort(Long deskId, Port newPort);

    /**
     * Updates a port for a desk.
     * @param deskId The ID of the desk.
      * @param portName The name of the port to update.
      * @param updatedPort The updated port.
      * @return The updated desk with the updated port.
     */
    Desk updatePort(Long deskId, String portName, Port updatedPort);

    /**
     *
     * Deletes a port from a desk.
     * @param deskId The ID of the desk.
     * @param portName The name of the port to delete.
     * @return The updated desk without the deleted port.
     */
    Desk deletePort(Long deskId, String portName);

    /**
     * Retrieves the ports of a desk.
     * @param deskId The ID of the desk.
     * @return A list of ports of the desk.
     */
    List<Port> getPorts(Long deskId);

}