package com.itkolleg.bookingsystem.service.desk;

import com.itkolleg.bookingsystem.domains.Desk;
import com.itkolleg.bookingsystem.domains.Port;
import com.itkolleg.bookingsystem.exceptions.ResourceDeletionFailureException;
import com.itkolleg.bookingsystem.exceptions.ResourceNotFoundException;
import com.itkolleg.bookingsystem.repos.desk.DeskRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 Service implementation for managing desks.
 *
 * @author Sonja Lechner
 * @version 1.0
 * @since 2023-05-26
 */
@Slf4j
@Service
public class DeskServiceImplementation implements DeskService {
    //Dependency Injection
    private final DeskRepo deskRepo;

    public DeskServiceImplementation(DeskRepo deskRepo) {
        this.deskRepo = deskRepo;
    }

    /**
     * Adds a new desk.
     *
     * @param desk The desk to add.
     * @return The added desk.
     */
    @Override
    public Desk addDesk(Desk desk) {
        return this.deskRepo.addDesk(desk);
    }

    /**
     Retrieves the total number of desks.
     @return The total number of desks.
     */
    public int getTotalDesks() {
        return this.deskRepo.getAllDesks().size();
    }

    /**
     Retrieves all desks.
     @return The list of all desks.
     */
    @Override
    public List<Desk> getAllDesks() {
        return this.deskRepo.getAllDesks();
    }

    /**
     Retrieves all desks with pagination.
     @param pageable The pageable object for pagination configuration.
     @return The page of desks.
     */
    public Page<Desk> getAllDesksByPage(Pageable pageable) {
        return this.deskRepo.getAllDesksByPage(pageable);
    }

    /**
     Retrieves a desk by its ID.
     @param id The ID of the desk.
     @return The desk with the specified ID.
     @throws ResourceNotFoundException If the desk with the specified ID is not found.
     */
    @Override
    public Desk getDeskById(Long id) throws ResourceNotFoundException {
        return this.deskRepo.getDeskById(id);
    }

    /**
     Updates a desk by its ID.
     @param id The ID of the desk to update.
     @param desk The updated desk object.
     @return The updated desk.
     @throws ResourceNotFoundException If the desk with the specified ID is not found.
     */
    @Override
    public Desk updateDeskById(Long id, Desk desk) throws ResourceNotFoundException {
         this.deskRepo.updateDeskById(id, desk);
         return this.deskRepo.getDeskById(id);
    }

    /**
     * Updates a desk.
     *
     * @param desk The updated desk.
     * @return The updated desk.
     * @throws ResourceNotFoundException If the desk with the specified ID is not found.
     */
    public Desk updateDesk(Desk desk) throws ResourceNotFoundException {
        return this.deskRepo.updateDesk(desk);
    }

    /**
     * Deletes a desk by its ID.
     *
     * @param id The ID of the desk to delete.
     * @throws ResourceDeletionFailureException If the deletion of the desk fails.
     */
    @Override
    public void deleteDeskById(Long id) throws ResourceDeletionFailureException {
        this.deskRepo.deleteDeskById(id);
    }

    /**
     Creates a new port for a desk.
     @param deskId The ID of the desk.
     @param newPort The new port to create.
     @return The desk with the new port.
     */
    @Override
    public Desk createPort(Long deskId, Port newPort) {
        return this.deskRepo.createPort(deskId, newPort);
    }

    /**
     Updates an existing port of a desk.
     @param deskId The ID of the desk.
     @param portName The name of the port to update.
     @param updatedPort The updated port object.
     @return The desk with the updated port.
     */
    @Override
    public Desk updatePort(Long deskId, String portName, Port updatedPort) {
        return this.deskRepo.updatePort(deskId, portName, updatedPort);
    }

    /**
     Deletes a port of a desk.
     @param deskId The ID of the desk.
     @param portName The name of the port to delete.
     @return The desk after deleting the port.
     */
    @Override
    public Desk deletePort(Long deskId, String portName) {
        return this.deskRepo.deletePort(deskId, portName);
    }

    /**
     Retrieves the list of ports for a desk.
     @param deskId The ID of the desk.
     @return The list of ports for the desk.
     */
    public List<Port> getPorts(Long deskId) {
        return this.deskRepo.getPorts(deskId);
    }
}